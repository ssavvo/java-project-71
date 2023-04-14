package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Callable;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "gendiff 0.0.1",
        description = "Compares two configuration files and shows a difference.")
class App implements Callable<Integer> {
    @Parameters(index = "0", description = "path to first file", paramLabel = "filepath1")
    private File file1;
    @Parameters(index = "1", description = "path to second file", paramLabel = "filepath2")
    private File file2;
    @Option(names = {"-f", "--format"}, description = "output format [default: stylish]", paramLabel = "format")
    private String format = "stylish";
    @Override
    public Integer call() throws Exception { // your business logic goes here...
        var file1Content = Files.readString(file1.toPath());
        var file2Content = Files.readString(file2.toPath());
        ObjectMapper objectMapper = new ObjectMapper();
        List<TreeMap<String, Object>> maps = List.of(objectMapper.readValue(file1Content, new TypeReference<>(){}), objectMapper.readValue(file2Content, new TypeReference<>(){}));
        var map1 = maps.get(0);
        var map2 = maps.get(1);
        /*if (map1.isEmpty() && map2.isEmpty()) {
            return "{}";
        }*/
        String[][] keys = {map1.keySet().toArray(String[]::new), map2.keySet().toArray(String[]::new)};
        int[] pointers = {0, 0};
        Map<String, Object> diff = new LinkedHashMap<>();
        while (pointers[0] < keys[0].length && pointers[1] < keys[1].length) {
            var key1 = keys[0][pointers[0]];
            var key2 = keys[1][pointers[1]];
            var compareResult = key1.compareTo(key2);
            if (compareResult == 0) {
                var value1 = map1.get(key1);
                var value2 = map2.get(key2);
                if (value1.equals(value2)) {
                    diff.put("  " + key1, value1);
                } else {
                    diff.put("- " + key1, value1);
                    diff.put("+ " + key2, value2);
                }
                pointers[0]++;
                pointers[1]++;
            } else if (compareResult > 0) {
                var value2 = map2.get(key2);
                diff.put("+ " + key2, value2);
                pointers[1]++;
            } else {
                var value1 = map1.get(key1);
                diff.put("- " + key1, value1);
                pointers[0]++;
            }
        }
        int restIndex = pointers[0] < keys[0].length ? 0 : 1;
        String prefix = restIndex == 0 ? "- " : "+ ";
        for (var i = pointers[restIndex]; i < keys[restIndex].length; i++) {
            var key = keys[restIndex][i];
            diff.put(prefix + key, maps.get(restIndex).get(key));
        }
        StringBuilder diffString = new StringBuilder("{\n");
        for (Map.Entry<String, Object> entry: diff.entrySet()) {
            diffString.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        diffString.append("}");
        System.out.println(diffString.toString());
        return 0;
    }

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}