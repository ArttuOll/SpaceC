package interpreter;

import static lexer.tokenTypes.SingleCharacterToken.MINUS;
import static lexer.tokenTypes.SingleCharacterToken.PLUS;
import static lexer.tokenTypes.SingleCharacterToken.SLASH;
import static lexer.tokenTypes.SingleCharacterToken.STAR;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.BANG;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.BANG_EQUAL;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.EQUAL;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.GREATER;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.GREATER_EQUAL;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.LESS;
import static lexer.tokenTypes.SingleOrTwoCharacterToken.LESS_EQUAL;

import java.util.List;
import lexer.Token;
import lexer.tokenTypes.TokenType;
import parser.expression.BinaryExpression;
import parser.expression.Expression;
import parser.expression.ExpressionVisitor;
import parser.expression.GroupingExpression;
import parser.expression.LiteralExpression;
import parser.expression.UnaryExpression;
import parser.statement.ExpressionStatement;
import parser.statement.PrintStatement;
import parser.statement.Statement;
import parser.statement.StatementVisitor;
import utils.ErrorReporter;
import utils.PropertiesReader;

public class Interpreter implements ExpressionVisitor<Object>, StatementVisitor<Void> {

    private final PropertiesReader propertiesReader;
    private final ErrorReporter errorReporter;

    public Interpreter() {
        this.propertiesReader = new PropertiesReader("src/main/resources/strings.properties");
        this.errorReporter = new ErrorReporter();
    }

    public void interpret(List<Statement> statements) {
        try {
            for (Statement statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            errorReporter.runtimeError(error);
        }
    }

    private void execute(Statement statement) {
        statement.accept(this);
    }

    // TODO: paivita testit kayttamaan uutta interpret-metodia.
    public Object interpretRaw(Expression expression) {
        try {
            return evaluate(expression);
        } catch (RuntimeError error) {
            errorReporter.runtimeError(error);
        }
        return null;
    }


    private String stringify(Object value) {
        if (value == null) {
            return "nil";
        } else if (value instanceof Double) {
            return stringifyDouble(value);
        } else {
            return value.toString();
        }
    }

    private String stringifyDouble(Object value) {
        var stringValue = value.toString();
        if (stringValue.endsWith(".0")) {
            return stringValue.substring(0, stringValue.length() - 2);
        } else {
            return stringValue;
        }
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement statement) {
        evaluate(statement.expression);
        return null;
    }

    @Override
    public Void visitPrintStatement(PrintStatement statement) {
        Object value = evaluate(statement.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Object visitBinaryExpression(BinaryExpression expression) {
        Object right = evaluate(expression.right);
        Object left = evaluate(expression.left);

        return evaluateBinaryExpression(left, right, expression.operator);
    }

    private Object evaluateBinaryExpression(Object left, Object right, Token operator) {
        TokenType operatorType = operator.type();

        if (operatorType == PLUS) {
            checkNumberOperands(operator, right, left);
            return (double) left + (double) right;

        } else if (operatorType == MINUS) {
            checkNumberOperands(operator, right, left);
            return (double) left - (double) right;

        } else if (operatorType == STAR) {
            checkNumberOperands(operator, right, left);
            return (double) left * (double) right;
        } else if (operatorType == SLASH) {
            checkNumberOperands(operator, right, left);
            checkDivisionByZero(operator, right);
            return (double) left / (double) right;

        } else if (operatorType == GREATER) {
            checkNumberOperands(operator, right, left);
            return (double) left > (double) right;

        } else if (operatorType == GREATER_EQUAL) {
            checkNumberOperands(operator, right, left);
            return (double) left >= (double) right;

        } else if (operatorType == LESS) {
            checkNumberOperands(operator, right, left);
            return (double) left < (double) right;

        } else if (operatorType == LESS_EQUAL) {
            checkNumberOperands(operator, right, left);
            return (double) left <= (double) right;

        } else if (operatorType == EQUAL) {
            return isEqual(right, left);

        } else if (operatorType == BANG_EQUAL) {
            return !isEqual(right, left);
        }
        return null;
    }

    private void checkNumberOperands(Token operator, Object leftOperand, Object rightOperand) {
        if (leftOperand instanceof Double && rightOperand instanceof Double) {
            return;
        }
        throw new RuntimeError(
            operator,
            propertiesReader.getString("interpreter_error_non_numeric_operands"),
            String.format(
                "%s %s %s",
                leftOperand.toString(),
                operator.lexeme(),
                rightOperand.toString()
            )
        );
    }

    private void checkDivisionByZero(Token operator, Object divider) {
        if (((double) divider == 0.0)) {
            throw new RuntimeError(
                operator,
                propertiesReader.getString("interpreter_error_division_by_zero"),
                String.format("%s %s ", operator.lexeme(), divider)
            );
        }
    }

    private boolean isEqual(Object right, Object left) {
        if (right == null && left == null) {
            return true;
        } else if (right == null) {
            return false;
        } else {
            return right.equals(left);
        }
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression expression) {
        Object right = evaluate(expression.right);

        Token operator = expression.operator;
        if (operator.type() == MINUS) {
            checkNumberOperand(operator, right);
            return -(double) right;
        } else if (operator.type() == BANG) {
            return !getTruthyValue(right);
        }
        return null;
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) {
            return;
        }
        throw new RuntimeError(
            operator,
            propertiesReader.getString("interpreter_error_non_numeric_operand"),
            String.format("%s %s ", operator.lexeme(), operand.toString())
        );
    }

    private Object evaluate(Expression expression) {
        return expression.accept(this);
    }

    /**
     * In SpaceC, nil and false are falsy, everything else is truthy.
     *
     * @param value The object, truthiness of which is evaluated.
     * @return The truthiness of the given argument. False, if it was false or nil, otherwise true.
     */
    private boolean getTruthyValue(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        return true;
    }

    @Override
    public Object visitGroupingExpression(GroupingExpression expression) {
        return evaluate(expression.expression);
    }

    @Override
    public Object visitLiteralExpression(LiteralExpression expression) {
        return expression.value;
    }
}
