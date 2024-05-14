package MainOutput;
import ProcessOfcode.StudentManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.InputMismatchException;
import java.util.Scanner;

import static FeildPackage.Student.*;

public class MainApplication {
    public static void main(String[] args) throws IOException {
        StudentManagement student = new StudentManagement();
        Scanner scanner = new Scanner(System.in);
        int choice;
        System.out.println("""
                \s
                  ██╗    ██╗███████╗██╗     ██╗      ██████╗ ██████╗ ███╗   ███╗    ████████╗ ██████╗      ██████╗███████╗████████╗ █████╗ ██████╗\s
                  ██║    ██║██╔════╝██║     ██║     ██╔════╝██╔═══██╗████╗ ████║    ╚══██╔══╝██╔═══██╗    ██╔════╝██╔════╝╚══██╔══╝██╔══██╗██╔══██╗
                  ██║ █╗ ██║█████╗  ██║     ██║     ██║     ██║   ██║██╔████╔██║       ██║   ██║   ██║    ██║     ███████╗   ██║   ███████║██║  ██║
                  ██║███╗██║██╔══╝  ██║     ██║     ██║     ██║   ██║██║╚██╔╝██║       ██║   ██║   ██║    ██║     ╚════██║   ██║   ██╔══██║██║  ██║
                  ╚███╔███╔╝███████╗███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║       ██║   ╚██████╔╝    ╚██████╗███████║   ██║   ██║  ██║██████╔╝
                   ╚══╝╚══╝ ╚══════╝╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝       ╚═╝    ╚═════╝      ╚═════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═════╝                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              \s

                """);
        String filePath = "Temp.csv";
        // Capture the start time
        Instant beforeReadData = Instant.now();
        try {
            Files.lines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
        // Capture the end time
        Instant afterReadData = Instant.now();
        long durationInSeconds = Duration.between(beforeReadData, afterReadData).getSeconds();

        System.out.println("[*] Test speed from record of " + filePath + ": " + durationInSeconds + " seconds");
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (reader.readLine() != null) {
                count++;
            }
            System.out.println("Number of records in file: " + count);
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        student.displayCurrentPage();
        do {
            student.commitOrNot();
            System.out.println("\n>>>>>>>>>>>>>Check In before Choose<<<<<<<<<<<<<");
            System.out.println(student.repeat("=", 1000));
            System.out.println("""
                        1.Add Student           2.List All Student              3.CommitData to File\

                        4.Search for Student    5.Update Student by Info By ID  6.Delete Student Data\

                        7.Generate Data to File 8.Delete/Clear Data Store From Data Store
                        0,99.Exit""");
            System.out.println(student.repeat("=", 1000));
            System.out.println("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 0, 99:
                        System.out.println("Exiting the program...");
                        break;
                    case 1:
                        student.add();
                        break;
                    case 2:
                        student.listDataStudent();
                        System.out.println(student.repeat("=", 1000));
                        System.out.print("[+]Page number : 1");
                        System.out.print("\t\t[+]Actual Record :" + (count - 1));
                        System.out.print("\t\t[+] All record:" + count);
                        System.out.println("\t\t\t\t\t\t[+]Previous(prev)\t-Next(next)\t-Back(B)");
                        System.out.println(student.repeat("=", 1000));
                        break;
                    case 3:
                        student.commitOrNot();
                        break;
                    case 4:
                        student.searchStudent(students, scanner);
                        break;
                    case 5:
                        student.updateDataInFile(inputFilePath);
                        break;
                    case 6:
                        student.deleteDataById(inputFilePath);
                        break;
                    case 7:
                        student.generateData();
                        break;
                    case 8:
                        student.clearDataStore();
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number corresponding to the menu options.");
                scanner.nextLine(); // Consume the invalid input
                choice = -1; // Set choice to invalid value to continue the loop
            }
        } while ((choice != 99)&&(choice!=0));
    }
}
