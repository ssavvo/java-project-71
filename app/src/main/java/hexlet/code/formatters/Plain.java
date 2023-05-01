package hexlet.code.formatters;

import hexlet.code.KeyState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Plain {
    public static String format(Map<String, KeyState> diffMap) throws Exception {
        if (diffMap.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        var index = 0;
        var last = diffMap.size();
        for (Map.Entry<String, KeyState> entry: diffMap.entrySet()) {
            index++;
            var key = entry.getKey();
            var values = entry.getValue().getValues();
            var state = entry.getValue().getState();
            switch (state) {
                case "unchanged" -> {
                    continue;
                }
                case "changed" -> {
                    var value1Representation = getRepresentation(values[0]);
                    var value2Representation = getRepresentation(values[1]);
                    sb.append("Property '").append(key).append("' was updated. From ")
                            .append(value1Representation).append(" to ").append(value2Representation);
                }
                case "deleted" -> sb.append("Property '").append(key).append("' was removed");
                case "added" -> {
                    var value3Representation = getRepresentation(values[0]);
                    sb.append("Property '").append(key).append("' was added with value: ")
                            .append(value3Representation);
                }
                default -> throw new Exception("Wrong state");
            }
            if (index < last) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private static Object getRepresentation(Object item) {
        if (item instanceof ArrayList || item instanceof LinkedHashMap) {
            return "[complex value]";
        } else if (item instanceof String) {
            return "'" + item + "'";
        } else {
            return item;
        }
    }
}
