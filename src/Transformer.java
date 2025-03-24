/**
 * @author mzhang27
 * @since 2025/3/13
 */
import java.util.ArrayList;
import java.util.List;

public class Transformer {
    public Node transform(Node ast) {
        Node newAst = new Node("Program");
        newAst.body = new ArrayList<>();
        ast._context = newAst.body;

        traverse(ast, null, new Visitor());
        return newAst;
    }

    private void traverse(Node node, Node parent, Visitor visitor) {
        visitor.enter(node, parent);

        switch (node.type) {
            case "Program":
                traverseArray(node.body, node, visitor);
                break;
            case "CallExpression":
                traverseArray(node.params, node, visitor);
                break;
            case "NumberLiteral":
            case "StringLiteral":
                break;
            default:
                throw new RuntimeException("Unknown node type: " + node.type);
        }

        visitor.exit(node, parent);
    }

    private void traverseArray(List<Node> nodes, Node parent, Visitor visitor) {
        for (Node node : nodes) {
            traverse(node, parent, visitor);
        }
    }

    private static class Visitor {

        public void enter(Node node, Node parent) {
            if (node.type.equals("NumberLiteral")) {
                parent._context.add(new Node("NumberLiteral") {{
                    value = node.value;
                }});
            }

            if (node.type.equals("StringLiteral")) {
                parent._context.add(new Node("StringLiteral") {{
                    value = node.value;
                }});
            }

            if (node.type.equals("CallExpression")) {
                Node expression = new Node("CallExpression");
                expression.callee = new Node("Identifier") {{
                    name = node.name;
                }};
                expression.arguments = new ArrayList<>();
                node._context = expression.arguments;

                if (!parent.type.equals("CallExpression")) {
                    expression = new Node("ExpressionStatement") {{
                        this.expression = expression;
                    }};
                }

                parent._context.add(expression);
            }
        }

        public void exit(Node node, Node parent) {

        }
    }
}