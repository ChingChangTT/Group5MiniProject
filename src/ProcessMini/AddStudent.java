package ProcessMini;

import java.util.List;

public interface AddStudent {
    List<String> add();

    void listStudentsAsTable(String file);
    void searchStudent();
    void updateDataInFile(String file);
    void deleteDataById(String filePath);
}

