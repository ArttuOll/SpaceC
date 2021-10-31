package parser.expression;

public interface ExpressionVisitor<R> {

    R visitBinaryExpression(Binary expression);

    R visitUnaryExpression(Unary expression);

    R visitGroupingExpression(Grouping expression);

    R visitLiteralExpression(Literal expression);

}