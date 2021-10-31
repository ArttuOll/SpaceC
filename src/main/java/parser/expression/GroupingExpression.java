package parser.expression;

public class GroupingExpression extends Expression {

    public final Expression expression;

    public GroupingExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitGroupingExpression(this);
    }
}
