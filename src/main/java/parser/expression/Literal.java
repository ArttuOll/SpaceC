package parser.expression;

public class Literal extends Expression {

    public final Object value;

    public Literal(Object value) {
        this.value = value;
    }


    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLiteralExpression(this);
    }
}
