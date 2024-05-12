package ProcessMini;

import java.io.IOException;
import java.util.List;

public interface AddStudent {
    List<String> add() throws IOException;

    void listStudentsAsTable(String file);
    void searchStudent();
    void updateDataInFile(String file);
    void deleteDataById(String filePath);
}

