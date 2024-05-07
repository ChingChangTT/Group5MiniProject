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
import java.nio.file.StandardOpenOption;
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

    @Override
    public  List<String> add() {
        String inputFilePath = "output.csv";
        Scanner scanner = new Scanner(System.in);
        int randomNum = new Random().nextInt(90000) + 10000;
        String id = String.valueOf(randomNum);

        System.out.println("Enter student name:");
        String name = scanner.nextLine();
        String dateOfBirth = " ";
        boolean isValidDate = false;
        while (!isValidDate) {
            System.out.println("Enter student date of birth (YYYY-MM-DD):");
            String dobInput = scanner.nextLine();
            String[] parts = dobInput.split("-");
            if (parts.length == 3 && parts[0].length() == 4 && parts[1].length() == 2 && parts[2].length() == 2) {
                dateOfBirth = dobInput;
                isValidDate = true;
            } else {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }

        System.out.println("Enter student classroom:");
        String classroom = scanner.nextLine();

        System.out.println("Enter student subject:");
        String subject = scanner.nextLine();

        Student student = new Student(id, name, dateOfBirth, classroom, subject);
        students.add(student); // Add student to the list
        System.out.println("Student added successfully!");
        try (Stream<String> lines = students.stream().map(students ->
                student.getId() + "," + student.getName() + "," + student.getDateOfBirth() + "," +
                        student.getClassroom() + "," + student.getSubject())) {
            Files.write(Paths.get(inputFilePath), lines.collect(Collectors.toList()), StandardOpenOption.APPEND);
            System.out.println("Data written successfully to " + inputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public  void listStudentsAsTable() {
        readDataFromFile("output.csv");
        Table tb = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
        for(int i=0; i<5; i++){
            tb.setColumnWidth(i,25,25);
        }
        tb.addCell("Student ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        tb.addCell("Student Name",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        tb.addCell("Date of birth",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        tb.addCell("Classroom",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        tb.addCell("Subject",new CellStyle(CellStyle.HorizontalAlign.CENTER));

        for (Student student : students) {
            tb.addCell(student.getId(),new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell(student.getName(),new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell(student.getDateOfBirth(),new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell(student.getClassroom(),new CellStyle(CellStyle.HorizontalAlign.CENTER));
            tb.addCell(student.getSubject(),new CellStyle(CellStyle.HorizontalAlign.CENTER));
        }
        System.out.println(tb.render());
    }
    public void readDataFromFile(String filePath) {
        students.clear();
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
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Student student=new Student();
        Scanner scanner = new Scanner(System.in);
        int choice;
        System.out.print("Insert Menu : ");
        String menu=scanner.nextLine();
        System.out.println("""
                \s

                  ██╗    ██╗███████╗██╗     ██╗      ██████╗ ██████╗ ███╗   ███╗    ████████╗ ██████╗      ██████╗███████╗████████╗ █████╗ ██████╗\s
                  ██║    ██║██╔════╝██║     ██║     ██╔════╝██╔═══██╗████╗ ████║    ╚══██╔══╝██╔═══██╗    ██╔════╝██╔════╝╚══██╔══╝██╔══██╗██╔══██╗
                  ██║ █╗ ██║█████╗  ██║     ██║     ██║     ██║   ██║██╔████╔██║       ██║   ██║   ██║    ██║     ███████╗   ██║   ███████║██║  ██║
                  ██║███╗██║██╔══╝  ██║     ██║     ██║     ██║   ██║██║╚██╔╝██║       ██║   ██║   ██║    ██║     ╚════██║   ██║   ██╔══██║██║  ██║
                  ╚███╔███╔╝███████╗███████╗███████╗╚██████╗╚██████╔╝██║ ╚═╝ ██║       ██║   ╚██████╔╝    ╚██████╗███████║   ██║   ██║  ██║██████╔╝
                   ╚══╝╚══╝ ╚══════╝╚══════╝╚══════╝ ╚═════╝ ╚═════╝ ╚═╝     ╚═╝       ╚═╝    ╚═════╝      ╚═════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═════╝                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              \s

                """);
        if(menu.equals("yes")||menu.equals("y")){

            do {
                System.out.println("\n>>>>>>>>>>>>>Check In before Choose<<<<<<<<<<<<<");
                String[] all={" All of Menu","1. Add Student","2. List students","3. Commit data","5.Search Student",
                        "6. " };
                Table table=new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
                table.setColumnWidth(0,100,200);
                for (String s : all) {
                    table.addCell(s);
                }
                System.out.println(table.render());
                System.out.println("Enter your choice: ");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        student.add();
                        System.out.println("Student added. Do you want to commit the data now? (yes/no)");
                        String commitChoice = scanner.nextLine();
                        if (commitChoice.equalsIgnoreCase("yes")) {
                            Files.write(Paths.get("output.csv"), "yes".getBytes());
                        } else {
                            System.out.println("Data not committed.");
                        }
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
        else {
            System.out.println("Nothing Show.");
        }
    }
}

