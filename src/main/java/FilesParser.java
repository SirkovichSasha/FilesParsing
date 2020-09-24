import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.readString;

public class FilesParser {

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName));) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader).
                    withMappingStrategy(strategy).
                    build();
            list = csv.parse();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = builder.parse(new File(fileName));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node root = doc.getDocumentElement();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                String firstname = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lastname = element.getElementsByTagName("lastName").item(0).getTextContent();
                String country = element.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                list.add(new Employee(id, firstname, lastname, country, age));
            }
        }

        return list;
    }

    public static <T> void listToJson(List<T> list) {
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try (FileWriter file = new
                FileWriter("C:\\Users\\Owner\\Desktop\\Netology\\Java-Core\\FilesParsing\\src\\main\\resources\\new_data.json")) {
            String json = gson.toJson(list, listType);
            file.write(json.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void jsonToEmployee(String filename) {
        List<Employee> list = new ArrayList<>();
        try {
            String jsonText = readString(Paths.get(filename));
            list = jsonToList(jsonText);
            for (Employee e : list
            ) {
                System.out.println(e.toString());
            }
        } catch (IOException e) {
        }
        
    }

    private static List<Employee> jsonToList(String jsonText) {
        List<Employee> list = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonText);
            JSONArray objs = (JSONArray) obj;
            for (Object e : objs) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Employee emp = gson.fromJson(e.toString(), Employee.class);
                list.add(emp);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }
}


