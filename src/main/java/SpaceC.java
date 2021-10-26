import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import lexer.Lexer;
import lexer.Token;
import utils.Exitcode;
import utils.FileToStringConverter;
import utils.PropertiesReader;

public class SpaceC {

    static PropertiesReader propertiesReader = new PropertiesReader(
        "src/main/resources/strings.properties");
    static boolean hasError = false;

    public static void main(String[] args) throws IOException {

        if (tooManyArgs(args)) {
            System.out.println(propertiesReader.getString("cli.error"));
            System.exit(Exitcode.EX_USAGE.value);
        } else if (pathGiven(args)) {
            String path = args[0];
            runFile(path);
        } else {
            runPrompt();
        }
    }

    private static boolean tooManyArgs(String[] args) {
        return args.length > 1;
    }

    private static boolean pathGiven(String[] args) {
        return args.length == 1;
    }

    private static void runFile(String path) throws IOException {
        String sourceCode = FileToStringConverter.convert(path);
        run(sourceCode);
        if (hasError) {
            System.exit(Exitcode.EX_DATAERR.value);
        }
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            printPrompt();
            String sourceLine = reader.readLine();
            if (Objects.equals(sourceLine, "exit")) {
                break;
            }
            run(sourceLine);
            // Don't kill user's session if they make a mistake.
            hasError = false;
        }
    }

    private static void printPrompt() {
        System.out.println(propertiesReader.getString("cli.prompt"));
    }

    private static void run(String source) {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
