import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import utils.PropertiesReader;

public class SpaceC {

    static PropertiesReader propertiesReader = new PropertiesReader(
        "src/main/resources/strings.properties");

    public static void main(String[] args) throws IOException {

        if (tooManyArgs(args)) {
            System.out.println(propertiesReader.getString("cli.error"));
            /*
                Exitcodes based on Unix sysexit.h, viewable here:
                https://www.freebsd.org/cgi/man.cgi?query=sysexits&apropos=0&sektion=0&manpath=FreeBSD+4.3-RELEASE&format=html
             */
            System.exit(64);
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
        byte[] data = Files.readAllBytes(Paths.get(path));
        String sourceCode = new String(data, Charset.defaultCharset());
        run(sourceCode);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            printPrompt();
            String sourceLine = reader.readLine();
            if (sourceLine == null) {
                break;
            }
            run(sourceLine);
        }
    }

    private static void printPrompt() {
        System.out.println(propertiesReader.getString("cli.prompt"));
    }

    private static void run(String source) {
    /*
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        for (Token token : tokens) {
            System.out.println(token);
        }
     */
    }
}
