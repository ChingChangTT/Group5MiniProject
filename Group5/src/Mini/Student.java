package Mini;

import ProcessMini.AddStudent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.Duration;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements AddStudent {
    private String id;
    private String name;
    private String dateOfBirth;
    private String classroom;
    private String subject;
    public static List<Student> students = new ArrayList<>();
    public static String inputFilePath = "output.csv";

    @Override
    public List<String> add() {
        Scanner scanner = new Scanner(System.in);
        int randomNum = new Random().nextInt(90000) + 10000;
        String id = String.valueOf(randomNum);

        System.out.println("Enter student name:");
        String name = scanner.nextLine();
        String year = "";
        boolean isValidYear = false;
        while (!isValidYear) {
            System.out.println("Enter student year of birth (YYYY):");
            year = scanner.nextLine();
            if (year.matches("\\d{4}")) {
                isValidYear = true;
            } else {
                System.out.println("Invalid year format. Please enter a 4-digit year.");
            }
        }

        String month = "";
        boolean isValidMonth = false;
        while (!isValidMonth) {
            System.out.println("Enter student month of birth (MM):");
            month = scanner.nextLine();
            if (month.matches("\\d{2}")) {
                int monthValue = Integer.parseInt(month);
                if (monthValue >= 1 && monthValue <= 12) {
                    isValidMonth = true;
                } else {
                    System.out.println("Invalid month. Please enter a value between 01 and 12.");
                }
            } else {
                System.out.println("Invalid month format. Please enter a 2-digit month.");
            }
        }

        String day = "";
        boolean isValidDay = false;
        while (!isValidDay) {
            System.out.println("Enter student day of birth (DD):");
            day = scanner.nextLine();
            if (day.matches("\\d{2}")) {
                int dayValue = Integer.parseInt(day);
                if (dayValue >= 1 && dayValue <= 31) {
                    isValidDay = true;
                } else {
                    System.out.println("Invalid day. Please enter a value between 01 and 31.");
                }
            } else {
                System.out.println("Invalid day format. Please enter a 2-digit day.");
            }
        }

        String dateOfBirth = year + "-" + month + "-" + day;

        System.out.println("Enter student classroom:");
        String classroom = scanner.nextLine();

        System.out.println("Enter student subject:");
        String subject = scanner.nextLine();
        Student student = new Student(id, name, dateOfBirth, classroom, subject);
        students.add(student);
        WriteDataToFile();
        students.clear();
        return List.of();
    }

    public void WriteDataToFile() {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFilePath, true))) {
                    String studentData = students.stream()
                            .map(s -> s.getId() + "," + s.getName() + "," + s.getDateOfBirth() + "," +
                                    s.getClassroom() + "," + s.getSubject())
                            .collect(Collectors.joining(System.lineSeparator())); // Join with line separator

                    // Write the data to the file
                    writer.write(studentData);
                    writer.newLine();
                    System.out.println("Data appended successfully to " + inputFilePath);

                } catch (IOException e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                }
    }

    @Override
    public void listStudentsAsTable() {
        readDataFromFile("output.csv");
        Table tb = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
        for (int i = 0; i < 5; i++) {
            tb.setColumnWidth(i, 25, 25);
        }
        tb.addCell("Student ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        tb.addCell("Student Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        tb.addCell("Date of birth", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        tb.addCell("Classroom", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        tb.addCell("Subject", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        for (Student student : students) {
            tb.addCell(student.getId(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell(student.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell(student.getDateOfBirth(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell(student.getClassroom(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell(student.getSubject(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        }
        System.out.println(tb.render());
    }


    public void readDataFromFile(String filePath) {
        System.out.println("Do you want to commit the data now? (yes/no)");
        String commitChoice = new Scanner(System.in).nextLine().trim();
        if (commitChoice.equalsIgnoreCase("yes")){
            try {
                Stream<String> lines = Files.lines(Paths.get(filePath));
                lines.map(line -> {
                            String[] parts = line.split(",");
                            if (parts.length >= 5) {
                                String id = parts[0];
                                String name = parts[1];
                                String dateOfBirth = parts[2];
                                String classroom = parts[3];
                                String subject = parts[4];
                                return new Student(id, name, dateOfBirth, classroom, subject);

                            } else {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .forEach(students::add);

                // Close the Stream
                lines.close();

                System.out.println("Data read successfully from " + filePath);
            } catch (IOException e) {
                System.out.println("File not found" + e.getMessage());
            }
        }
        else {
            System.out.println("No data Commited.");
        }

    }
    public void searchStudent(String searchKey) {
        boolean found = false;
        List<Student> matchingStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(searchKey) || student.getName().equalsIgnoreCase(searchKey) || student.getName().toLowerCase().startsWith(searchKey.toLowerCase())) {
                found = true;
                matchingStudents.add(student);
            }
        }
        if (found) {
            System.out.println("Search Results:");
            Table tb = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
            for (int i = 0; i < 5; i++) {
                tb.setColumnWidth(i, 25, 25);
            }
            tb.addCell("Student ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell("Student Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell("Date of birth", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell("Classroom", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell("Subject", new CellStyle(CellStyle.HorizontalAlign.CENTER));

            for (Student student : matchingStudents) {
                tb.addCell(student.getId(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                tb.addCell(student.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                tb.addCell(student.getDateOfBirth(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                tb.addCell(student.getClassroom(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                tb.addCell(student.getSubject(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            }
            System.out.println(tb.render());
        } else {
            System.out.println("No student found with the specified search criteria.");
        }
    }


    public void generateRandomStudentData(int count) {
        // Read data from the output.csv file
        readDataFromFile(inputFilePath);

        // Start time
        Instant startTime = Instant.now();

        // Generate random student data based on the existing data
        List<Student> randomStudents = generateRandomStudents(count);

        // End time
        Instant endTime = Instant.now();

        // Calculate the elapsed time in milliseconds
        long durationMillis = Duration.between(startTime, endTime).toMillis();

        // Convert milliseconds to seconds
        double durationSeconds = durationMillis / 1000.0;

        // Display the time taken to generate the random student data
        System.out.println("Time taken to generate random student data: " + durationSeconds + " seconds.");

        // Display the random student data with pagination
        displayStudentDataWithPagination(randomStudents);
    }


    private List<Student> generateRandomStudents(int count) {
        Random random = new Random();
        return students.stream()
                .map(student -> {
                    // Generate a random ID in the range of 1 to 100,000,000
                    int randomNum = random.nextInt(100000000) + 1;
                    String newId = String.valueOf(randomNum);

                    // Create a new Student object with random ID and same other attributes
                    return new Student(newId, student.getName(), student.getDateOfBirth(),
                            student.getClassroom(), student.getSubject());
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    private void displayStudentDataWithPagination(List<Student> students) {
        Scanner scanner = new Scanner(System.in);
        final int pageSize = 5;
        int pageCount = (int) Math.ceil((double) students.size() / pageSize);
        int currentPage = 1;

        while (true) {
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, students.size());

            // Display current page data
            System.out.println("Page " + currentPage + " of " + pageCount + ":");
            displayStudentData(students.subList(startIndex, endIndex));

            // Ask user for navigation input
            System.out.println("Enter 'next' to view next page, 'prev' to view previous page, or 'exit' to quit:");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("next")) {
                if (currentPage < pageCount) {
                    currentPage++;
                } else {
                    System.out.println("Already on the last page.");
                }
            } else if (input.equals("prev")) {
                if (currentPage > 1) {
                    currentPage--;
                } else {
                    System.out.println("Already on the first page.");
                }
            } else if (input.equals("exit")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'next', 'prev', or 'exit'.");
            }
        }
    }

    private void displayStudentData(List<Student> students) {
        // Create a table to display the student data
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
        for (int i = 0; i < 5; i++) {
            table.setColumnWidth(i, 25, 25);
        }
        table.addCell("Student ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Student Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Date of Birth", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Classroom", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Subject", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        // Add student data to the table
        for (Student student : students) {
            table.addCell(student.getId(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(student.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(student.getDateOfBirth(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(student.getClassroom(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(student.getSubject(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        }

        // Display the table
        System.out.println(table.render());
    }


    public static void main(String[] args) {
        Student student = new Student();
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

        if ( student.equals(!students.isEmpty())){
            student.WriteDataToFile();
        } else {
            do {
                System.out.println("\n>>>>>>>>>>>>>Check In before Choose<<<<<<<<<<<<<");
                String[] all = {" All of Menu", "1. Add Student", "2. List students", "3. Commit data", "5.Search Student",
                        "6. "};
                Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
                table.setColumnWidth(0, 100, 200);

                for (String s : all) {
                    table.addCell(s);
                }
                System.out.println(table.render());
                System.out.println("Enter your choice: ");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        student.add();
                        break;
                    case 2:
                        student.listStudentsAsTable();
                        break;
                    case 3:
                        System.out.println("Enter ID, name, or key to search:");
                        scanner.nextLine(); // Consume the newline character
                        String searchKey = scanner.nextLine();
                        student.searchStudent(searchKey);
                        break;
                    case 4:
                        System.out.println("Do you want to commit the data now? (yes/no)");
                        String commitChoice = new Scanner(System.in).nextLine().trim();
                        if (commitChoice.equalsIgnoreCase("yes")){
                            System.out.println("Enter the file path to read data from:");
                            String filePath = scanner.nextLine();
                            student.readDataFromFile(filePath);
                        } else {
                            System.out.println("No data Commited.");
                        }
                        break;
                    case 5:
                        System.out.println("Enter the number of random students to generate:");
                        int count = scanner.nextInt();
                        student.generateRandomStudentData(count);
                        break;
                    case 99:
                        System.out.println("Exiting the program...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } while (choice != 99);
        }
    }

    private void searchStudentByIdAndKeyword(String id, String keyword) {
    }
}


