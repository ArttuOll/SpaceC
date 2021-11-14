package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestExpressions.one;
import static utils.TestExpressions.three;
import static utils.TestExpressions.two;
import static utils.TestExpressions.zero;
import static utils.TestTokens.minusToken;
import static utils.TestTokens.multiplyToken;
import static utils.TestTokens.plusToken;
import static utils.TestTokens.slashToken;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.expression.BinaryExpression;
import parser.expression.Expression;
import parser.expression.GroupingExpression;
import parser.expression.UnaryExpression;

public class ExpressionInterpretationTests {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final ByteArrayOutputStream error = new ByteArrayOutputStream();
    private final PrintStream systemOut = System.out;
    private final PrintStream systemError = System.err;
    private Interpreter interpreter;

    @BeforeEach
    void setup() {
        System.setOut(new PrintStream(output));
        System.setOut(new PrintStream(error));
        interpreter = new Interpreter();
    }

    @AfterEach
    void restoration() {
        System.setOut(systemOut);
        System.setOut(systemError);
    }

    @Test
    void correctAddition() {
        Expression onePlusTwo = new BinaryExpression(one, plusToken, two);
        Object actual = interpreter.interpretRaw(onePlusTwo);
        Double expected = 3.0;
        assertEquals(expected, actual);
    }

    @Test
    void correctAdditionWithNegativeNumbers() {
        Expression minusOnePlusTwo = new BinaryExpression(
            new UnaryExpression(minusToken, one),
            plusToken,
            two
        );
        Object actual = interpreter.interpretRaw(minusOnePlusTwo);
        Double expected = 1.0;
        assertEquals(expected, actual);
    }

    @Test
    void correctAdditionWithZeros() {
        Expression minusOnePlusZero = new BinaryExpression(
            new UnaryExpression(minusToken, one),
            plusToken,
            zero
        );
        Object actual = interpreter.interpretRaw(minusOnePlusZero);
        Double expected = -1.0;
        assertEquals(expected, actual);
    }

    @Test
    void correctSubtraction() {
        Expression oneMinusTwo = new BinaryExpression(one, minusToken, two);
        Object actual = interpreter.interpretRaw(oneMinusTwo);
        Double expected = -1.0;
        assertEquals(expected, actual);
    }

    @Test
    void correctSubtractionWithNegativeNumbers() {
        Expression minusOneMinusTwo = new BinaryExpression(
            new UnaryExpression(minusToken, one),
            minusToken,
            two
        );
        Object actual = interpreter.interpretRaw(minusOneMinusTwo);
        Double expected = -3.0;
        assertEquals(expected, actual);
    }

    @Test
    void correctSubtractionWithZeros() {
        Expression minusOneMinusZero = new BinaryExpression(
            new UnaryExpression(minusToken, one),
            minusToken,
            zero
        );
        Object actual = interpreter.interpretRaw(minusOneMinusZero);
        Double expected = -1.0;
        assertEquals(expected, actual);
    }

    @Test
    void correctMultiplication() {
        Expression oneMultipliedByTwo = new BinaryExpression(one, multiplyToken, two);
        Object actual = interpreter.interpretRaw(oneMultipliedByTwo);
        Double expected = 2.0;
        assertEquals(expected, actual);
    }

    @Test
    void correctMultiplicationWithNegativeNumbers() {
        Expression minusOneMultipliedByTwo = new BinaryExpression(new UnaryExpression(
            minusToken,
            one
        ), multiplyToken, two);
        Object actual = interpreter.interpretRaw(minusOneMultipliedByTwo);
        Double expected = -2.0;
        assertEquals(expected, actual);
    }

    @Test
    void correctMultiplicationWithZeros() {
        Expression minusOneMultipliedByZero = new BinaryExpression(new UnaryExpression(
            minusToken,
            one
        ), multiplyToken, zero);
        Object actual = interpreter.interpretRaw(minusOneMultipliedByZero);
        Double expected = -0.0;
        assertEquals(expected, actual);
    }

    @Test
    void correctDivision() {
        Expression oneDividedByTwo = new BinaryExpression(one, slashToken, two);
        Object actual = interpreter.interpretRaw(oneDividedByTwo);
        Double expected = 0.5;
        assertEquals(expected, actual);
    }

    @Test
    void correctDivisionWithNegativeNumbers() {
        Expression minusOneDividedByTwo = new BinaryExpression(
            new UnaryExpression(minusToken, one),
            slashToken,
            two
        );
        Object actual = interpreter.interpretRaw(minusOneDividedByTwo);
        Double expected = -0.5;
        assertEquals(expected, actual);
    }

    @Test
    void correctOperatorPrecedence() {
        Expression minusOneDividedByThreePlusTwoMinusOne = new BinaryExpression(
            new UnaryExpression(
                minusToken,
                one
            ),
            slashToken,
            new GroupingExpression(new BinaryExpression(
                three,
                plusToken,
                new GroupingExpression(new BinaryExpression(two, minusToken, one))
            ))
        );
        Object actual = interpreter.interpretRaw(minusOneDividedByThreePlusTwoMinusOne);
        Double expected = -0.25;
        assertEquals(expected, actual);
    }

    // @Test
    // void throwsErrorOnDivisionByZero() {
    //     Expression oneDividedByZero = new BinaryExpression(one, slashToken, zero);
    //     Object actual = interpreter.interpretRaw(oneDividedByZero);
    //     assertNull(actual);
    // }
}
