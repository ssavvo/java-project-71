package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.TreeMap;

public class Parser {
    public static TreeMap<String, Object> parse(String data, String extension) throws Exception {
        return switch (extension) {
            case "json" -> parseJSON(data);
            case "yml", "yaml" -> parseYAML(data);
            default -> throw new Exception("Unknown file extension");
        };
    }

    private static TreeMap<String, Object> parseJSON(String data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(data, new TypeReference<>() { });
    }
    private static TreeMap<String, Object> parseYAML(String data) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(data, new TypeReference<>() { });
    }
}
