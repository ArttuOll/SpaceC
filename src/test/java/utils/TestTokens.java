package utils;

import lexer.Token;
import lexer.tokenTypes.EndOfFile;
import lexer.tokenTypes.IdentifierToken;
import lexer.tokenTypes.Keyword;
import lexer.tokenTypes.LiteralToken;
import lexer.tokenTypes.SingleCharacterToken;
import lexer.tokenTypes.SingleOrTwoCharacterToken;

public class TestTokens {

    public static final Token oneToken = new Token(LiteralToken.NUMBER, "1", 1.0, 1);
    public static final Token twoToken = new Token(LiteralToken.NUMBER, "2", 2.0, 1);
    public static final Token equalsToken = new Token(
        SingleOrTwoCharacterToken.EQUAL,
        "=",
        null,
        1
    );
    public static final Token plusToken = new Token(SingleCharacterToken.PLUS, "+", null, 1);
    public static final Token minusToken = new Token(SingleCharacterToken.MINUS, "-", null, 1);
    public static final Token bangToken = new Token(SingleOrTwoCharacterToken.BANG, "!", null, 1);
    public static final Token multiplyToken = new Token(SingleCharacterToken.STAR, "*", null, 1);
    public static final Token slashToken = new Token(SingleCharacterToken.SLASH, "/", null, 1);
    public static final Token greaterThanToken = new Token(
        SingleOrTwoCharacterToken.GREATER,
        ">",
        null,
        1
    );
    public static final Token trueToken = new Token(Keyword.TRUE, "true", null, 1);
    public static final Token leftParenthesisToken = new Token(
        SingleCharacterToken.LEFT_PARENTHESIS,
        "(",
        null,
        1
    );
    public static final Token rightParenthesisToken = new Token(
        SingleCharacterToken.RIGHT_PARENTHESIS,
        ")",
        null,
        1
    );
    public static final Token eofToken = new Token(EndOfFile.EOF, "", null, 1);
    public static final Token stringToken = new Token(IdentifierToken.IDENTIFIER, "asdf", null, 1);
    public static final Token semicolonToken = new Token(
        SingleCharacterToken.SEMICOLON,
        ";",
        null,
        1
    );
}
