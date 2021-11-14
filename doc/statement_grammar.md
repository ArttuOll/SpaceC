# Grammar of SpaceC statements

program -> statement* EOF ; statement -> expressionStatement | printStatement ; expressionStatement
-> expression ; printStatement -> "print" expression ";" ;

_program_ is the entrypoint of the program. It represents a complete SpaceC-script. The mandatory
end token ensures that the parser all the tokens.