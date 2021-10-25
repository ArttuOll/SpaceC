package lexer;

import java.util.HashMap;
import lexer.tokenTypes.Keyword;
import lexer.tokenTypes.TokenType;

public class Keywords {

    private static final HashMap<String, Keyword> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", Keyword.AND);
        keywords.put("class", Keyword.CLASS);
        keywords.put("else", Keyword.ELSE);
        keywords.put("false", Keyword.FALSE);
        keywords.put("fun", Keyword.FUN);
        keywords.put("for", Keyword.FOR);
        keywords.put("if", Keyword.IF);
        keywords.put("nil", Keyword.NIL);
        keywords.put("or", Keyword.OR);
        keywords.put("print", Keyword.PRINT);
        keywords.put("return", Keyword.RETURN);
        keywords.put("super", Keyword.SUPER);
        keywords.put("this", Keyword.THIS);
        keywords.put("true", Keyword.TRUE);
        keywords.put("while", Keyword.WHILE);
    }

    public static TokenType checkKeywordType(String keyword) {
        return keywords.get(keyword);
    }
}
