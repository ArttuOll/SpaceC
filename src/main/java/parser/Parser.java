package parser;

import static lexer.tokenTypes.Keyword.CLASS;
import static lexer.tokenTypes.Keyword.FALSE;
import static lexer.tokenTypes.Keyword.FOR;
import static lexer.tokenTypes.Keyword.FUN;
import static lexer.tokenTypes.Keyword.IF;
import static lexer.tokenTypes.Keyword.NIL;
import static lexer.tokenTypes.Keyword.PRINT;
import static lexer.tokenTypes.Keyword.RETURN;
import static lexer.tokenTypes.Keyword.TRUE;
import static lexer.tokenTypes.Keyword.WHILE;
import static lexer.tokenTypes.LiteralToken.NUMBER;
import static lexer.tokenTypes.LiteralToken.STRING;
import static lexer.tokenTypes.SingleCharacterToken.LEFT_PARENTHESIS;
import static lexer.tokenTypes.SingleCharacterToken.MINUS;
import static lexer.tokenTypes.SingleCharacterToken.PLUS;
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

import java.util.Arrays;
import java.util.List;
import lexer.Token;
import lexer.tokenTypes.EndOfFile;
import lexer.tokenTypes.TokenType;
import parser.expression.BinaryExpression;
import parser.expression.Expression;
import parser.expression.GroupingExpression;
import parser.expression.LiteralExpression;
import parser.expression.UnaryExpression;
import utils.ErrorReporter;
import utils.PropertiesReader;

/**
 * This is the parser of SpaceC. It is a recursive descent parser, a type of top-down parser,
 * meaning we parse the syntax tree from the root towards the leaves.
 * <p>
 * Note, how the most grammatical rules are implemented. In the beginning of the method we call the
 * rule of the expression with next highest precedence and then conditionally process the current
 * rule. This way the rules always match the current expression or anything of higher precedence.
 */
public class Parser {

    private final List<Token> tokens;
    private final ErrorReporter errorReporter;
    private final PropertiesReader propertiesReader;
    private int nextTokenPointer;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.nextTokenPointer = 0;
        this.errorReporter = new ErrorReporter();
        this.propertiesReader = new PropertiesReader("src/main/resources/strings.properties");
    }

    public Expression parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null;
        }
    }

    private Expression expression() {
        return equality();
    }

    private Expression equality() {
        Expression expression = comparison();

        while (matchNextTokenWith(EQUAL, BANG_EQUAL)) {
            Token operator = previous();
            Expression right = comparison();
            expression = new BinaryExpression(expression, operator, right);
        }
        return expression;
    }

    private Expression comparison() {
        Expression expression = term();

        while (matchNextTokenWith(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expression right = term();
            expression = new BinaryExpression(expression, operator, right);
        }
        return expression;
    }

    private Expression term() {
        Expression expression = factor();

        while (matchNextTokenWith(PLUS, MINUS)) {
            Token operator = previous();
            Expression right = factor();
            expression = new BinaryExpression(expression, operator, right);
        }
        return expression;
    }

    private Expression factor() {
        Expression expression = unary();

        while (matchNextTokenWith(STAR, SLASH)) {
            Token operator = previous();
            Expression right = unary();
            expression = new BinaryExpression(expression, operator, right);
        }
        return expression;
    }

    private Expression unary() {
        if (matchNextTokenWith(BANG, MINUS)) {
            Token operator = previous();
            Expression right = unary();

            return new UnaryExpression(operator, right);
        }
        return primary();
    }

    private Expression primary() {
        if (matchNextTokenWith(FALSE)) {
            return new LiteralExpression(false);
        } else if (matchNextTokenWith(TRUE)) {
            return new LiteralExpression(true);
        } else if (matchNextTokenWith(NIL)) {
            return new LiteralExpression(null);
        } else if (matchNextTokenWith(NUMBER, STRING)) {
            Token matchedToken = previous();
            return new LiteralExpression(matchedToken.literal());
        } else if (matchNextTokenWith(LEFT_PARENTHESIS)) {
            Expression expression = expression();
            consume(
                RIGHT_PARENTHESIS,
                propertiesReader.getString("parser_error_no_closing_parenthesis")
            );
            return new GroupingExpression(expression);
        }
        throw dispatchParseError(
            peek(),
            propertiesReader.getString("parser_error_expected_expression")
        );
    }

    private boolean matchNextTokenWith(TokenType... types) {
        for (TokenType tokenType : types) {
            if (checkNextTokenMatches(tokenType)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token consume(TokenType type, String message) {
        if (checkNextTokenMatches(type)) {
            return advance();
        }
        throw dispatchParseError(peek(), message);
    }

    private boolean checkNextTokenMatches(TokenType type) {
        if (isAtEnd()) {
            return false;
        }
        Token nextToken = peek();
        return nextToken.type() == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            nextTokenPointer++;
        }
        return previous();
    }

    private ParseError dispatchParseError(Token token, String error) {
        errorReporter.error(token, error);
        return new ParseError();
    }

    private boolean isAtEnd() {
        Token nextToken = peek();
        return nextToken.type() == EndOfFile.EOF;
    }

    private Token peek() {
        return tokens.get(nextTokenPointer);
    }

    private Token previous() {
        return tokens.get(nextTokenPointer - 1);
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            discardTokensUntilStatementBoundaryFound();
        }
    }

    private void discardTokensUntilStatementBoundaryFound() {
        Token currentProcessedToken = previous();
        if (currentProcessedToken.type() == SEMICOLON) {
            return;
        }

        Token nextToken = peek();
        var statementBorderIndicatingTokens = Arrays.asList(
            new TokenType[]{CLASS, FUN, FOR, IF, WHILE, PRINT, RETURN});
        if (statementBorderIndicatingTokens.contains(nextToken.type())) {
            return;
        }

        advance();
    }

    private static class ParseError extends RuntimeException {

    }
}
