package hexlet.code;

import hexlet.code.formatters.Json;
import hexlet.code.formatters.Plain;
import hexlet.code.formatters.Stylish;

import java.util.Map;

public class Formatter {
    public static String format(Map<String, KeyState> diffMap, String style) throws Exception {
        return switch (style) {
            case "stylish" -> Stylish.format(diffMap);
            case "plain" -> Plain.format(diffMap);
            case "json" -> Json.toJson(diffMap);
            default -> "";
        };
    }
}
