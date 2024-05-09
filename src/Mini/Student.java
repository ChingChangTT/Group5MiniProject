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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
}


