package lexer;

import static lexer.tokenTypes.SingleCharacterToken.COMMA;
import static lexer.tokenTypes.SingleCharacterToken.DOT;
import static lexer.tokenTypes.SingleCharacterToken.LEFT_BRACE;
import static lexer.tokenTypes.SingleCharacterToken.LEFT_PARENTHESIS;
import static lexer.tokenTypes.SingleCharacterToken.MINUS;
import static lexer.tokenTypes.SingleCharacterToken.PLUS;
import static lexer.tokenTypes.SingleCharacterToken.RIGHT_BRACE;
import static lexer.tokenTypes.SingleCharacterToken.RIGHT_PARENTHESIS;
import static lexer.tokenTypes.SingleCharacterToken.SEMICOLON;
import static lexer.tokenTypes.SingleCharacterToken.STAR;

import java.util.ArrayList;
import java.util.List;
import lexer.tokenTypes.EndOfFile;
import lexer.tokenTypes.TokenType;
import utils.ErrorReporter;

public class Lexer {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int currentCharacterOfLexeme = 0;
    private final int sourceLine = 1;
    private int firstCharacterOfLexeme = 0;
    private final ErrorReporter errorReporter;

    public Lexer(String source) {
        this.source = source;
        this.errorReporter = new ErrorReporter();
    }

    public List<Token> scanTokens() {
        while (charactersLeft()) {
            firstCharacterOfLexeme = currentCharacterOfLexeme;
            scanToken();
        }

        tokens.add(new Token(EndOfFile.EOF, "", null, sourceLine));
        return tokens;
    }

    private boolean charactersLeft() {
        return !(currentCharacterOfLexeme >= source.length());
    }

    private void scanToken() {
        char character = consumeCharacter();
        currentCharacterOfLexeme++;
        switch (character) {
            case '(' -> addToken(LEFT_PARENTHESIS);
            case ')' -> addToken(RIGHT_PARENTHESIS);
            case '{' -> addToken(LEFT_BRACE);
            case '}' -> addToken(RIGHT_BRACE);
            case ',' -> addToken(COMMA);
            case '.' -> addToken(DOT);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case ';' -> addToken(SEMICOLON);
            case '*' -> addToken(STAR);
            default -> errorReporter.error(sourceLine, "lexer_error_unexpected_character");
        }
    }

    private char consumeCharacter() {
        return source.charAt(currentCharacterOfLexeme);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String lexeme = source.substring(firstCharacterOfLexeme, currentCharacterOfLexeme);
        tokens.add(new Token(type, lexeme, literal, sourceLine));
    }
}
