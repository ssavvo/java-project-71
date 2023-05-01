package hexlet.code.formatters;

import hexlet.code.KeyState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Plain {
    public static String format(Map<String, KeyState> diffMap) {
        if (diffMap.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, KeyState> entry: diffMap.entrySet()) {
            var key = entry.getKey();
            var values = entry.getValue().values;
            var state = entry.getValue().state;
            switch (state) {
                case "unchanged":
                    break;
                case "changed":
                    var value1Representation = getRepresentation(values[0]);
                    var value2Representation = getRepresentation(values[1]);
                    sb.append("Property '").append(key).append("' was updated. From ")
                            .append(value1Representation).append(" to ").append(value2Representation).append("\n");
                    break;
                case "deleted":
                    sb.append("Property '").append(key).append("' was removed\n");
                    break;
                case "added":
                    var value3Representation = getRepresentation(values[0]);
                    sb.append("Property '").append(key).append("' was added with value: ")
                            .append(value3Representation).append("\n");
                    break;
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
