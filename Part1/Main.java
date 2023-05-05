import java.io.IOException;

class Main {
    public static void main(String[] args) {
        System.out.println("Give an expression: ");
        try {
            System.out.println("Value of expression is: " + (new TernaryEvaluator(System.in)).eval());
        } catch (IOException | ParseError e) {
            System.err.println(e.getMessage());
        }
    }
}
