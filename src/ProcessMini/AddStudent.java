package ProcessMini;

import Mini.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public interface AddStudent {
    List<String> add() throws IOException;
    void listStudentsAsTable(String file);
    void commitOrNot() throws IOException;
    void searchStudent(ArrayList<Student> students, Scanner scanner);
    void updateDataInFile(String file) throws IOException;
    void deleteDataById(String filePath);
    void generateData();
    void clearDataStore();
}

