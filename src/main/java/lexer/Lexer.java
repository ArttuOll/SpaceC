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
import static lexer.tokenTypes.SingleOrTwoCharacterToken.EQUAL;
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

/**
 * This is the tokenizer of SpaceC.
 * <p>
 * It uses a lookahead of 1 in tokenization. When tokenizing decimal numbers, lookahead of 2 is used
 * briefly in order to see if there are digits after the decimal separator.
 * <p>
 * Whitespace (" ", \r and \t) characters are ignored. Newlines increment the source line marker.
 * <p>
 * Untokenizable characters are reported to the user, but they do not interrupt tokenization.
 */
public class Lexer {

    private final String source;
    private final List<Token> tokens;
    private final ErrorReporter errorReporter;
    private int sourceLine = 1;
    private int currentCharacterOfLexeme = 0;
    private int firstCharacterOfLexeme = 0;

    public Lexer(String source) {
        this.source = source;
        this.errorReporter = new ErrorReporter();
        this.tokens = new ArrayList<>();
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
            case ' ', '\t', '\r' -> {
            }
            case '=' -> addToken(EQUAL);
            case '!' -> addToken(match('=') ? BANG_EQUAL : BANG);
            case '>' -> addToken(match('=') ? GREATER_EQUAL : GREATER);
            case '<' -> addToken(match('=') ? LESS_EQUAL : LESS);
            case '"' -> handleStrings();
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> handleNumbers();
            case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' -> handleIdentifiers();
            default -> errorReporter.error(sourceLine, "lexer_error_unexpected_character",
                String.valueOf(character)
            );
        }
    }

    private void handleIdentifiers() {
        while (isAlphaNumeric(peek())) {
            consumeCharacter();
        }

        String identifier = source.substring(firstCharacterOfLexeme, currentCharacterOfLexeme);
        TokenType tokenType = Keywords.checkKeywordType(identifier);
        addToken(Objects.requireNonNullElse(tokenType, IdentifierToken.IDENTIFIER));
    }

    private boolean isAlphaNumeric(char character) {
        return isAlphabetic(character) || Character.isDigit(character);
    }

    private boolean isAlphabetic(char character) {
        return character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z';
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
