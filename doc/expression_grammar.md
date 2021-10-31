# Grammar for SpaceC expressions

This is a rough version, which is enough for now, but improved in the future.

```
expression -> equality ;
equality -> comparison ( ( "=" | "!=" ) comparison )* ;
comparison -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term -> factor ( ( "+" | "-" ) factor )* ;
factor -> unary ( ( "/" | "*" ) unary )* ;
unary -> ("-" | "!") unary | primary ;
primary -> NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" ;
```

## Operator precedence and associativity

From lowest to highest:

Name | Operators | Associates
|----|-----------|-----------|
| Equality| =, != | Left |
| Comparison | <, <=, >, >= | Left|
| Term | +, - | Left |
| Factor | /, * | Left |
| Unary | !, - | Right |