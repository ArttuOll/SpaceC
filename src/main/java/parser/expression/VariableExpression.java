package parser.expression;

import lexer.Token;

public class VariableExpression extends Expression {

    public Token name;

    public VariableExpression(Token name) {
        this.name = name;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitVariableExpression(this);
    }
}
