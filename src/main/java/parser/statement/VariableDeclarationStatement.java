package parser.statement;

import lexer.Token;
import parser.expression.Expression;

public class VariableDeclarationStatement extends Statement {

    public Token name;
    public Expression initializer;

    public VariableDeclarationStatement(
        Token name, Expression initializer
    ) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitVariableDeclarationStatement(this);
    }
}
