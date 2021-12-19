package parser;

import java.util.Arrays;
import lexer.Token;

public class TestUtils {

    static Parser initializeParser(Token... tokens) {
        return new Parser(Arrays.asList(tokens));
    }
}
