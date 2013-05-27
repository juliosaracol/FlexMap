package interpretfunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


/**
 *
 * @author renato
 */
public class InterpretFunction {

    Tree tree;
    Stack stack;
    String functionCost;

    public InterpretFunction(String fc) {
        this.tree = new Tree();
        this.functionCost = handleExpression.HandleExpression.deletSpace(fc);
        this.stack = new Stack();

        createTree();
    }

    public float calcFunctionCost(float area, float delay, float power, float input, float output, float other) {

        HashMap<String, Float> hash = new HashMap<String, Float>();

        hash.put("area", area);
        hash.put("delay", delay);
        hash.put("power", power);
        hash.put("input", input);
        hash.put("output", output);
        hash.put("other", other);

        inorderTree(this.tree.getRoot(), hash);

        return (Float) this.stack.pop();
    }

    private void createTree() {

        int indexRoot;
        String f;

        ArrayList<Node> queue = new ArrayList<Node>();
        Node nodo = new Node(this.functionCost);
        queue.add(nodo);

        for (int i = 0; i < queue.size(); i++) {

            if (queue.get(i) != null) {

                f = (String) queue.get(i).getKeyValeu();

                while (handleExpression.HandleExpression.checkParenthesesBeginAndEnd(f)) {
                    if (f.length() > 1) {
                        f = f.substring(1, f.length() - 1);
                    } else {
                        break;
                    }
                }

                indexRoot = findRoot(f);

                if (indexRoot == 0) {

                    queue.get(i).setKeyValeu(f);
                    queue.add(null);
                    queue.add(null);

                } else {

                    queue.get(i).setKeyValeu("" + f.charAt(indexRoot));

                    Node nodeLeft = new Node("" + f.substring(0, indexRoot));
                    queue.get(i).setLeftNode(nodeLeft);
                    nodeLeft.setFather(queue.get(i));
                    queue.add(nodeLeft);

                    Node nodeRigth = new Node("" + f.substring(indexRoot + 1, f.length()));
                    queue.get(i).setRightNode(nodeRigth);
                    nodeRigth.setFather(queue.get(i));
                    queue.add(nodeRigth);

                }
            }
        }

        this.tree.setRoot(queue.get(0));
    }

    private int findRoot(String s) {

        int contParentheses = 0;
        int signalRoot = -1;
        int pos = 0;
        int currentOp;
        boolean iteration = false;

        while (pos < s.length()) {

            if (s.charAt(pos) == '-' || s.charAt(pos) == '+' || s.charAt(pos) == '*' || s.charAt(pos) == '/' || s.charAt(pos) == '(') {

                currentOp = pos;

                contParentheses = 0;
                if (s.charAt(pos) == '(') {
                    contParentheses++;
                    iteration = true;
                }

                while (iteration) {
                    pos++;

                    if (s.charAt(pos) == '(') {
                        contParentheses++;
                    } else {
                        if (s.charAt(pos) == ')') {
                            contParentheses--;
                        }
                    }

                    if (contParentheses == 0) {
                        iteration = false;
                        pos++;
                    }
                }

                if (pos == s.length() - 1) {
                    return signalRoot;
                }
                if (signalRoot == -1) {
                    signalRoot = pos;
                } else {
                    if ((s.charAt(signalRoot) == '*' || s.charAt(signalRoot) == '/') && (s.charAt(currentOp) == '+' || s.charAt(currentOp) == '-')) {
                        signalRoot = currentOp;
                    } else {
                        if ((s.charAt(signalRoot) == '*' && s.charAt(currentOp) == '/') || (s.charAt(signalRoot) == '/' && s.charAt(currentOp) == '*')) {
                            signalRoot = currentOp;
                        } else {
                            if ((s.charAt(signalRoot) == '+' && s.charAt(currentOp) == '-') || (s.charAt(signalRoot) == '-' && s.charAt(currentOp) == '+')) {
                                signalRoot = currentOp;
                            }
                        }
                    }
                }

            }

            pos++;

        }

        if (signalRoot != -1) {
            return signalRoot;
        } else {
            return 0;
        }

    }

    private void inorderTree(Node root, HashMap<String, Float> hash) {
        
        if (root.getLeftNode() == null && root.getRightNode() == null) {            
            if ( hash.get((String) root.getKeyValeu()) != null) {
                this.stack.push(hash.get((String) root.getKeyValeu()));
            } else {
                this.stack.push(Float.parseFloat((String) root.getKeyValeu()));
            }
        } else {
            inorderTree(root.getLeftNode(), hash);           
            inorderTree(root.getRightNode(), hash);            
            calc(root);
        }

    }
    
    private void calc(Node root) {

        float b = (Float) this.stack.pop();
        float a = (Float) this.stack.pop();

        if (root.getKeyValeu().equals("+")) {
            this.stack.push(a + b);
        } else {
            if (root.getKeyValeu().equals("-")) {
                this.stack.push(a - b);
            } else {
                if (root.getKeyValeu().equals("*")) {
                    this.stack.push(a * b);
                } else {
                    if (root.getKeyValeu().equals("/")) {
                        this.stack.push(a / b);
                    }
                }
            }
        }
    }    
    
}
