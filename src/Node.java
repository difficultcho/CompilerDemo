/**
 * @author mzhang27
 * @since 2025/3/13
 * 
 * 节点类型的设计，需要能够包含必要的信息，提供给 transformer
 */
import java.util.ArrayList;
import java.util.List;

public class Node {
	/**
	 * 节点类型
	 */
    public String type;
    
    /**
     * 节点名
     */
    public String name;
    
    /**
     * 节点值
     */
    public String value;

    /**
     * 节点内容
     */
    public List<Node> body = new ArrayList<>();

    /**
     * 节点参数 params
     * 对应于 expression，即 expression 节点有若干的 params
     */
    public List<Node> params = new ArrayList<>();

    /**
     * 节点参数 arguments
     */
    public List<Node> arguments = new ArrayList<>();

    /**
     * 节点的调用对象
     */
    public Node callee;

    /**
     * 节点的表达式
     */
    public Node expression;

    /**
     * 节点的上下文环境
     */
    public List<Node> _context;

    public Node(String type) {
        this.type = type;
    }

    public Node(String type, String value) {
        this.type = type;
        this.value = value;
    }
}