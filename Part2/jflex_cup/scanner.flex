import java_cup.runtime.*;
%%

%class Scanner
%unicode
%cup
%line
%column

%{
StringBuffer stringBuffer = new StringBuffer();
private Symbol symbol(int type) {
   return new Symbol(type, yyline, yycolumn);
}
private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
}
%}


LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

Identifier = [:jletter:] [:jletterdigit:]*

%state STRING

%%

<YYINITIAL> {
"("             { return symbol(sym.LPAREN); }
")"             { return symbol(sym.RPAREN); }
"{"             { return symbol(sym.LBRACE); }
"}"             { return symbol(sym.RBRACE);}
","             { return symbol(sym.COMMA);}
"+"             { return symbol(sym.PLUS);}
"prefix"        {return symbol(sym.PREFIX);}
"if"            {return symbol(sym.IF);}
"else"          {return symbol(sym.ELSE);}
"reverse"       {return symbol(sym.REVERSE);}
\"              { stringBuffer.setLength(0); yybegin(STRING); }
{Identifier}    {return (symbol(sym.IDENTIFIER , new String(yytext())));}

{WhiteSpace}    { /* just skip what was found, do nothing */ }
}


<STRING> {
      \"                             { yybegin(YYINITIAL);
                                        return symbol(sym.STRING_LITERAL, stringBuffer.toString()); }
      [^\n\r\"\\]+                   { stringBuffer.append( yytext() ); }
      \\t                            { stringBuffer.append('\t'); }
      \\n                            { stringBuffer.append('\n'); }

      \\r                            { stringBuffer.append('\r'); }
      \\\"                           { stringBuffer.append('\"'); }
      \\                             { stringBuffer.append('\\'); }
}

[^] { throw new Error("Illegal character <"+
yytext()+">"); }