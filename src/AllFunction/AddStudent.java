package AllFunction;
import FeildPackage.Student;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public interface AddStudent {
    List<String> add() throws IOException;
    void listStudentsAsTable(String file);
    void searchStudent(List<Student> students, Scanner scanner);
    int updateDataInFile(String filePath) throws IOException;
    void commitOrNot() throws IOException;
    void deleteDataById(String filePath);
    void clearDataStore();
    void generateData();
}
