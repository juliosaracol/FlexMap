package expanderExpression;

import java.util.ArrayList;

/**
 *
 * @author renato
 */
public class Node {

    private Node father;
    private Node leftNode;
    private Node rightNode;
    private Object keyValue;
    private ArrayList<ArrayList<String>> product;

    public Node() {
        this.father = null;
        this.leftNode = null;
        this.rightNode = null;
        this.keyValue = null;
        this.product = null;
    }

    public Node(Object key) {
        this.father = null;
        this.leftNode = null;
        this.rightNode = null;
        this.keyValue = key;
        this.product = new ArrayList<ArrayList<String>>();
    }

    public Object getKeyValeu() {
        return keyValue;
    }

    public Node getFather() {
        return father;
    }
    
    public Node getLeftNode() {
        return leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public ArrayList<ArrayList<String>> getProduct() {
        return product;
    }

    public void setKeyValeu(Object key) {
        this.keyValue = key;
    }

    public void setFather(Node father) {
        this.father = father;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public void setProduct(ArrayList<ArrayList<String>> product) {
        this.product = product;
    }

    public void addProduct(ArrayList<String> production) {
        this.product.add(production);
    }
}
