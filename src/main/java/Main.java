import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "C:\\Users\\Owner\\Desktop\\Netology\\Java-Core\\FilesParsing\\src\\main\\resources\\data.csv";
        String fileName2 = "C:\\Users\\Owner\\Desktop\\Netology\\Java-Core\\FilesParsing\\src\\main\\resources\\data.xml";

        List<Employee> list = FilesParser.parseCSV(columnMapping, fileName);
        List<Employee> list2 = FilesParser.parseXML(fileName2);

        FilesParser.listToJson(list);
        FilesParser.listToJson(list2);
    }
}
