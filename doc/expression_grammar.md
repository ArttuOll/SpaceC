## Grammar for SpaceC expressions

This is a rough version, which is enough for now, but improved in the future.

```
expression -> literal | unary | binary | grouping ;
literal -> NUMBER | STRING | "true" | "false" | "nil" ;
unary -> ("-" | "!") expression ;
binary -> expression operator expression ;
grouping -> "(" expression ")" ;
operator -> "=" | "!=" | "+" | "-" | "*" | "/" | ">" | "<" | "<=" | ">=" ;
```