package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "gendiff 0.0.1",
        description = "Compares two configuration files and shows a difference.")
class App implements Callable<Integer> {
    @Parameters(index = "0", description = "path to first file", paramLabel = "filepath1")
    private File file1;
    @Parameters(index = "1", description = "path to second file", paramLabel = "filepath2")
    private File file2;
    @Option(names = {"-f", "--format"}, description = "output format [default: stylish]", paramLabel = "format")
    private String format = "SHA-256";
    @Override
    public Integer call() throws Exception { // your business logic goes here...

        return 0;
    }

    // this example implements Callable, so parsing, error handling and handling user
    // requests for usage help or version help can be done with one line of code.
    public static void main(String... args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}