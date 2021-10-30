package parser.expression;

class Literal extends Expression {

    final Object value;

    Literal(Object value) {
        this.value = value;
    }


    @Override
    <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitLiteralExpression(this);
    }
}
