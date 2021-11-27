package parser.statement;

public interface StatementVisitor<R> {

    R visitExpressionStatement(ExpressionStatement statement);

    R visitPrintStatement(PrintStatement statement);

    R visitVariableDeclarationStatement(VariableDeclarationStatement statement);

    R visitBlockStatement(BlockStatement statement);
}
