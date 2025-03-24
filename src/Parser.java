/**
 * @author mzhang27
 * @since 2025/3/13
 */
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int current = 0;

    public Node parse(List<Token> tokens) {
        this.tokens = tokens;
        Node ast = new Node("Program");
        ast.body = new ArrayList<>();

        while (current < tokens.size()) {
            ast.body.add(walk());
        }

        return ast;
    }

    private Node walk() {
        Token token = tokens.get(current);

        if (token.type.equals("number")) {
            current++;
            return new Node("NumberLiteral", token.value);
        }

        if (token.type.equals("string")) {
            current++;
            return new Node("StringLiteral", token.value);
        }

        /**
         * 仅针对 demo 的 处理逻辑：
         * 括号意味着表达式，
         * 表达式名为括号下一个 token，
         * 表达式参数为随后所有的 token，直到括号关闭
         */
        if (token.type.equals("parenthesis") && token.value.equals("(")) {
            current++;
            token = tokens.get(current);

            Node node = new Node("CallExpression");
            node.name = token.value;
            node.params = new ArrayList<>();

            current++;
            token = tokens.get(current);

            while (!token.type.equals("parenthesis") || !token.value.equals(")")) {
                node.params.add(walk());
                token = tokens.get(current);
            }

            current++;
            return node;
        }

        throw new RuntimeException("Unknown token type: " + token.type);
    }
}