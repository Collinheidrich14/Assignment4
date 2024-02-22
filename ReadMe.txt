AccidentPack is a Java program that reads accident data from a CSV file, processes it, and determines the minimum number of counters needed to handle the accidents efficiently. The program utilizes a queue-based simulation to manage the processing of accidents based on severity.

Clone the repository to your local machine.
Ensure you have Java installed.
Compile the program using the Java compiler (javac Program4.java).
Run the compiled program (java Program4).
Usage
AccidentPack reads accident data from a CSV file named "accidents.csv" and simulates processing for specific counties and states. The program then prints the minimum number of counters needed for each location.

java
Copy code
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

Task Division:
Collin did task 1, 2, and part of 3. I got help from Jesse on writing the main code of task3
Jesse did the meat of task 3 with me over a zoom, documentation, pdf, and time calculations, and submission

Contributors
Collin Heidrich - team manager
Jesse Cyrbrophy - Coder, Documentator, debugger


Version: February 15, 2021
