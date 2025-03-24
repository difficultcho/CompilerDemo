/**
 * @author mzhang27
 * @since 2025/3/13
 */
public class Main {
    public static void main(String[] args) {
        String input = "(add (mul 15 (add 7 66)) (subtract 4 \"abc\"))";
        //String input = "(add 2 3)";
        Compiler compiler = new Compiler();
        String output = compiler.compile(input);
        System.out.println(output); // 输出: add(2, subtract(4, 2));
    }
}