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
                writer.write("{\n"
                        + "\"host\": \"hexlet.io\","
                        + "\"timeout\": 50,"
                        + "\"proxy\": \"123.234.53.22\","
                        + "\"follow\": false"
                        + "}");
                writer.close();
            }
            if (json2.createNewFile()) {
                FileWriter writer = new FileWriter(json2);
                writer.write("{\n"
                        + "\"timeout\": 20,"
                        + "\"verbose\": true,"
                        + "\"host\": \"hexlet.io\""
                        + "}");
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
                  - follow: false
                    host: hexlet.io
                  - proxy: 123.234.53.22
                  - timeout: 50
                  + timeout: 20
                  + verbose: true
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
                  + follow: false
                    host: hexlet.io
                  + proxy: 123.234.53.22
                  - timeout: 20
                  + timeout: 50
                  - verbose: true
                }""";
        try {
            assertEquals(expected, Differ.generate(json2.getPath(), json1.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
