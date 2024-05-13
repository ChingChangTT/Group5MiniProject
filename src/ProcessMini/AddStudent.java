package ProcessMini;

import java.io.IOException;
import java.util.List;

public interface AddStudent {
    List<String> add() throws IOException;

    void listStudentsAsTable(String file);
    void updateDataInFile(String file) throws IOException;
    void deleteDataById(String filePath);
}

