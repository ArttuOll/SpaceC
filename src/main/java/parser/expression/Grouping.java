package parser.expression;

class Grouping extends Expression {

    final Expression expression;

    Grouping(Expression expression) {
        this.expression = expression;
    }

    @Override
    <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitGroupingExpression(this);
    }
}
