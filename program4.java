package accidentpack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;
/**
 * This java class reads the accident csv file and puts the data into an arraylist. The arraylist is then processed through a queue to count how many counters are needed. 
*  Some sources I used where https://www.youtube.com/watch?v=91CMnJeHJVc, https://www.youtube.com/watch?v=-Aud0cDh-J8, https://www.youtube.com/watch?v=70qy6_gw1Hc, https://www.youtube.com/watch?v=VX9CwPn-BBE and using chatgpt for basic things to save time.
* @author Collin Heidrich, Jesse Cyrbrophy
* @version Febuary 15 2021
*/

public class program4 {
	private static final String READABLE_DATETIME_FORMAT = "MM-dd-yyyy H:mm:ss";
	private static final int MINUTES_PER_DAY = 24 * 60;

	public static void main(String[] args) {
		executeProgram();
	}

	/*
	 * User prompt to simualate miniumum counters for county and state
	 */
	private static void executeProgram() {
//		String[] answers = promptUserForCriterions();
		String[] answers = new String[] { "Los Angeles", "CA" };
		String[] answers1 = new String[] { "Orange", "FL" };
		String[] answers2 = new String[] { "Harris", "TX" };
		String[] answers3 = new String[] { "Hamilton", "OH" };
		String[] answers4 = new String[] { "New Castle", "DE" };
		List<Report> reports = readCSVAndSort("accidents.csv");
		List<Report> californiaFiltered = filterReportsBy(answers, reports);
		List<Report> floridaFiltered = filterReportsBy(answers1, reports);
		List<Report> texasFiltered = filterReportsBy(answers2, reports);
		List<Report> ohioFiltered = filterReportsBy(answers3, reports);
		List<Report> delawareFiltered = filterReportsBy(answers4, reports);
		calculateMinCounters(californiaFiltered, answers[0], answers[1]);
		calculateMinCounters(floridaFiltered, answers1[0], answers1[1]);
		calculateMinCounters(texasFiltered, answers2[0], answers2[1]);
		calculateMinCounters(ohioFiltered, answers3[0], answers3[1]);
		calculateMinCounters(delawareFiltered, answers4[0], answers4[1]);

	}

	// O(N)
	/**
	 * A method to calculate the minimum counters for each county and state
	 * 
	 * @param reports, the list of accidents filtered by county and state
	 * @param county,  the county in which the accidents took place
	 * @param state,   the state in which the accidents took place
	 */
	public static void calculateMinCounters(List<Report> reports, String county, String state) {
		System.out.printf("--- %s , %s ---\n", county, state);
		long cumulativeTime = 0;
		Queue<Report> queue = new ArrayDeque<>();
		Instant start = null, end = null;
		if (!reports.isEmpty()) {
			queue.addAll(reports);
			start = Instant.now();
			cumulativeTime = executeProcessingTime(queue);
			end = Instant.now();
		}
		double timeInNanoSeconds = Duration.between(start, end).getNano();
		System.out.printf("%s seconds to simulate the process\n", timeInNanoSeconds / 1000000000);
		long counter = Math.floorDiv(cumulativeTime, (long) MINUTES_PER_DAY);
		System.out.printf("minimum number of counters: %d\n", counter, reports.size());
	}

//	O(n)
	/**
	 * A method to calculate total processing time by severity
	 * 
	 * @param queue, first elements into queue from report, parsing severity to
	 *               calculate total processing time
	 * @return totalTime to process by severity
	 */
	private static long executeProcessingTime(Queue<Report> queue) {
		Long totalTime = Long.parseLong("0");
		do {
			Report report = queue.poll();
			Integer severity = Integer.parseInt(report.getSeverity());
			totalTime = totalTime.longValue() + getTimeBasedOnSeverity(severity).longValue();
		} while (!queue.isEmpty());
		return totalTime;
	}

	/**
	 * A method to calculate processed time for each accident by severity
	 * 
	 * @param severity, the severity of accident based on Queue<Report> queue
	 * @return the processed time of each accident
	 */
	private static Integer getTimeBasedOnSeverity(Integer severity) {
		switch (severity) {
		case 1:
			return 60;
		case 2:
			return 120;
		case 3:
			return 180;
		case 4:
			return 240;
		default:
			return 0;
		}
	}

	/**
	 * A method for user to prompt County and State to filter accidents
	 * 
	 * @return User input
	 */

	private static String[] promptUserForCriterions() {
		String[] prompts = new String[] { "County:", "State:" };
		String[] answers = new String[prompts.length];
		int i = 0;
		try (Scanner z = new Scanner(System.in)) {
			for (String s : prompts) {
				helperInputMethod(answers, i, s, z);
				i++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return answers;
	}

	/**
	 * A method to help prompt user input
	 * 
	 * @param answers holds the array of user input
	 * @param i       keeps track of the index of user input
	 * @param prompt  the string that asks user what to input
	 * @param z       scans and reads allowing for user input
	 */
	private static void helperInputMethod(String[] answers, int i, String prompt, Scanner z) {
		System.out.printf("%s ", prompt);
		String input = z.nextLine();
		if (!input.isEmpty()) {
			answers[i] = input.trim();
		} else {
			helperInputMethod(answers, i, prompt, z);
		}
	}

	/**
	 * A method to sort the list of objects into a stream collected by filter
	 * 
	 * @param answers, the county and state to which is declared to fill the list
	 * @param reports, the list of accidents
	 * @return The list of accidents filtered by county and state
	 */

	public static List<Report> filterReportsBy(String[] answers, List<Report> reports) {
		return reports.parallelStream().filter(e -> e.getCounty().equalsIgnoreCase(answers[0].trim())
				&& e.getState().equalsIgnoreCase(answers[1].trim())).collect(Collectors.toList());
	}

	// O(2N)
	/**
	 * A method to sort the accidents (3).csv file into a list of reports
	 * 
	 * @param csvfile, the CSV file to read reports of specific types of data
	 * @return
	 */
	public static List<Report> readCSVAndSort(String csvfile) {
		List<Report> reports = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvfile))) {
			br.readLine(); // Skip header line
			String line;
			while ((line = br.readLine()) != null) {
				String[] rawData = line.split(",");
				Report report = new Report(rawData[0], rawData[1],
						dateFormatter(rawData[2].trim(), READABLE_DATETIME_FORMAT),
						dateFormatter(rawData[3].trim(), READABLE_DATETIME_FORMAT), rawData[4], rawData[5], rawData[6],
						rawData[7], rawData[8], rawData[9], Double.parseDouble(rawData[10]), rawData[11], rawData[12],
						rawData[13]);
				reports.add(report);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		reports.sort(Comparator.comparing(Report::getStartTime));
		return reports;
	}

	/**
	 * Method to parse start and end time in accidents (3).csv
	 * 
	 * @param dateTimeStr start and end time
	 * @param format      the pattern and symbols represented in CSV file.
	 * @return formatted date
	 */
	private static LocalDateTime dateFormatter(String dateTimeStr, String format) {
		if (dateTimeStr.contains(" ") && format != null) {
			String date = handleDateFormat(dateTimeStr.split(" ")[0]);
			String time = handleTimestamp(dateTimeStr.split(" ")[1]);
			final String dateTIme = String.format("%s %s", date, time);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
			return LocalDateTime.parse(dateTIme, dtf);
		}
		return LocalDateTime.now();
	}

	/**
	 * Method to handle the string of data represented in accidents.csv (3) split by
	 * year, month, day
	 * 
	 * @param dateStr represents the dates in Report
	 * @return formatted date
	 */
	private static String handleDateFormat(String dateStr) {
		String[] dateSplit = dateStr.contains("/") ? dateStr.split("/") : dateStr.split("-");
		String year = String.format("%4d", Integer.parseInt(dateSplit[0]));
		String month = String.format("%02d", Integer.parseInt(dateSplit[1]));
		String day = String.format("%02d", Integer.parseInt(dateSplit[2]));
		return String.format("%s-%s-%s", month, day, year);
	}

	/**
	 * Regular Expression that matches characters \. and replaces with empty string
	 * 
	 * @param timeStr string of data in accidents (3).csv
	 * @return replaced type
	 */
	private static String handleTimestamp(String timeStr) {
		String time = timeStr.replaceAll("\\..*", "");
		return String.format("%s", time);
	}
}
