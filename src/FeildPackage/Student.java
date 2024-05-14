package FeildPackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String id;
    private String name;
    private String dateOfBirth;
    private String classroom;
    private String subject;
    public static List<Student> students = new ArrayList<>();
    public static String inputFilePath = "output.csv";
    public static String outputFilePath = "Temp.csv";
    public static int currentIndex = 0;
    public  static int pageSize = 5;
    public static int prevIndex = 0;
}
