package lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lexer.tokenTypes.EndOfFile;
import lexer.tokenTypes.IdentifierToken;
import lexer.tokenTypes.Keyword;
import lexer.tokenTypes.LiteralToken;
import lexer.tokenTypes.SingleCharacterToken;
import lexer.tokenTypes.SingleOrTwoCharacterToken;
import org.junit.jupiter.api.Test;
import utils.FileToStringConverter;

public class TokenizationTests {

    @Test
    void recognizesSingleCharacterTokens() throws IOException {
        Lexer lexer = initializeLexer("src/test/resources/lexer/singleCharacterTokens.space");

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(SingleCharacterToken.LEFT_PARENTHESIS, "(", null, 1));
        expected.add(new Token(SingleCharacterToken.RIGHT_PARENTHESIS, ")", null, 1));
        expected.add(new Token(SingleCharacterToken.COLON, ":", null, 1));
        expected.add(new Token(SingleCharacterToken.SEMICOLON, ";", null, 1));
        expected.add(new Token(SingleCharacterToken.STAR, "*", null, 1));
        expected.add(new Token(SingleCharacterToken.LEFT_BRACE, "{", null, 1));
        expected.add(new Token(SingleCharacterToken.RIGHT_BRACE, "}", null, 1));
        expected.add(new Token(SingleCharacterToken.DOT, ".", null, 1));
        expected.add(new Token(SingleCharacterToken.COMMA, ",", null, 1));
        expected.add(new Token(SingleCharacterToken.PLUS, "+", null, 1));
        expected.add(new Token(SingleCharacterToken.MINUS, "-", null, 1));
        expected.add(new Token(EndOfFile.EOF, "", null, 1));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);
    }

    @Test
    void recognizesSingleOrTwoCharacterTokens() throws IOException {
        Lexer lexer = initializeLexer("src/test/resources/lexer/singleOrTwoCharacterTokens.space");

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(SingleOrTwoCharacterToken.EQUAL, "=", null, 1));
        expected.add(new Token(SingleOrTwoCharacterToken.GREATER, ">", null, 2));
        expected.add(new Token(SingleOrTwoCharacterToken.GREATER_EQUAL, ">=", null, 3));
        expected.add(new Token(SingleOrTwoCharacterToken.LESS, "<", null, 4));
        expected.add(new Token(SingleOrTwoCharacterToken.LESS_EQUAL, "<=", null, 5));
        expected.add(new Token(SingleOrTwoCharacterToken.BANG, "!", null, 6));
        expected.add(new Token(SingleOrTwoCharacterToken.BANG_EQUAL, "!=", null, 7));
        expected.add(new Token(EndOfFile.EOF, "", null, 7));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);
    }

    @Test
    void recognizesComments() throws IOException {
        Lexer lexer = initializeLexer("src/test/resources/lexer/comments.space");

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(SingleCharacterToken.LEFT_BRACE, "{", null, 2));
        expected.add(new Token(SingleCharacterToken.RIGHT_BRACE, "}", null, 2));
        expected.add(new Token(EndOfFile.EOF, "", null, 2));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);

    }

    @Test
    void ignoresWhitespace() throws IOException {
        Lexer lexer = initializeLexer("src/test/resources/lexer/whitespace.space");

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(SingleCharacterToken.LEFT_BRACE, "{", null, 3));
        expected.add(new Token(SingleCharacterToken.RIGHT_BRACE, "}", null, 3));
        expected.add(new Token(EndOfFile.EOF, "", null, 5));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);

    }

    @Test
    void recognizesStrings() throws IOException {
        Lexer lexer = initializeLexer("src/test/resources/lexer/strings.space");

        List<Token> expected = new ArrayList<>();
        var singleLineLiteral = "Strings are easy to write.";
        var multiLineLiteral = "\nMulti\nrow\nstrings\nare\nno\nproblem\n";
        expected.add(new Token(
            LiteralToken.STRING,
            "\"Strings are easy to write.\"",
            singleLineLiteral,
            1
        ));
        expected.add(new Token(
            LiteralToken.STRING,
            "\"\nMulti\nrow\nstrings\nare\nno\nproblem\n\"",
            multiLineLiteral,
            10
        ));
        expected.add(new Token(EndOfFile.EOF, "", null, 10));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);
    }

    @Test
    void recognizesIntegers() throws IOException {
        Lexer lexer = initializeLexer("src/test/resources/lexer/integers.space");

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(LiteralToken.NUMBER, "0", 0.0, 1));
        expected.add(new Token(LiteralToken.NUMBER, "1", 1.0, 1));
        expected.add(new Token(LiteralToken.NUMBER, "2", 2.0, 1));
        expected.add(new Token(LiteralToken.NUMBER, "3", 3.0, 1));
        expected.add(new Token(LiteralToken.NUMBER, "4", 4.0, 1));
        expected.add(new Token(LiteralToken.NUMBER, "5", 5.0, 1));
        expected.add(new Token(LiteralToken.NUMBER, "6", 6.0, 1));
        expected.add(new Token(LiteralToken.NUMBER, "7", 7.0, 1));
        expected.add(new Token(LiteralToken.NUMBER, "8", 8.0, 1));
        expected.add(new Token(LiteralToken.NUMBER, "9", 9.0, 1));
        expected.add(new Token(EndOfFile.EOF, "", null, 1));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);
    }

    @Test
    void recognizesDoubles() throws IOException {
        Lexer lexer = initializeLexer("src/test/resources/lexer/doubles.space");

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(LiteralToken.NUMBER, "1.2345", 1.2345, 1));
        expected.add(new Token(LiteralToken.NUMBER, "0.1234", 0.1234, 2));
        expected.add(new Token(EndOfFile.EOF, "", null, 2));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);
    }

    @Test
    void recognizesKeywords() throws IOException {
        Lexer lexer = initializeLexer("src/test/resources/lexer/keywords.space");

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(Keyword.NIL, "nil", null, 1));
        expected.add(new Token(Keyword.FALSE, "false", null, 1));
        expected.add(new Token(Keyword.TRUE, "true", null, 1));
        expected.add(new Token(EndOfFile.EOF, "", null, 1));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);
    }

    @Test
    void recognizesIdentifiers() throws IOException {
        Lexer lexer = initializeLexer("src/test/resources/lexer/identifiers.space");

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(IdentifierToken.IDENTIFIER, "hello", null, 1));
        expected.add(new Token(IdentifierToken.IDENTIFIER, "world", null, 1));
        expected.add(new Token(EndOfFile.EOF, "", null, 1));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);
    }

    @Test
    void failsOnUnexpectedCharacter() throws IOException {
        Lexer lexer = initializeLexer("src/test/resources/lexer/unexpectedCharacters.space");

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(EndOfFile.EOF, "", null, 1));

        List<Token> actual = lexer.scanTokens();

        assertEquals(expected, actual);
    }

    private Lexer initializeLexer(String testFilePath) throws IOException {
        String sourceCode = FileToStringConverter.convert(testFilePath);
        return new Lexer(sourceCode);
    }
}
