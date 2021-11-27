package parser.expression;

import lexer.Token;

public class AssignmentExpression extends Expression {

    public Token name;
    public Expression value;

    public AssignmentExpression(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <R> R accept(ExpressionVisitor<R> visitor) {
        return visitor.visitAssignmentExpression(this);
    }
}
