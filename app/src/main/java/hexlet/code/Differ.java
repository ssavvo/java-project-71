package hexlet.code;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class Differ {
    private static final Map<String, String> NORMALIZE_EXTENSION =
            Map.of("yml","yaml", "yaml", "yaml", "json", "json");
    public static String generate(String path1, String path2) throws Exception {
        return generate(path1, path2, "stylish");
    }
    public static String generate(String path1, String path2, String format) throws Exception {
        Path file1Path = getPath(path1);
        Path file2Path = getPath(path2);
        if (!Files.exists(file1Path)) {
            throw new Exception("File '" + file1Path + "' does not exist");
        }
        if (!Files.exists(file2Path)) {
            throw new Exception("File '" + file2Path + "' does not exist");
        }
        String file1Ext = getExtension(file1Path);
        String file2Ext = getExtension(file2Path);
        if (!file1Ext.equals(file2Ext)) {
            throw new Exception("Files have different extension");
        }
        String file1Content = Files.readString(file1Path).length() == 0 ? "{}" : Files.readString(file1Path);
        String file2Content = Files.readString(file2Path).length() == 0 ? "{}" : Files.readString(file2Path);
        Map<String, Object> map1 = Parser.parse(file1Content, file1Ext);
        Map<String, Object> map2 = Parser.parse(file2Content, file1Ext);
        var diff = calcDiff(map1, map2);
        return Formatter.format(diff, format);
    }

    private static Path getPath(String path) {
        return Paths.get(path).toAbsolutePath().normalize();
    }
    private static String getExtension(Path path) throws Exception {
        String p = path.toString();
        String extension =  p.substring(p.lastIndexOf(".") + 1);
        if (!NORMALIZE_EXTENSION.containsKey(extension)) {
            throw new Exception(extension + " not valid file format");
        }
        return NORMALIZE_EXTENSION.get(extension);
    }
    private static Map<String, KeyState> calcDiff(Map<String, Object> map1, Map<String, Object> map2) {
        TreeMap<String, KeyState> diff = new TreeMap<>();
        TreeSet<String> unionKey = new TreeSet<>();
        unionKey.addAll(map1.keySet());
        unionKey.addAll(map2.keySet());
        for (String key: unionKey) {
            var isKeyInMap1 = map1.containsKey(key);
            var isKeyInMap2 = map2.containsKey(key);
            if (isKeyInMap1 && isKeyInMap2) {
                var value1 = map1.get(key);
                var value2 = map2.get(key);
                if (Objects.equals(value1, value2)) {
                    diff.put(key, new KeyState("unchanged", new Object[]{value1}));
                } else {
                    diff.put(key, new KeyState("changed", new Object[]{value1, value2}));
                }
            } else if (isKeyInMap1) {
                var value1 = map1.get(key);
                diff.put(key, new KeyState("deleted", new Object[]{value1}));
            } else {
                var value2 = map2.get(key);
                diff.put(key, new KeyState("added", new Object[]{value2}));
            }
        }
        return diff;
    }
}
