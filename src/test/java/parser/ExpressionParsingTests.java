package parser;

import org.junit.jupiter.api.Test;
import parser.expression.*;
import parser.statement.ExpressionStatement;
import parser.statement.Statement;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static parser.TestUtils.initializeParser;
import static utils.TestExpressions.*;
import static utils.TestTokens.*;

public class ExpressionParsingTests {

    @Test
    void parsesAssignmentExpressions() {
        Parser parser = initializeParser(
                identifierToken,
                colonToken,
                twoToken,
                semicolonToken,
                eofToken
        );

        List<Statement> actual = parser.parse();

        var expression = new AssignmentExpression(identifierToken, two);
        var expected = List.of(new ExpressionStatement(expression));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesEqualityExpressions() {
        Parser parser = initializeParser(oneToken, equalsToken, twoToken, semicolonToken, eofToken);

        List<Statement> actual = parser.parse();

        var expression = new BinaryExpression(one, equalsToken, two);
        var expected = List.of(new ExpressionStatement(expression));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesComparisonExpressions() {
        Parser parser = initializeParser(
            oneToken,
            greaterThanToken,
            twoToken,
            semicolonToken,
            eofToken
        );

        List<Statement> actual = parser.parse();

        var expression = new BinaryExpression(one, greaterThanToken, two);
        var expected = List.of(new ExpressionStatement(expression));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesTermExpressions() {
        Parser parser = initializeParser(oneToken, plusToken, twoToken, semicolonToken, eofToken);

        var actual = parser.parse();

        var expression = new BinaryExpression(one, plusToken, two);
        var expected = List.of(new ExpressionStatement(expression));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesFactorExpressions() {
        Parser parser = initializeParser(
            oneToken,
            multiplyToken,
            twoToken,
            semicolonToken,
            eofToken
        );

        var actual = parser.parse();

        var expression = new BinaryExpression(one, multiplyToken, two);
        var expected = List.of(new ExpressionStatement(expression));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesUnaryExpressions() {
        Parser parser = initializeParser(bangToken, trueToken, semicolonToken, eofToken);

        var actual = parser.parse();

        var expression = new UnaryExpression(bangToken, trueExpression);
        var expected = List.of(new ExpressionStatement(expression));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void parsesPrimaryExpressions() {
        Parser parser = initializeParser(trueToken, semicolonToken, eofToken);

        var actual = parser.parse();

        var expression = new LiteralExpression(true);
        var expected = List.of(new ExpressionStatement(expression));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void quitsIfNoMatchingParenthesisFound() {
        Parser parser = initializeParser(
            leftParenthesisToken,
            oneToken,
            plusToken,
            oneToken,
            semicolonToken,
            eofToken
        );

        List<Statement> actual = parser.parse();

        List<Statement> expected = new ArrayList<>();
        expected.add(null);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void quitsIfTokensDontFitExpressionGrammar() {
        Parser parser = initializeParser(stringToken, eofToken);

        List<Statement> actual = parser.parse();

        List<Statement> expected = new ArrayList<>();
        expected.add(null);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
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
            semicolonToken,
            eofToken
        );

        List<Statement> actual = parser.parse();

        var expression = new BinaryExpression(
            one,
            plusToken,
            new GroupingExpression(new BinaryExpression(one, multiplyToken, two))
        );
        var expected = new ArrayList<Statement>(List.of(new ExpressionStatement(expression)));

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
