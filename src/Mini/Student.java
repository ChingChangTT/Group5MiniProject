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
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
        List<String> classrooms = Arrays.asList(classroom.split("/"));
        System.out.println("Enter student subject:");
        String subject = scanner.nextLine();
        List<String> subjects = Arrays.asList(subject.split("/"));
        Student student = new Student(id, name, dateOfBirth, String.join(",",classrooms)
                 , String.join(",",subjects));
        students.add(student);
        WriteDataToFile();
        students.clear();
        return List.of();
    }


    public void WriteDataToFile() {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(inputFilePath))) {
            StringBuilder studentData = new StringBuilder();
            for (Student s : students) {
                studentData.append(s.getId()).append("/").append(s.getName()).append("/").append(s.getDateOfBirth()).append("/")
                        .append(s.getClassroom()).append("/").append(s.getSubject()).append(System.lineSeparator());
            }
            bos.write(studentData.toString().getBytes());
            System.out.println("Data written successfully to " + inputFilePath);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }


    @Override
    public void listStudentsAsTable(String file) {
        Instant  beforeReadData = Instant .now();

//        readDataFromFile("output.csv");
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
        if (Objects.equals(file, "output.csv")){
            System.out.print("Time used to read data: " + ((afterReadDate.toEpochMilli() - beforeReadData.toEpochMilli()) * 0.001) + "s");

        }
        else {
            System.out.print("Time used to read 1000 000 record: " + ((afterReadDate.toEpochMilli() - beforeReadData.toEpochMilli()) * 0.001) + "s");

        }

    }

    public void commitChangesToFile() {
        WriteDataToFile();
        System.out.println("Changes committed successfully to file.");
    }
    public void listDataStudent(List<Student> students,int pageNumber, int recordsPerPage){
        int startIndex = (pageNumber - 1) * recordsPerPage;
        int endIndex = Math.min(startIndex + recordsPerPage, students.size());


        readDataFromFile("output.csv");
        listStudentsAsTable("output.csv");
    }
    public void readDataFromFile(String filePath) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            StringBuilder content = new StringBuilder();
            byte[] buffer = new byte[1024]; // You can adjust buffer size according to your needs
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                content.append(new String(buffer, 0, bytesRead));
            }
            String[] lines = content.toString().split(System.lineSeparator());
            students = Arrays.stream(lines)
                    .map(line -> {
                        String[] parts = line.split("/");
                        if (parts.length >= 5) {
                            String id = parts[0];
                            String name = parts[1];
                            String dateOfBirth = parts[2];
                            String classrooms = parts[3];
                            String subjectParts=parts[4];

                            return new Student(id, name, dateOfBirth, classrooms.toString(), subjectParts.toString());
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            System.out.println("Data read successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }
    private void displayStudentDataWithPagination(List<Student> students) {
        Scanner scanner = new Scanner(System.in);
        final int pageSize = 5;
        int pageCount = (int) Math.ceil((double) students.size() / pageSize);
        int currentPage = 1;

        label:
        while (true) {
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, students.size());

            // Display current page data
            System.out.println("Page " + currentPage + " of " + pageCount + ":");
            listDataStudent(students.subList(startIndex, endIndex),5,5);

            // Ask user for navigation input
            System.out.println("\nEnter 'next' to view next page, 'prev' to view previous page, or 'exit' to quit:");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "next":
                    if (currentPage < pageCount) {
                        currentPage++;
                    } else {
                        System.out.println("Already on the last page.");
                    }
                    break;
                case "prev":
                    if (currentPage > 1) {
                        currentPage--;
                    } else {
                        System.out.println("Already on the first page.");
                    }
                    break;
                case "exit":
                    break label;
                default:
                    System.out.println("Invalid input. Please enter 'next', 'prev', or 'exit'.");
                    break;
            }
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

        // Search for the student in the list
        Optional<Student> optionalStudent = students.stream()
                .filter(student -> student.getId().equalsIgnoreCase(studentId))
                .findFirst();

        if (optionalStudent.isPresent()) {
            Student studentToUpdate = optionalStudent.get();

            // Display current student details
            System.out.println("Current details of the student:");
            displayStudentDetails(studentToUpdate);

            // Prompt user for the field to update
            System.out.println("Which field do you want to update? (name/date/class/subject)");
            String fieldToUpdate = scanner.nextLine().trim().toLowerCase();

            // Update the chosen field
            switch (fieldToUpdate) {
                case "name":
                    System.out.println("Enter new name:");
                    String newName = scanner.nextLine().trim();
                    studentToUpdate.setName(newName);
                    break;
                case "date":
                    System.out.println("Enter new date of birth (YYYY-MM-DD):");
                    String newDateOfBirth = scanner.nextLine().trim();
                    studentToUpdate.setDateOfBirth(newDateOfBirth);
                    break;
                case "class":
                    System.out.println("Enter new classroom:");
                    String newClassroom = scanner.nextLine().trim();
                    studentToUpdate.setClassroom(newClassroom);
                    break;
                case "subject":
                    System.out.println("Enter new subject:");
                    String newSubject = scanner.nextLine().trim();
                    studentToUpdate.setSubject(newSubject);
                    break;
                default:
                    System.out.println("Invalid field specified. No changes made.");
                    break;
            }

            // Commit Changes
            System.out.println("Do you want to commit changes? (yes/no)");
            String answer = scanner.nextLine().trim().toLowerCase();

            switch (answer) {
                case "yes":
                    // Write updated data to file
                    commitChangesToFile();
                    System.out.println("Changes committed successfully.");
                    break;
                case "no":
                    System.out.println("Changes discarded.");
                    break;
                default:
                    System.out.println("Invalid input. Changes discarded.");
                    break;
            }
        } else {
            System.out.println("Student with ID " + studentId + " not found.");
        }
    }


    public void commitOrNot() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to commit changes? (yes/no)");
        String answer = scanner.nextLine().trim().toLowerCase();

        switch (answer) {
            case "yes":
                commitChangesToFile();
                break;
            case "no":
                System.out.println("Changes discarded.");
                break;
            default:
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                commitOrNot(); // Ask again until valid response is given
                break;
        }
    }

    @Override
    public void deleteDataById(String filePath) {
        commitChangesToFile();
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

    public void generateData() throws IOException {
        String file = "DummyFile.csv";
        Path filePath = Paths.get(file);
        if (Files.exists(filePath) && Files.size(filePath) > 0) { // Check if the file exists and is not empty
            readDataFromFile(file);
            listStudentsAsTable("DummyFile.csv");

        } else {
            try (BufferedWriter ignored = new BufferedWriter(new FileWriter(file))) {
                for(int i = 0; i < 1000000; i++) {
                    int randomNum = new Random().nextInt(90000) + 10000;
                    String id = String.valueOf(randomNum);
                    String name = "student" + i; // Initialize local variables
                    String dateOfBirth = "2024-05-11";
                    String classroom = "Morning";
                    String subject = "Java";
                    Student student = new Student(id, name, dateOfBirth, classroom, subject);
                    students.add(student);
                    WriteDataToFile(); // Assuming WriteDataToFile writes the students to a file
                    System.out.println("order : " + i);
                }
                System.out.println("Finished Insert Batch of Students!!");
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
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
        String filePath = "output.csv";
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
        do {
            System.out.println("\n>>>>>>>>>>>>>Check In before Choose<<<<<<<<<<<<<");
            System.out.println(repeat("=", 1000));
            System.out.println("""
                        1.Add Student           2.List All Student              3.CommitData to File\

                        4.Search for Student    5.Update Student by Info By ID  6.Delete Student Data\

                        7.Generate Data to File 8.Delete/Clear Data Store From Data Store
                        0,99.Exit""");
            System.out.println(repeat("=", 1000));
//            student.displayStudentDataWithPagination(students);
            System.out.println(repeat("=", 1000));
            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 0, 99:
                    System.out.println("Exiting the program...");
                    break;
                case 1:
                    student.add();
                    break;
                case 2:
                    student.listDataStudent(students,5,5);
                    break;
                case 3:
                    student.commitOrNot();
                    break;
                case 4:
                    student.searchStudent();
                    break;
                case 5:
                    student.updateDataInFile(inputFilePath);
                    break;
                case 6:
                    student.deleteDataById(inputFilePath);
                case 7:
                    student.generateData();
                    break;
                case 8:
                    student.clearDataStore();
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while ((choice != 99)&&(choice!=0));
    }
}


