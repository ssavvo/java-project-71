package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "gendiff 0.0.1",
        description = "Compares two configuration files and shows a difference.")
public class App implements Callable<Integer> {
    @Parameters(index = "0", description = "path to first file", paramLabel = "filepath1")
    private String filepath1;
    @Parameters(index = "1", description = "path to second file", paramLabel = "filepath2")
    private String filepath2;
    @Option(names = {"-f", "--format"}, description = "output format [default: stylish]",
            paramLabel = "format", defaultValue = "stylish")
    private static String format;
    @Override
    public Integer call() throws Exception { // your business logic goes here...
        System.out.println(Differ.generate(filepath1, filepath2, format));
        return 0;
    }
    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}
