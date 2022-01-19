# Grammar of SpaceC statements

program -> declaration* EOF ;

declaration -> statement ;

statement -> expressionStatement | printStatement | block;

block -> "{" declaration* "}" ;

expressionStatement -> expression ; printStatement -> "print" expression ";" ;

_program_ is the entrypoint of the program. It represents a complete SpaceC-script. The mandatory
end token ensures that the parser all the tokens.

Declaration statements come before other statements, because they have higher precedence: everywhere
where declaration statements are allowed, also other kinds of statements are allowed, but not the
other way around. For example, this is the case after unparenthesized if-statements; this is not
allowed

```
if (condition) var asdf = 0 // What is the scope of this variable?
```