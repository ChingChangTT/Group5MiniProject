package Mini;

import ProcessMini.AddStudent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
    public static String outputFilePath = "Temp.csv";

    @Override
    public List<String> add() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int randomNum = new Random().nextInt(90000) + 10000;
        String id = randomNum + "CSTAD";

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

        System.out.println("Enter student classroom [+] you can input more than one :");
        String classroom = scanner.nextLine();
        List<String> classrooms = Arrays.asList(classroom.split("/"));
        System.out.println("Enter student subject you can [+] input subject more than one :");
        String subject = scanner.nextLine();
        List<String> subjects = Arrays.asList(subject.split("/"));
        Student student = new Student(id, name, dateOfBirth, String.join(",", classrooms)
                , String.join(",", subjects));
        students.add(student);
        WriteDataToFile(outputFilePath);
        commitOrNot();
        students.clear();
        return List.of();
    }


    public void WriteDataToFile(String paths) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(paths))) {
            StringBuilder studentData = new StringBuilder();
            for (Student s : students) {
                studentData.append(s.getId()).append("/").append(s.getName()).append("/").append(s.getDateOfBirth()).append("/")
                        .append(s.getClassroom()).append("/").append(s.getSubject()).append(System.lineSeparator());
            }
            bos.write(studentData.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }


    @Override
    public void listStudentsAsTable(String file) {
//        Instant  beforeReadData = Instant .now();
//        long startTime = System.nanoTime();
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

//        Instant afterReadDate = Instant.now();
//        long endTime = System.nanoTime();
//        if (Objects.equals(file, "output.csv")) {
////            System.out.print("Time used to Read and Write data: " + ((afterReadDate.toEpochMilli() - beforeReadData.toEpochMilli()) * 0.001) + "s\n");
//            System.out.print("Time used to Read and Write data: " + (endTime - startTime) / 1e9 + " s\n");
//        } else if (Objects.equals(file, "DummyFile.csv")) {
//            System.out.print("Time used to read 1000 000 record: " + (endTime - startTime) / 1e9 + " s\n");
//        } else {
//            System.out.println("No file to read.");
//        }
    }

    public void listDataStudent(List<Student> students, int pageNumber, int recordsPerPage) throws IOException {
        readDataFromFile("output.csv");
        listStudentsAsTable("output.csv");
    }

    public void readDataFromFile(String filePath) {
        Instant startTime = Instant.now();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            StringBuilder content = new StringBuilder();
            byte[] buffer = new byte[1024];
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
                            String subjectParts = parts[4];

                            return new Student(id, name, dateOfBirth, classrooms.toString(), subjectParts.toString());
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            Instant endTime = Instant.now();
            Duration duration = Duration.between(startTime, endTime);
            System.out.println("Time to read data: " +  duration.toMillis()/1000.0+ " seconds");
            System.out.println("Data read successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    private void displayStudentDataWithPagination(List<Student> students) throws IOException {
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
            listDataStudent(students.subList(startIndex, endIndex), 5, 5);
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


    public static void searchStudent(ArrayList<Student> students, Scanner scanner) {
        System.out.println("Choose the search criteria:");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over
        switch (choice) {
            case 1:
                System.out.println("Enter the ID of the student you want to search for:");
                long studentId = scanner.nextLong(); // Reading input directly as long
                scanner.nextLine();
                Optional<Student> foundStudent = students.stream()
                        .filter(s -> s.getId().equalsIgnoreCase(String.valueOf(studentId)))
                        .findFirst();

                if (!foundStudent.isPresent()) { // Corrected condition
                    System.out.println("No student found with ID " + studentId + ".");
                } else {
                    System.out.println("Student name Found: " + foundStudent.get().getName());
                    System.out.println("ID of Student Found:" + foundStudent.get().getId());
                    System.out.println("Classroom found:" + foundStudent.get().getClassroom());
                    System.out.println("Subject FOunded:" + foundStudent.get().getSubject());
                }
                break;
            case 2:
                System.out.println("Enter the name of the student you want to search for:");
                String studentName = scanner.nextLine();
                foundStudent = students.stream()
                        .filter(s -> s.getName().equalsIgnoreCase(studentName))
                        .findFirst();

                if (foundStudent.isPresent()) {
                    System.out.println("Student Found: " + foundStudent.get().getId());
                    System.out.println("ID of Student Found:" + foundStudent.get().getId());
                    System.out.println("Classroom found:" + foundStudent.get().getClassroom());
                    System.out.println("Subject FOunded:" + foundStudent.get().getSubject());
                } else {
                    System.out.println("No student found with name " + studentName + ".");
                }
                break;
            default:
                System.out.println("Invalid choice. Please choose 1 for ID or 2 for Name.");
                break;
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
    public void updateDataInFile(String filePath) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the student you want to update:");
        long studentId = scanner.nextLong();
        // Search for the student in the list
        Optional<Student> optionalStudent = students.stream()
                .filter(student -> student.getId().equalsIgnoreCase(String.valueOf(studentId)))
                .findFirst();

        if (optionalStudent.isPresent()) {
            Student studentToUpdate = optionalStudent.get();

            // Display current student details
            System.out.println("Current details of the student:");
            displayStudentDetails(studentToUpdate);

            // Prompt user for the field to update
            System.out.println("Which field do you want to update? (1.name/2.date/3.class/4.subject)");
            int fieldToUpdate = scanner.nextInt();
            scanner.nextLine();
            // Update the chosen field
            switch (fieldToUpdate) {
                case 1:
                    System.out.println("Enter new name:");
                    String newName = scanner.nextLine().trim();
                    studentToUpdate.setName(newName);
                    break;
                case 2:
                    System.out.println("Enter new date of birth (YYYY-MM-DD):");
                    String newDateOfBirth = scanner.nextLine().trim();
                    studentToUpdate.setDateOfBirth(newDateOfBirth);
                    break;
                case 3:
                    System.out.println("Enter new classroom:");
                    String newClassroom = scanner.nextLine().trim();
                    studentToUpdate.setClassroom(newClassroom);
                    break;
                case 4:
                    System.out.println("Enter new subject:");
                    String newSubject = scanner.nextLine().trim();
                    studentToUpdate.setSubject(newSubject);
                    break;
                default:
                    System.out.println("Invalid field specified. No changes made.");
                    break;
            }
            WriteDataToFile(outputFilePath);
            commitOrNot();
        } else {
            System.out.println("Student with ID " + studentId + " not found.");
        }

    }

    public void commitOrNot() throws IOException {
        Path tempFilePath = Paths.get("Temp.csv");
        Path outputFilePath = Paths.get("output.csv");
        boolean fileExistsAndNotEmpty = Files.exists(tempFilePath) && Files.size(tempFilePath) > 0;
        if (fileExistsAndNotEmpty == false) {
            System.out.println("Data already commit So not need to commit ><.");

        } else {
            System.out.println("Do you want to commit changes? (yes/no)");
            String answer = new Scanner(System.in).nextLine();
            if (answer.equals("yes") || answer.equals("Yes")) {
                try (BufferedReader reader = Files.newBufferedReader(tempFilePath);
                     BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                    System.out.println("Data written successfully to " + inputFilePath);
                } catch (IOException e) {
                    System.out.println("Error transferring data: " + e.getMessage());
                }
                // Clear data from temp.csv
//                try {
//                    Files.write(tempFilePath, Collections.emptyList());
//                    System.out.println("Data transferred and temp.csv cleared successfully.");
//                } catch (IOException e) {
//                    System.out.println("Error clearing temp.csv: " + e.getMessage());
//                }
                try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath)) {
                    writer.write("");
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("Error clearing Temp.csv: " + e.getMessage());
                }
//                System.out.println("Data transferred and Temp.csv cleared successfully.");
            } else {
                try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath)) {
                    writer.write("");
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("Error clearing Temp.csv: " + e.getMessage());
                }
                System.out.println("Data transferred and Temp.csv cleared successfully.");
                System.out.println("Data has not input to file");
            }
        }

    }

    @Override
    public void deleteDataById(String filePath) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the student you want to delete:");
        String studentId = scanner.nextLine().trim();

        try {
            Path path = Paths.get(filePath);
            List<String> fileContent = new ArrayList<>(Files.readAllLines(path));
            boolean found = false;

            for (int i = 0; i < fileContent.size(); i++) {
                String line = fileContent.get(i);
                String[] parts = line.split("/");
                if (parts.length >= 1 && parts[0].equals(studentId)) {
                    WriteDataToFile(outputFilePath);
                    fileContent.remove(i);
                    found = true;
                    break;
                }
            }
            commitOrNot();
            if (found) {
                Files.write(path, fileContent);
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
                Files.newBufferedWriter(filePath, StandardOpenOption.TRUNCATE_EXISTING).close();
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

    public void generateData() {
        String dummyFilePath = inputFilePath;
        int TOTAL_RECORDS = 1000001;
        int BATCH_SIZE = 1000; // Adjust batch size based on performance testing
        Random random = new Random();

        // Create a BlockingQueue to collect records from threads
        BlockingQueue<String> recordQueue = new LinkedBlockingQueue<>();

        // Submit tasks to a ThreadPoolExecutor
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10, // Core pool size
                20, // Maximum pool size
                60, // Keep alive time
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()); // Work queue

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(dummyFilePath), StandardCharsets.UTF_8)) {
            for (int i = 0; i <= TOTAL_RECORDS; i += BATCH_SIZE) {
                for (int j = i; j < i + BATCH_SIZE && j < TOTAL_RECORDS; j++) {
                    int finalJ = j;
                    executor.execute(() -> {
                        int randomNum = random.nextInt(90000) + 10000;
                        String id = randomNum + "CSTAD";
                        String name = "student" + finalJ;
                        String dateOfBirth = "2000-01-01"; // Example date
                        String classroom = "Class" + (finalJ % 10 + 1); // Example classroom
                        String subject = "Subject" + (finalJ % 5 + 1); // Example subject

                        String sb = id + "/" +
                                name + "/" +
                                dateOfBirth + "/" +
                                classroom + "/" +
                                subject +
                                System.lineSeparator(); // Use system-dependent line separator

                        recordQueue.offer(sb);
                    });

                }
            }
            readDataFromFile(inputFilePath);
//            listStudentsAsTable(inputFilePath);
            // Wait for all tasks to finish
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }

            // Process the queue to write records to the file
            while (!recordQueue.isEmpty()) {
                String record = recordQueue.poll();
                try {
                    writer.write(record);
                    writer.newLine();
                } catch (IOException e) {
                    System.err.println("Error writing to file: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
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
        do {
            student.commitOrNot();
            System.out.println("\n>>>>>>>>>>>>>Check In before Choose<<<<<<<<<<<<<");
            System.out.println(repeat("=", 1000));
            System.out.println("""
                        1.Add Student           2.List All Student              3.CommitData to File\

                        4.Search for Student    5.Update Student by Info By ID  6.Delete Student Data\

                        7.Generate Data to File 8.Delete/Clear Data Store From Data Store
                        0,99.Exit""");
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
                    System.out.println(repeat("=", 1000));
                    System.out.print("[+]Page number : 1");
                    System.out.print("\t\t[+]Actual Record :"+(count-1));
                    System.out.print("\t\t[+] All record:"+count);
                    System.out.println("\t\t\t\t\t\t[+]Previous(prev)\t-Next(next)\t-Back(B)");
                    System.out.println(repeat("=", 1000));
                    student.displayStudentDataWithPagination(students);
                    break;
                case 3:
                    student.commitOrNot();
                    break;
                case 4:
                    searchStudent((ArrayList<Student>) students,scanner);
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

