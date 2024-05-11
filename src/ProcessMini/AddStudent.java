package ProcessMini;

import java.io.IOException;
import java.util.List;

public interface AddStudent {
    List<String> add();
    void listStudentsAsTable() throws IOException;
    void searchStudent();
    void updateDataInFile(String file);

    void deleteDataById(String filePath);
}

