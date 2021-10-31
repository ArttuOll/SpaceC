package parser.expression.util;

import java.util.Arrays;
import parser.expression.Binary;
import parser.expression.Expression;
import parser.expression.ExpressionVisitor;
import parser.expression.Grouping;
import parser.expression.Literal;
import parser.expression.Unary;

public class AbstractSyntaxTreePrinter implements ExpressionVisitor<String> {

    public String print(
        Expression expression
    ) {
        return expression.accept(this);
    }

    @Override
    public String visitBinaryExpression(Binary expression) {
        return toSchemeString(expression.operator.lexeme(), expression.left, expression.right);
    }

    @Override
    public String visitUnaryExpression(Unary expression) {
        return toSchemeString(expression.operator.lexeme(), expression.right);
    }

    @Override
    public String visitGroupingExpression(Grouping expression) {
        return toSchemeString("group", expression.expression);
    }

    @Override
    public String visitLiteralExpression(Literal expression) {
        return expression.value == null ? "nil" : expression.value.toString();
    }

    private String toSchemeString(String name, Expression... expressions) {
        var stringBuilder = new StringBuilder();

        stringBuilder.append("(").append(name);
        Arrays.stream(expressions).forEach((Expression expression) -> {
            stringBuilder.append(" ");
            stringBuilder.append(expression.accept(this));
        });
        stringBuilder.append(")");

        return stringBuilder.toString();
    }
}
