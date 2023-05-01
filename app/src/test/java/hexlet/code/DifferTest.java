package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class DifferTest {
    private static File emptyJson;
    private static File json1;
    private static File json2;
    @BeforeAll
    public static void beforeAll() {
        String writeFilePath = Paths.get("./src/test/resources").toAbsolutePath().normalize().toString();
        emptyJson = new File(writeFilePath + "/empty.json");
        json1 = new File(writeFilePath + "/json1.json");
        json2 = new File(writeFilePath + "/json2.json");
        try {
            if (emptyJson.createNewFile()) {
                FileWriter writer = new FileWriter(emptyJson);
                writer.write("{}");
                writer.close();
            }
            if (json1.createNewFile()) {
                FileWriter writer = new FileWriter(json1);
                writer.write("""
                        {
                          "setting1": "Some value",
                          "setting2": 200,
                          "setting3": true,
                          "key1": "value1",
                          "numbers1": [1, 2, 3, 4],
                          "numbers2": [2, 3, 4, 5],
                          "id": 45,
                          "default": null,
                          "checked": false,
                          "numbers3": [3, 4, 5],
                          "chars1": ["a", "b", "c"],
                          "chars2": ["d", "e", "f"]
                        }""");
                writer.close();
            }
            if (json2.createNewFile()) {
                FileWriter writer = new FileWriter(json2);
                writer.write("""
                        {
                          "setting1": "Another value",
                          "setting2": 300,
                          "setting3": "none",
                          "key2": "value2",
                          "numbers1": [1, 2, 3, 4],
                          "numbers2": [22, 33, 44, 55],
                          "id": null,
                          "default": ["value1", "value2"],
                          "checked": true,
                          "numbers4": [4, 5, 6],
                          "chars1": ["a", "b", "c"],
                          "chars2": false,
                          "obj1": {
                            "nestedKey": "value",
                            "isNested": true
                          }
                        }""");
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @AfterAll
    public static void afterAll() {
        emptyJson.delete();
        json1.delete();
        json2.delete();
    }
    @Test
    public void emptyJsonDiff() {
        try {
            assertEquals("{}", Differ.generate(emptyJson.getPath(), emptyJson.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void jsonDiff1() {
        String expected = """
                {
                    chars1: [a, b, c]
                  - chars2: [d, e, f]
                  + chars2: false
                  - checked: false
                  + checked: true
                  - default: null
                  + default: [value1, value2]
                  - id: 45
                  + id: null
                  - key1: value1
                  + key2: value2
                    numbers1: [1, 2, 3, 4]
                  - numbers2: [2, 3, 4, 5]
                  + numbers2: [22, 33, 44, 55]
                  - numbers3: [3, 4, 5]
                  + numbers4: [4, 5, 6]
                  + obj1: {nestedKey=value, isNested=true}
                  - setting1: Some value
                  + setting1: Another value
                  - setting2: 200
                  + setting2: 300
                  - setting3: true
                  + setting3: none
                }""";
        try {
            assertEquals(expected, Differ.generate(json1.getPath(), json2.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void jsonDiff2() {
        String expected = """
                {
                    chars1: [a, b, c]
                  - chars2: false
                  + chars2: [d, e, f]
                  - checked: true
                  + checked: false
                  - default: [value1, value2]
                  + default: null
                  - id: null
                  + id: 45
                  + key1: value1
                  - key2: value2
                    numbers1: [1, 2, 3, 4]
                  - numbers2: [22, 33, 44, 55]
                  + numbers2: [2, 3, 4, 5]
                  + numbers3: [3, 4, 5]
                  - numbers4: [4, 5, 6]
                  - obj1: {nestedKey=value, isNested=true}
                  - setting1: Another value
                  + setting1: Some value
                  - setting2: 300
                  + setting2: 200
                  - setting3: none
                  + setting3: true
                }""";
        try {
            assertEquals(expected, Differ.generate(json2.getPath(), json1.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
