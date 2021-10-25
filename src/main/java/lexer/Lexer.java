package lexer;

import static lexer.tokenTypes.SingleCharacterToken.COLON;
import static lexer.tokenTypes.SingleCharacterToken.COMMA;
import static lexer.tokenTypes.SingleCharacterToken.DOT;
import static lexer.tokenTypes.SingleCharacterToken.LEFT_BRACE;
import static lexer.tokenTypes.SingleCharacterToken.LEFT_PARENTHESIS;
import static lexer.tokenTypes.SingleCharacterToken.MINUS;
import static lexer.tokenTypes.SingleCharacterToken.PLUS;
import static lexer.tokenTypes.SingleCharacterToken.RIGHT_BRACE;
import static lexer.tokenTypes.SingleCharacterToken.RIGHT_PARENTHESIS;
import static lexer.tokenTypes.SingleCharacterToken.SEMICOLON;
import static lexer.tokenTypes.SingleCharacterToken.SLASH;
import static lexer.tokenTypes.SingleCharacterToken.STAR;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.BANG;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.BANG_EQUAL;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.GREATER;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.GREATER_EQUAL;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.LESS;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.LESS_EQUAL;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lexer.tokenTypes.EndOfFile;
import lexer.tokenTypes.IdentifierToken;
import lexer.tokenTypes.LiteralToken;
import lexer.tokenTypes.TokenType;
import utils.ErrorReporter;
import utils.EscapeCharacter;

public class Lexer {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private final ErrorReporter errorReporter;
    private int sourceLine = 1;
    private int currentCharacterOfLexeme = 0;
    private int firstCharacterOfLexeme = 0;

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

    private void scanToken() {
        char character = consumeCharacter();
        scanSingleCharacterToken(character);
        scanSingleOrTwoCharacterToken(character);
        scanLiteralToken(character);
        scanIdentifier(character);
    }

    private void scanSingleCharacterToken(char character) {
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
            case ':' -> addToken(COLON);
            case '*' -> addToken(STAR);
            case '/' -> handleSlash();
            case '\n' -> sourceLine++;
            case ' ', '\t', 'r' -> {
            }
        }
    }

    private void scanSingleOrTwoCharacterToken(char character) {
        switch (character) {
            case '!' -> addToken(match('=') ? BANG_EQUAL : BANG);
            case '>' -> addToken(match('=') ? GREATER_EQUAL : GREATER);
            case '<' -> addToken(match('=') ? LESS_EQUAL : LESS);
        }
    }

    private void scanLiteralToken(char character) {
        switch (character) {
            case '"' -> handleStrings();
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> handleNumbers();
        }
    }

    private void scanIdentifier(char character) {
        if (isAlphabetic(character)) {
            handleIdentifiers();
        } else {
            errorReporter.error(sourceLine, "lexer_error_unexpected_character");
        }
    }

    private boolean isAlphabetic(char character) {
        return character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z';
    }

    private boolean isAlphaNumeric(char character) {
        return isAlphabetic(character) || Character.isDigit(character);
    }

    private void handleIdentifiers() {
        while (isAlphaNumeric(peek())) {
            consumeCharacter();
        }

        String identifier = source.substring(firstCharacterOfLexeme, currentCharacterOfLexeme);
        TokenType tokenType = Keywords.checkKeywordType(identifier);
        addToken(Objects.requireNonNullElse(tokenType, IdentifierToken.IDENTIFIER));
    }

    private boolean match(char expected) {
        if (!charactersLeft() || characterNotExpected(expected)) {
            return false;
        }

        currentCharacterOfLexeme++;
        return true;
    }

    private boolean characterNotExpected(char character) {
        return source.charAt(currentCharacterOfLexeme) != character;
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String lexeme = source.substring(firstCharacterOfLexeme, currentCharacterOfLexeme);
        tokens.add(new Token(type, lexeme, literal, sourceLine));
    }

    private void handleSlash() {
        if (match('/')) {
            while (peek() != EscapeCharacter.NEWLINE.value && charactersLeft()) {
                consumeCharacter();
            }
        } else {
            addToken(SLASH);
        }
    }

    private void handleStrings() {
        while (peek() != '"' && charactersLeft()) {
            if (peek() == EscapeCharacter.NEWLINE.value) {
                sourceLine++;
            }
            consumeCharacter();
        }

        if (!charactersLeft()) {
            errorReporter.error(sourceLine, "lexer_error_unterminated_string");
            return;
        }

        // Consume the terminating double quote
        consumeCharacter();

        var string = getStringValue();
        addToken(LiteralToken.STRING, string);
    }

    private void handleNumbers() {
        while (nextCharacterIsDigit()) {
            consumeCharacter();
        }

        if (peek() == '.' && Character.isDigit(doublePeek())) {
            // Consume the decimal separator
            consumeCharacter();

            while (nextCharacterIsDigit()) {
                consumeCharacter();
            }
        }

        addToken(
            LiteralToken.NUMBER,
            Double.parseDouble(source.substring(firstCharacterOfLexeme, currentCharacterOfLexeme))
        );
    }

    private boolean nextCharacterIsDigit() {
        return Character.isDigit(peek());
    }

    private char doublePeek() {
        if (currentCharacterOfLexeme + 1 >= source.length()) {
            return EscapeCharacter.NULL.value;
        }
        return source.charAt(currentCharacterOfLexeme + 1);
    }

    private char peek() {
        if (!charactersLeft()) {
            return EscapeCharacter.NULL.value;
        }
        return source.charAt(currentCharacterOfLexeme);
    }

    private char consumeCharacter() {
        char character = source.charAt(currentCharacterOfLexeme);
        currentCharacterOfLexeme++;
        return character;
    }

    private boolean charactersLeft() {
        return currentCharacterOfLexeme < source.length();
    }

    private String getStringValue() {
        // Note, that escape characters are not supported (not escaped here).
        return source.substring(firstCharacterOfLexeme + 1, currentCharacterOfLexeme - 1);
    }
}
