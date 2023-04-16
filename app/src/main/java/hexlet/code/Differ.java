package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.concurrent.Callable;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "gendiff 0.0.1",
        description = "Compares two configuration files and shows a difference.")
class Differ implements Callable<Integer> {
    @Parameters(index = "0", description = "path to first file", paramLabel = "filepath1")
    private String filepath1;
    @Parameters(index = "1", description = "path to second file", paramLabel = "filepath2")
    private String filepath2;
    @Option(names = {"-f", "--format"}, description = "output format [default: stylish]", paramLabel = "format")
    private String format = "stylish";
    @Override
    public Integer call() throws Exception { // your business logic goes here...
        var diff = Differ.generate(filepath1, filepath2);
        System.out.println(diff);
        return 0;
    }

    public static <T> String generate(String path1, String path2) throws Exception {
        Path file1Path = Paths.get(path1).toAbsolutePath().normalize();
        Path file2Path = Paths.get(path2).toAbsolutePath().normalize();
        String file1Content = Files.readString(file1Path).length() == 0 ? "{}" : Files.readString(file1Path);
        String file2Content = Files.readString(file2Path).length() == 0 ? "{}" : Files.readString(file2Path);
        ObjectMapper mapper = new ObjectMapper();
        TreeMap<String, String> map1 = mapper
                .readValue(file1Content, new TypeReference<>() { });
        TreeMap<String, String> map2 = mapper
                .readValue(file2Content, new TypeReference<>() { });
        if (map1.size() == 0 && map2.size() == 0) {
            return "{}";
        }
        StringBuilder diff = new StringBuilder("{\n");
        String[] keys1 = map1.keySet().toArray(String[]::new);
        String[] keys2 = map2.keySet().toArray(String[]::new);
        int p1 = 0;
        int p2 = 0;
        while (p1 < map1.size() || p2 < map2.size())    {
            diff.append("  ");
            if (p1 == map1.size()) {
                var key = keys2[p2];
                diff.append("+ ").append(key).append(": ").append(map2.get(key)).append("\n");
                p2++;
                continue;
            }
            if (p2 == map2.size()) {
                var key = keys1[p1];
                diff.append("- ").append(key).append(": ").append(map1.get(key)).append("\n");
                p1++;
                continue;
            }
            var key1 = keys1[p1];
            var key2 = keys2[p2];
            var compareResult = key1.compareTo(key2);
            if (compareResult == 0) {
                var value1 = map1.get(key1);
                var value2 = map2.get(key2);
                if (value1.equals(value2)) {
                    diff.append("  ").append(key1).append(": ").append(map1.get(key1)).append("\n");
                } else {
                    diff.append("- ").append(key1).append(": ").append(map1.get(key1)).append("\n");
                    diff.append("  ").append("+ ").append(key2).append(": ").append(map2.get(key2)).append("\n");
                }
                p1++;
                p2++;
            } else if (compareResult > 0) {
                diff.append("+ ").append(key2).append(": ").append(map2.get(key2)).append("\n");
                p2++;
            } else {
                diff.append("- ").append(key1).append(": ").append(map1.get(key1)).append("\n");
                p1++;
            }
        }
        diff.append("}");
        return diff.toString();
    }
    public static void main(String... args) {
        int exitCode = new CommandLine(new Differ()).execute(args);
        System.exit(exitCode);
    }
}
