package parser.statement;

public abstract class Statement {

    public abstract <R> R accept(StatementVisitor<R> visitor);
}
