/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package student;

import java.util.Scanner;

/**
 *
 * @author phang
 */
public class Student {

  // Setters for name and test score (not needed for ID)
  public void setName(String name) {
    this.name = name;
  }

  public void setTestScore(int testScore) {
    validateTestScore(testScore);
    this.testScore = testScore;
    passed = determinePassingStatus();
  }

  // Getters for all attributes
  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getTestScore() {
    return testScore;
  }

  public boolean isPassed() {
    return passed;
  }

  public static void main(String[] args) {
    // User interaction variables
    Scanner scanner = new Scanner(System.in);
    int numStudents;

    // Get user input for number of students
    System.out.print("Enter the number of students: ");
    numStudents = scanner.nextInt();

    // Create an array of Student objects
    Student[] students = new Student[numStudents];

    // Input data for each student
    for (int i = 0; i < numStudents; i++) {
      System.out.println("\nEnter details for student " + (i + 1) + ":");
      students[i] = createStudent(scanner);
    }

    // Display student information
    System.out.println("\nSorted list of students:");
    for (Student student : students) {
      System.out.println("Student ID: " + student.getId());
      System.out.println("Student Name: " + student.getName());
      System.out.println("Test Score: " + student.getTestScore());
      System.out.println("Passing Status: " + (student.isPassed() ? "True" : "False"));
      System.out.println("--------------------");
    }

    scanner.close();
  }

  // Method to create a Student object with user input
  public static Student createStudent(Scanner scanner) {
    System.out.print("Student ID: ");
    int id = scanner.nextInt();
    scanner.nextLine(); // Consume the newline character after int input

    System.out.print("Student Name: ");
    String name = scanner.nextLine();

    System.out.print("Test Score: ");
    int testScore = scanner.nextInt();
    validateTestScore(testScore); // Call the validation method

    return new Student(id, name, testScore);
  }

  // Function to validate test score, any number below 0 or over 100 will print an error 
  private static void validateTestScore(int testScore) {
    if (testScore < 0 || testScore > 100) {
      throw new IllegalArgumentException("Test score must be between 0 and 100");
    }
  }

  private static int totalStudents = 0;

  private final int id;
  private String name;
  private int testScore;
  private boolean passed;

  // Constructor with parameter validation 
  public Student(int id, String name, int testScore) {
    // Validation logic is handled by the separate validateTestScore method
    this.id = id;
    this.name = name;
    this.testScore = testScore;
    passed = determinePassingStatus();
    totalStudents++;
  }

  // Method to determine passing status based on a minimum passing score
  private boolean determinePassingStatus() {
    int minPassingScore = 50;
    return testScore >= minPassingScore;
  }
}