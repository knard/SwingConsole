grammar SyntaxParser;

QUOTES: '"';
OR: '|';
OPENING_PARENTHESIS: '(';
CLOSING_PARENTHESIS: ')';
ID :  [a-zA-Z] [a-zA-Z0-9]*;
WS: [ \n\t\r]+ -> skip;

syntax : expression EOF;

expression : or_expression | element ;

or_expression : OPENING_PARENTHESIS followed_expression ( OR followed_expression )+ CLOSING_PARENTHESIS;

followed_expression : expression+;

element : name | name_holder;

name : QUOTES ID QUOTES;

name_holder : ID;