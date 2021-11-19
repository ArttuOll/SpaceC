package parser.expression;

public interface ExpressionVisitor<R> {

    R visitBinaryExpression(BinaryExpression expression);

    R visitUnaryExpression(UnaryExpression expression);

    R visitGroupingExpression(GroupingExpression expression);

    R visitLiteralExpression(LiteralExpression expression);

    R visitVariableExpression(VariableExpression expression);

}
