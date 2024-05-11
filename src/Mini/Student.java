package Mini;

import ProcessMini.AddStudent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFilePath))) {
            String studentData = students.stream()
                    .map(s -> s.getId() + "," + s.getName() + "," + s.getDateOfBirth() + "," +
                            s.getClassroom() + "," + s.getSubject())
                    .collect(Collectors.joining(System.lineSeparator())); // Join with line separator

            // Write the data to the file
            writer.write(studentData);
            writer.newLine();
            //System.out.println("Data appended successfully to " + inputFilePath);

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public void listStudentsAsTable() {

        Instant  beforeReadData = Instant .now();

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

        Instant afterReadDate = Instant.now();

        System.out.println("Time used to read data  : " + (afterReadDate.toEpochMilli() -beforeReadData.toEpochMilli()  )*0.001 + "s" );

    }

    public void readDataFromFile(String filePath) {
        try {
            // tract start reading time

            Stream<String> lines = Files.lines(Paths.get(filePath));
            List<Student> studentList = lines.map(line -> {
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
                    .collect(Collectors.toList());
            students.clear();
            students.addAll(studentList);
            // Close the Stream
            lines.close();

            System.out.println("Data read successfully from " + filePath);
            //tract time after read dataf
            /*try{
                Thread.sleep(2000);
            }catch (Exception e){}*/

       } catch (IOException e) {
            System.out.println("File not found" + e.getMessage());
        }
    }
    @Override
    public void searchStudent() {
        System.out.println("Enter the ID or name of the student to search:");
        String searchTerm = new Scanner(System.in).nextLine().trim();
        boolean found = false;
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(searchTerm) || student.getName().equalsIgnoreCase(searchTerm)) {
                System.out.println("Student found:");
                displayStudentDetails(student); // Display the found student details
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Student not found.");
        }
    }
    private void displayStudentDetails(Student student) {
        System.out.println("ID: " + student.getId());
        System.out.println("Name: " + student.getName());
        System.out.println("Date of Birth: " + student.getDateOfBirth());
        System.out.println("Classroom: " + student.getClassroom());
        System.out.println("Subject: " + student.getSubject());
    }
    @Override
    public void updateDataInFile(String filePath) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the student you want to update:");
        String studentId = scanner.nextLine().trim();

        try {
            Path path = Paths.get(filePath);
            List<String> fileContent = new ArrayList<>(Files.readAllLines(path));
            int rowToUpdate = -1;

            // Search for the row containing the student ID
            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                if (line.startsWith(studentId + ",")) {
                    rowToUpdate = i;
                    break;
                }
            }

            if (rowToUpdate != -1) {
                System.out.println("Enter the new data cut it by use this sign , :");
                String newData = scanner.nextLine().trim();

                fileContent.set(rowToUpdate, newData); // Update the data at the specified row
                Files.write(path, fileContent);
                System.out.println("Data updated successfully for student with ID: " + studentId);
            } else {
                System.out.println("Student with ID " + studentId + " not found.");
            }
        } catch (IOException e) {
            System.out.println("Error updating data in file: " + e.getMessage());
        }
    }
    @Override
    public void deleteDataById(String filePath) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the student you want to delete:");
        String studentId = scanner.nextLine().trim();

        try {
            Path path1 = Paths.get(filePath);
            List<String> fileContent = new ArrayList<>(Files.readAllLines(path1));
            boolean found = false;

            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(studentId)) {
                    fileContent.remove(i);
                    found = true;
                    break;
                }
            }

            if (found) {
                Files.write(path1, fileContent);
                System.out.println("Student data with ID " + studentId + " deleted successfully.");
            } else {
                System.out.println("Student with ID " + studentId + " not found.");
            }
        } catch (IOException e) {
            System.out.println("Error deleting data from file: " + e.getMessage());
        }
    }


    public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
    public void GenericData(){

    }
    public void clearDataStore() {
        students.clear();
        try {
            Path filePath = Paths.get(inputFilePath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Data store cleared successfully. Data file has been deleted.");
            } else {
                System.out.println("Data file does not exist. Nothing to delete.");
            }
        } catch (NoSuchFileException e) {
            System.out.println("File does not exist: " + e.getMessage());
        } catch (DirectoryNotEmptyException e) {
            System.out.println("Directory is not empty: " + e.getMessage());
        } catch (IOException | SecurityException e) {
            System.out.println("Error deleting data file: " + e.getMessage());
        }
    }

    public void InsertDummyData(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(inputFilePath))){

            for(int i = 0 ; i< 1000000 ; i++){
                int randomNum = new Random().nextInt(90000) + 10000;
                String id = String.valueOf(randomNum);
                name = "student" + i;
                dateOfBirth = "2024-05-11";
                classroom="Morning";
                subject="Java";
                Student student = new Student(id, name, dateOfBirth, classroom, subject);
                students.add(student);
               WriteDataToFile();
                System.out.println("order : " + i);
                //students.clear();
            }
            System.out.println("Finished Insert Batch of Students!!");
        }catch(Exception e){

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


            do {
                System.out.println("\n>>>>>>>>>>>>>Check In before Choose<<<<<<<<<<<<<");
                System.out.println(repeat("=", 1000));
                System.out.println("""
                        1.Add Student           2.List All Student              3.CommitData to File\

                        4.Search for Student    5.Update Student by Info By ID  6.Delete Student Data\

                        7.Generate Data to File 8.Delet/Clear Data Store From Data Store\
                        9. Insert Dummy Data
                        0,99.Exit""");
                System.out.println(repeat("=", 1000));
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
                            student.searchStudent();
                        break;
                    case 4:
                        student.updateDataInFile(inputFilePath);
                        break;
                    case 5:
                        student.deleteDataById(inputFilePath);
                    case 6:
                        student.GenericData();
                        break;
                    case 7:
                        student.clearDataStore();
                        break;
                    case 9:
                        student.InsertDummyData();
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


