package parser;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.TestExpressions.one;
import static utils.TestExpressions.trueExpression;
import static utils.TestExpressions.two;
import static utils.TestTokens.bangToken;
import static utils.TestTokens.eofToken;
import static utils.TestTokens.equalsToken;
import static utils.TestTokens.greaterThanToken;
import static utils.TestTokens.leftParenthesisToken;
import static utils.TestTokens.multiplyToken;
import static utils.TestTokens.oneToken;
import static utils.TestTokens.plusToken;
import static utils.TestTokens.rightParenthesisToken;
import static utils.TestTokens.stringToken;
import static utils.TestTokens.trueToken;
import static utils.TestTokens.twoToken;

import java.util.Arrays;
import lexer.Token;
import org.junit.jupiter.api.Test;
import parser.expression.BinaryExpression;
import parser.expression.Expression;
import parser.expression.GroupingExpression;
import parser.expression.LiteralExpression;
import parser.expression.UnaryExpression;

public class ExpressionParsingTests {

    @Test
    void parsesEqualityExpressions() {
        Parser parser = initializeParser(oneToken, equalsToken, twoToken, eofToken);

        BinaryExpression actual = (BinaryExpression) parser.parse();

        var expected = new BinaryExpression(one, equalsToken, two);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesComparisonExpressions() {
        Parser parser = initializeParser(oneToken, greaterThanToken, twoToken, eofToken);

        BinaryExpression actual = (BinaryExpression) parser.parse();

        var expected = new BinaryExpression(one, greaterThanToken, two);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesTermExpressions() {
        Parser parser = initializeParser(oneToken, plusToken, twoToken, eofToken);

        BinaryExpression actual = (BinaryExpression) parser.parse();

        var expected = new BinaryExpression(one, plusToken, two);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesFactorExpressions() {
        Parser parser = initializeParser(oneToken, multiplyToken, twoToken, eofToken);

        BinaryExpression actual = (BinaryExpression) parser.parse();

        var expected = new BinaryExpression(one, multiplyToken, two);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesUnaryExpressions() {
        Parser parser = initializeParser(bangToken, trueToken, eofToken);

        UnaryExpression actual = (UnaryExpression) parser.parse();

        var expected = new UnaryExpression(bangToken, trueExpression);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesPrimaryExpressions() {
        Parser parser = initializeParser(trueToken, eofToken);

        LiteralExpression actual = (LiteralExpression) parser.parse();

        var expected = new LiteralExpression(true);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void quitsIfNoMatchingParenthesisFound() {
        Parser parser = initializeParser(
            leftParenthesisToken,
            oneToken,
            plusToken,
            oneToken,
            eofToken
        );

        LiteralExpression actual = (LiteralExpression) parser.parse();

        assertThat(actual).usingRecursiveComparison().isEqualTo(null);
    }

    @Test
    void quitsIfTokensDontFitExpressionGrammar() {
        Parser parser = initializeParser(stringToken, eofToken);

        LiteralExpression actual = (LiteralExpression) parser.parse();

        assertThat(actual).usingRecursiveComparison().isEqualTo(null);
    }

    @Test
    void parsesMixedExpressions() {
        Parser parser = initializeParser(
            oneToken,
            plusToken,
            leftParenthesisToken,
            oneToken,
            multiplyToken,
            twoToken,
            rightParenthesisToken,
            eofToken
        );

        Expression actual = parser.parse();

        var expected = new BinaryExpression(
            one,
            plusToken,
            new GroupingExpression(new BinaryExpression(one, multiplyToken, two))
        );

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    private Parser initializeParser(Token... tokens) {
        return new Parser(Arrays.asList(tokens));
    }
}
