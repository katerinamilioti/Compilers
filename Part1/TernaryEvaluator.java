import java.io.InputStream;
import java.io.IOException;
 
/* Grammar and LL(1) Parsing Table are in README */

class TernaryEvaluator{
    private final InputStream in;

    private int lookahead;

    public TernaryEvaluator(InputStream in) throws IOException {
        this.in = in;
        lookahead = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {
        if (lookahead == symbol){
            lookahead = in.read();
        }
        else{
            throw new ParseError();
        }
    }

    private boolean isDigit(int c) {
        return '0' <= c && c <= '9';
    }

    private int evalDigit(int c) {
        return c - '0';
    }

    public int eval() throws IOException, ParseError {
        int value = exp();

        if (lookahead != -1 && lookahead != '\n'){
            throw new ParseError();
        }

        return value;
    }

    private int exp() throws IOException, ParseError {
        /* #1 production */
        if(isDigit(lookahead)){
            int y = term();
            return exp2(y);
        }
        /* #1 production */
        if(lookahead =='('){
            int y = term();
            return exp2(y);
        }
        /* For all other symbols in exp() we have parse error */
        throw new ParseError();
    }

   private int exp2(int cond) throws IOException, ParseError {
        switch(lookahead){
            /* #2 production */
            case '^':
                consume('^');
                int n = term();
                System.out.print(cond + "^" + n + " = ");
                int x = cond ^ n;
                System.out.println(x);
                return exp2(x);
            /* For symbols ),$ must do nothing, so return value */
            case ')':
            case '\n':
            case -1:
                return cond;
        }
        /* For all other symbols in exp2() we have parse error */
        throw new ParseError();
    }

    private int term() throws IOException, ParseError {
        /* #4 production */
        if(isDigit(lookahead)){
            int x = factor();
            int y = term2(x);
            return y;
        }
        /* #4 production */
        if(lookahead =='('){
            int x = factor();
            int y = term2(x);
            return y; 
        }
        /* For all other symbols in term() we have parse error */
        throw new ParseError();
    }

    private int term2(int cond) throws IOException, ParseError {
        switch(lookahead){
            /* #5 production */ 
            case '&':
                consume('&');
                int n = factor();
                System.out.print(cond + "&" + n + " = ");
                int x = cond & n;
                System.out.println(x);
                return term2(x);
            /* For symbols ),$,^ must do nothing, so return value */
            case ')':
            case '\n':
            case -1:
            case '^':
                return cond;
        }
        /* For all other symbols in term2() we have parse error */
        throw new ParseError();
    }

    private int factor() throws IOException, ParseError {
        /* #7 production */
        if(isDigit(lookahead)){
            return num();
        }
        /* #8 production */
        if(lookahead == '('){
            consume('(');
            int y = exp();
            consume(')');
            return y;
        }
        /* For all other symbols in factor() we have parse error */
        throw new ParseError();
    }

    private int num() throws IOException, ParseError {
        /* #(9-18) production */
        if(isDigit(lookahead)){
            int cond = evalDigit(lookahead);       // Convert character to integer
            consume(lookahead);
            return cond;
        }
        /* For all other symbols in num() we have parse error */
        throw new ParseError();
    }
}