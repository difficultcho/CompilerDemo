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

    /**
     * 核心逻辑：
     * 对每个 AST 节点，一进一出，期间对 program 和 expression 节点依次 traverse 其内容
     * 一进：
     * 一出：
     */
    private void traverse(Node node, Node parent, Visitor visitor) {
        visitor.enter(node, parent);

        switch (node.type) {
            case "Program":
                traverseArray(node.body, node, visitor); // program 的内容在 body 上
                break;
            case "CallExpression":
                traverseArray(node.params, node, visitor); // expression 的内容在 params 上
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
            	// 当前节点是数值，则其父节点的对偶节点（新 AST 节点）上，新增一个数值节点
                parent._context.add(new Node("NumberLiteral") {{
                    value = node.value;
                }});
            }

            if (node.type.equals("StringLiteral")) {
            	// 当前节点是 String，则其父节点的对偶节点（新 AST 节点）上，新增一个 String 节点
                parent._context.add(new Node("StringLiteral") {{
                    value = node.value;
                }});
            }

            if (node.type.equals("CallExpression")) {
                Node expr = new Node("CallExpression");
                
                // 为目标语言生成准备
                expr.callee = new Node("Identifier") {{
                    name = node.name;
                }};
                expr.arguments = new ArrayList<>();
                node._context = expr.arguments;

                // 
                if (parent.type.equals("CallExpression")) {
                	// 当前节点是 expression，上级节点也是 expression 时，
                	// 上级节点的对偶节点的上下文里增添一个当前的 expression 节点
                	parent._context.add(expr);
                } else {
                	// 当前节点是 expression，而上级节点不是 expression 时，
                	// 上级节点的对偶节点的上下文里增添一个【包装后的】当前的 expression 节点
                	parent._context.add(new Node("ExpressionStatement") {{
                        expression = expr;
                    }});
                }
            }
        }

        public void exit(Node node, Node parent) {

        }
        
    }
}