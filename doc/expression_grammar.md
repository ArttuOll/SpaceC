# Grammar for SpaceC expressions

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

Some notes:

- Negative zero is allowed.