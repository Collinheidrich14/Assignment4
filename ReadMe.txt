AccidentPack1 is a Java program that reads accident data from a CSV file, processes it, and determines the minimum number of counters needed to handle the accidents efficiently. The program utilizes a queue-based simulation to manage the processing of accidents based on severity.

Clone the repository to your local machine.
Ensure you have Java installed.
Compile the program using the Java compiler (javac Program4.java).
Run the compiled program (java Program4).
Usage
AccidentPack1 reads accident data from a CSV file named "accidents.csv" and simulates processing for specific counties and states. The program then prints the minimum number of counters needed for each location.

java
Copy code
public static void main(String[] args) {
    ArrayList<Accident> accidents = readCSV("accidents.csv");

    simulateAndPrintCounters("Los Angeles", "CA", accidents);
    simulateAndPrintCounters("Orange", "FL", accidents);
    simulateAndPrintCounters("Harris", "TX", accidents);
    simulateAndPrintCounters("Hamilton", "OH", accidents);
    simulateAndPrintCounters("New Castle", "DE", accidents);
}

Task Division:
Collin did task 1, 2, and part of 3. I got help from Jesse on writing the main code of task3
Jesse did the meat of task 3 with me over a zoom, documentation, pdf, and time calculations, and submission

Contributors
Collin Heidrich - team manager
Jesse Cyrbrophy - Coder, Documentator, debugger


Version: February 15, 2021