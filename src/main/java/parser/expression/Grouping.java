package parser.expression;

public class Grouping extends Expression {

    public final Expression expression;

    public Grouping(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitGroupingExpression(this);
    }
}
