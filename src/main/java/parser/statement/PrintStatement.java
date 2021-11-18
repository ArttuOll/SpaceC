package parser.statement;

import parser.expression.Expression;

public class PrintStatement extends Statement {

    public final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitPrintStatement(this);
    }
}
