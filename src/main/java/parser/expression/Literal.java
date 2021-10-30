package parser.expression;

class Literal extends Expression {

    final Object value;

    Literal(Object value) {
        this.value = value;
    }
}
