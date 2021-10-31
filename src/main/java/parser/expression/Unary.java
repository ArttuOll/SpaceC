package parser.expression;

import lexer.Token;

public class Unary extends Expression {

    public final Token operator;
    public final Expression right;

    public Unary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitUnaryExpression(this);
    }
}
