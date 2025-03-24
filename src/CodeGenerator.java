/**
 * @author mzhang27
 * @since 2025/3/13
 */
import java.util.stream.Collectors;

public class CodeGenerator {
    public String generate(Node node) {
        switch (node.type) {
            case "Program":
                return node.body.stream()
                        .map(this::generate)
                        .collect(Collectors.joining("\n"));
            case "ExpressionStatement":
                return generate(node.expression) + ";";
            case "CallExpression":
                return generate(node.callee) + "(" +
                        node.arguments.stream()
                                .map(this::generate)
                                .collect(Collectors.joining(", ")) + ")";
            case "Identifier":
                return node.name;
            case "NumberLiteral":
                return node.value;
            case "StringLiteral":
                return "\"" + node.value + "\"";
            default:
                throw new RuntimeException("Unknown node type: " + node.type);
        }
    }
}