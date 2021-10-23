package lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lexer.tokenTypes.EndOfFile;
import lexer.tokenTypes.SingleCharacterToken;
import lexer.tokenTypes.SingleOrTwoCharacterToken;
import org.junit.jupiter.api.Test;
import utils.FileToStringConverter;

public class TokenizationTests {

    @Test
    void recognizesSingleCharacterTokens() throws IOException {
        String sourceCode = FileToStringConverter.convert(
            "src/test/resources/singleCharacterTokens.space");
        Lexer lexer = new Lexer(sourceCode);

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
        String sourceCode = FileToStringConverter.convert(
            "src/test/resources/singleOrTwoCharacterTokens.space");
        Lexer lexer = new Lexer(sourceCode);

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(SingleOrTwoCharacterToken.GREATER, ">", null, 1));
        expected.add(new Token(SingleOrTwoCharacterToken.GREATER_EQUAL, ">=", null, 1));
        expected.add(new Token(SingleOrTwoCharacterToken.LESS, "<", null, 1));
        expected.add(new Token(SingleOrTwoCharacterToken.LESS_EQUAL, "<=", null, 1));
        expected.add(new Token(SingleOrTwoCharacterToken.BANG, "!", null, 1));
        expected.add(new Token(SingleOrTwoCharacterToken.BANG_EQUAL, "!=", null, 1));
        expected.add(new Token(EndOfFile.EOF, "", null, 1));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);
    }

    @Test
    void recognizesComments() throws IOException {
        String sourceCode = FileToStringConverter.convert(
            "src/test/resources/comments.space");
        Lexer lexer = new Lexer(sourceCode);

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(SingleCharacterToken.LEFT_BRACE, "{", null, 1));
        expected.add(new Token(SingleCharacterToken.RIGHT_BRACE, "}", null, 1));
        expected.add(new Token(EndOfFile.EOF, "", null, 1));

        List<Token> actual = lexer.scanTokens();
        assertEquals(expected, actual);

    }

    @Test
    void failsOnUnexpectedCharacter() throws IOException {
        String sourceCode = FileToStringConverter.convert(
            "src/test/resources/unexpectedCharacters.space");
        Lexer lexer = new Lexer(sourceCode);

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(EndOfFile.EOF, "", null, 1));

        List<Token> actual = lexer.scanTokens();

        assertEquals(expected, actual);
    }
}
