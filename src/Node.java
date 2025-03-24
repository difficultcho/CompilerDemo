/**
 * @author mzhang27
 * @since 2025/3/13
 */
import java.util.ArrayList;
import java.util.List;

public class Node {
    public String type;
    public String value;
    public String name;
    public List<Node> params = new ArrayList<>();
    public List<Node> body = new ArrayList<>();
    public List<Node> arguments = new ArrayList<>();
    public Node callee;
    public Node expression;
    public List<Node> _context;

    public Node(String type) {
        this.type = type;
    }

    public Node(String type, String value) {
        this.type = type;
        this.value = value;
    }
}