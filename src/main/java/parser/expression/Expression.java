package parser.expression;

public abstract class Expression {

    public abstract <R> R accept(ExpressionVisitor<R> visitor);
}
