package accidentpack1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
/**
 * This java class reads the accident csv file and puts the data into an arraylist. The arraylist is then processed through a queue to count how many counters are needed. 
*  Some sources I used where https://www.youtube.com/watch?v=91CMnJeHJVc, https://www.youtube.com/watch?v=-Aud0cDh-J8, https://www.youtube.com/watch?v=70qy6_gw1Hc, https://www.youtube.com/watch?v=VX9CwPn-BBE and using chatgpt for basic things to save time.
* @author Collin Heidrich, Jesse Cyrbrophy
* @version Febuary 15 2021
*/
class Accident {
    String ID;
    int Severity;
    Date StartTime;
    Date EndTime;
    String Street;
    String City;
    String County;
    String State;
    int Temperature;
    int Humidity;
    int Visibility;
    String WeatherCondition;
    boolean Crossing;
    String SunriseSunset;
    
    /**
     * Constructor for creating an Accident object from datalist.
     *
     * @param data Array containing accident data.
     * @throws ParseException If there is an error parsing date from the data.
     */
    public Accident(String[] data) throws ParseException {
        this.ID = data[0];
        this.Severity = Integer.parseInt(data[1]);
        this.StartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data[2]);
        this.EndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data[3]);
        this.Street = data[4];
        this.City = data[5];
        this.County = data[6];
        this.State = data[7];
        this.Temperature = (int) Double.parseDouble(data[8]);
        this.Humidity = (int) Double.parseDouble(data[9]);
        this.Visibility = (int) Double.parseDouble(data[10]);
        this.WeatherCondition = data[11];
        this.Crossing = Boolean.parseBoolean(data[12]);
        this.SunriseSunset = data[13];
    }
    /**
     * Getter method for retrieving the start time
     * @return The start time of the accident.
     */
    public Date getStartTime() {
        return StartTime;
    }
}

class Counter {
    int remainingTime;

    public Counter(int dailyWorkMinutes) {
        this.remainingTime = dailyWorkMinutes;
    }
    	/**
    	* Processes an accident based on severity and adds remaining time.
    	* @param severity Severity level of the accident.
    	* @return Processing time if successful
    	*/
    public int processAccident(int severity) {
        int processingTime = getProcessingTime(severity);
        if (remainingTime >= processingTime) {
            remainingTime -= processingTime;
            return processingTime;
        } else {
            return -1;
        }
    }

    private int getProcessingTime(int severity) {
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
}
/**
 * Main class containing the program logic for reading CSV, simulating processing, and printing results.
 */
public class Program4 {
    /**
     * Main method
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        ArrayList<Accident> accidents = readCSV("accidents.csv");

        simulateAndPrintCounters("Los Angeles", "CA", accidents);
        simulateAndPrintCounters("Orange", "FL", accidents);
        simulateAndPrintCounters("Harris", "TX", accidents);
        simulateAndPrintCounters("Hamilton", "OH", accidents);
        simulateAndPrintCounters("New Castle", "DE", accidents);
    }
    /**
     * Simulates processing for a specific county and state, then prints the minimum counters needed.
     * @param county    The county to simulate processing for.
     * @param state     The state to simulate processing for.
     * @param accidents List of accidents to process.
     */
    public static void simulateAndPrintCounters(String county, String state, ArrayList<Accident> accidents) {
        System.out.println("County: " + county + ", State: " + state);
        int dailyWorkMinutes = 5 * 24 * 60;
        int countersNeeded = simulateProcessing(county, state, accidents, dailyWorkMinutes);
        System.out.println("Minimum counters needed: " + countersNeeded);
        System.out.println("--------------");
    }
    /**
     * Simulates accident processing for a specified county and state.
     * @param county          The county to simulate processing for.
     * @param state           The state to simulate processing for.
     * @param accidents       List of accidents to process.
     * @param dailyWorkMinutes The daily work minutes available for processing.
     * @return The minimum number of counters needed.
     */
    public static int simulateProcessing(String county, String state, ArrayList<Accident> accidents, int dailyWorkMinutes) {
        Collections.sort(accidents, Comparator.comparing(Accident::getStartTime));

        Queue<Accident> queue = new LinkedList<>();
        int countersNeeded = 0;

        for (Accident accident : accidents) {
            if (accident.County.equals(county) && accident.State.equals(state)) {
                queue.add(accident);
            }
        }

        Counter counter = new Counter(dailyWorkMinutes);

        while (!queue.isEmpty()) {
            Accident currentAccident = queue.poll();
            long waitingTime = calculateWaitingTime(currentAccident.StartTime);

            if (waitingTime > 24 * 60) {
                // The accident report waited more than a day, not enough counters
                countersNeeded++;
                counter = new Counter(dailyWorkMinutes);
            }

            int processingTime = counter.processAccident(currentAccident.Severity);

            if (processingTime == -1) {
                // Not enough time to process the accident on the same day
                countersNeeded++;
                counter = new Counter(dailyWorkMinutes);
            }
        }

        return countersNeeded;
    }

    /**
     * Calculates waiting time in minutes based on the difference between current time and accident start time.
     * @param startTime The start time of the accident.
     * @return The waiting time in minutes.
     */
    public static long calculateWaitingTime(Date startTime) {
        long currentTime = System.currentTimeMillis();
        long accidentTime = startTime.getTime();
        return (currentTime - accidentTime) / (60 * 1000); // Convert milliseconds to minutes
    }

    public static ArrayList<Accident> readCSV(String filename) {
        ArrayList<Accident> accidents = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                accidents.add(new Accident(data));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return accidents;
    }
}
