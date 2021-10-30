package parser.expression;

abstract class Expression {
    abstract <R> R accept(ExpressionVisitor<R> visitor);
}
