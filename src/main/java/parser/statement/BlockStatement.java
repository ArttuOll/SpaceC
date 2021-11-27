package parser.statement;

import java.util.List;

public class BlockStatement extends Statement {

    public final List<Statement> statements;

    public BlockStatement(
        List<Statement> statements
    ) {
        this.statements = statements;
    }

    @Override
    public <R> R accept(StatementVisitor<R> visitor) {
        return visitor.visitBlockStatement(this);
    }
}
