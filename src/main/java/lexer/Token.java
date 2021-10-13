package lexer;

import lexer.tokenTypes.TokenType;

public record Token(TokenType type, String lexeme, Object literal, int line) {

    @Override
    public String toString() {
        return String.format(
            "Type: %s, Lexeme: %s, Literal: %s, Line %s",
            type,
            lexeme,
            literal,
            line
        );
    }
}
