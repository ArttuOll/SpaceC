package parser.expression.util;

import java.util.Arrays;
import parser.expression.AssignmentExpression;
import parser.expression.BinaryExpression;
import parser.expression.Expression;
import parser.expression.ExpressionVisitor;
import parser.expression.GroupingExpression;
import parser.expression.LiteralExpression;
import parser.expression.UnaryExpression;
import parser.expression.VariableExpression;

public class AbstractSyntaxTreePrinter implements ExpressionVisitor<String> {

    public String print(
        Expression expression
    ) {
        return expression.accept(this);
    }

    @Override
    public String visitBinaryExpression(BinaryExpression expression) {
        return toSchemeString(expression.operator.lexeme(), expression.left, expression.right);
    }

    @Override
    public String visitUnaryExpression(UnaryExpression expression) {
        return toSchemeString(expression.operator.lexeme(), expression.right);
    }

    @Override
    public String visitGroupingExpression(GroupingExpression expression) {
        return toSchemeString("group", expression.expression);
    }

    @Override
    public String visitLiteralExpression(LiteralExpression expression) {
        return expression.value == null ? "nil" : expression.value.toString();
    }

    @Override
    public String visitVariableExpression(VariableExpression expression) {
        return expression.name.lexeme();
    }

    @Override
    public String visitAssignmentExpression(AssignmentExpression expression) {
        return expression.name + " " + expression.value;
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
