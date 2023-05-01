package hexlet.code.formatters;

import hexlet.code.KeyState;

import java.util.Map;

public class Stylish {
    public static String format(Map<String, KeyState> diffMap) {
        if (diffMap.size() == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{\n");
        for (Map.Entry<String, KeyState> entry: diffMap.entrySet()) {
            var key = entry.getKey();
            var values = entry.getValue().values;
            var state = entry.getValue().state;
            switch (state) {
                case "unchanged" -> sb.append("    ").append(key).append(": ").append(values[0]).append("\n");
                case "changed" -> sb.append("  - ").append(key).append(": ").append(values[0]).append("\n").append("  + ").append(key).append(": ").append(values[1]).append("\n");
                case "deleted" -> sb.append("  - ").append(key).append(": ").append(values[0]).append("\n");
                case "added" -> sb.append("  + ").append(key).append(": ").append(values[0]).append("\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
