package student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Enhanced Student Management System
 * @author phang
 * @version 2.0
 */
public class Student implements Comparable<Student> {
    // Constants
    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 100;
    private static final int PASSING_SCORE = 50;
    
    // Static counter for total students
    private static int totalStudents = 0;
    
    // Instance variables
    private final int id;
    private String name;
    private int testScore;
    private String grade;
    private boolean passed;

    /**
     * Constructor with parameter validation
     */
    public Student(int id, String name, int testScore) {
        validateId(id);
        validateName(name);
        validateTestScore(testScore);
        
        this.id = id;
        this.name = name;
        this.testScore = testScore;
        this.grade = calculateGrade();
        this.passed = determinePassingStatus();
        totalStudents++;
    }

    // Getters and Setters with validation
    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public void setTestScore(int testScore) {
        validateTestScore(testScore);
        this.testScore = testScore;
        this.grade = calculateGrade();
        this.passed = determinePassingStatus();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getTestScore() { return testScore; }
    public boolean isPassed() { return passed; }
    public String getGrade() { return grade; }
    public static int getTotalStudents() { return totalStudents; }

    /**
     * Main method to run the student management system
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        try {
            // Get number of students
            System.out.print("Enter the number of students: ");
            int numStudents = getValidIntInput(scanner, 1, 100);

            // Input data for each student
            for (int i = 0; i < numStudents; i++) {
                System.out.println("\nEnter details for student " + (i + 1) + ":");
                students.add(createStudent(scanner));
            }

            // Display menu and process user choices
            boolean running = true;
            while (running) {
                running = displayMenuAndProcessChoice(scanner, students);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    /**
     * Displays menu and processes user choice
     */
    private static boolean displayMenuAndProcessChoice(Scanner scanner, ArrayList<Student> students) {
        System.out.println("\nStudent Management System Menu:");
        System.out.println("1. Display all students");
        System.out.println("2. Search for a student");
        System.out.println("3. Sort by ID");
        System.out.println("4. Sort by name");
        System.out.println("5. Sort by test score");
        System.out.println("6. Show statistics");
        System.out.println("7. Exit");
        
        int choice = getValidIntInput(scanner, 1, 7);
        
        switch (choice) {
            case 1 -> displayStudents(students);
            case 2 -> searchStudent(scanner, students);
            case 3 -> {
                Collections.sort(students);
                displayStudents(students);
            }
            case 4 -> {
                students.sort(Comparator.comparing(Student::getName));
                displayStudents(students);
            }
            case 5 -> {
                students.sort(Comparator.comparing(Student::getTestScore).reversed());
                displayStudents(students);
            }
            case 6 -> displayStatistics(students);
            case 7 -> {
                System.out.println("Goodbye!");
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a new student with user input
     */
    private static Student createStudent(Scanner scanner) {
        System.out.print("Student ID: ");
        int id = getValidIntInput(scanner, 1, Integer.MAX_VALUE);
        
        scanner.nextLine(); // Consume newline
        System.out.print("Student Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Test Score: ");
        int testScore = getValidIntInput(scanner, MIN_SCORE, MAX_SCORE);
        
        return new Student(id, name, testScore);
    }

    /**
     * Validation methods
     */
    private static void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    private static void validateTestScore(int testScore) {
        if (testScore < MIN_SCORE || testScore > MAX_SCORE) {
            throw new IllegalArgumentException(
                    String.format("Test score must be between %d and %d", MIN_SCORE, MAX_SCORE));
        }
    }

    /**
     * Calculate grade based on test score
     */
    private String calculateGrade() {
        if (testScore >= 80) return "A";
        if (testScore >= 70) return "B";
        if (testScore >= 60) return "C";
        if (testScore >= 50) return "D";
        return "F";
    }

    private boolean determinePassingStatus() {
        return testScore >= PASSING_SCORE;
    }

    /**
     * Utility methods for display and statistics
     */
    private static void displayStudents(ArrayList<Student> students) {
        System.out.println("\nStudent List:");
        System.out.println("--------------------");
        for (Student student : students) {
            System.out.printf("ID: %d%nName: %s%nScore: %d%nGrade: %s%nStatus: %s%n%n",
                    student.getId(),
                    student.getName(),
                    student.getTestScore(),
                    student.getGrade(),
                    student.isPassed() ? "Passed" : "Failed");
        }
    }

    private static void displayStatistics(ArrayList<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students to analyze.");
            return;
        }

        double average = students.stream()
                .mapToInt(Student::getTestScore)
                .average()
                .orElse(0.0);

        long passCount = students.stream()
                .filter(Student::isPassed)
                .count();

        System.out.println("\nClass Statistics:");
        System.out.printf("Total Students: %d%n", students.size());
        System.out.printf("Average Score: %.2f%n", average);
        System.out.printf("Pass Rate: %.2f%%%n", (passCount * 100.0) / students.size());
    }

    private static void searchStudent(Scanner scanner, ArrayList<Student> students) {
        System.out.print("Enter student ID to search: ");
        int searchId = getValidIntInput(scanner, 1, Integer.MAX_VALUE);
        
        students.stream()
                .filter(s -> s.getId() == searchId)
                .findFirst()
                .ifPresentOrElse(
                        s -> System.out.println("\nFound student:\n" + s),
                        () -> System.out.println("Student not found."));
    }

    private static int getValidIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.printf("Please enter a number between %d and %d: ", min, max);
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %d%nName: %s%nScore: %d%nGrade: %s%nStatus: %s",
                id, name, testScore, grade, passed ? "Passed" : "Failed");
    }

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.id, other.id);
    }
}
