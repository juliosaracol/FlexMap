package interpretfunction;

import handleExpression.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Classe que implementa o interpretador de funções
 * @author Renato Souza
 */
public class InterpretFunction {

    HashMap<Integer, Object> tree;    
    String functionCost;

    public InterpretFunction(String fc) {
        
        this.functionCost = handleExpression.HandleExpression.deletSpace(fc);        
        this.tree = new HashMap<Integer, Object>();

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
        
        return postOrderTree(1, hash);
    }

    private void createTree() {

        int indexRoot, left, right, father;
        String f;

        ArrayList<Integer> queueFather = new ArrayList<Integer>();
        tree.put(1, this.functionCost);
        queueFather.add(1);

        for (int i = 0; i < queueFather.size(); i++) {

            father = queueFather.get(i);
            f = (String) tree.get(father);

            while (handleExpression.HandleExpression.checkParenthesesBeginAndEnd(f)) {
                if (f.length() > 1) {
                    f = f.substring(1, f.length() - 1);
                } else {
                    break;
                }
            }

            indexRoot = findRoot(f);

            if (indexRoot != 0) {

                tree.put(father, "" + f.charAt(indexRoot));

                left = Heap.leftHeap(father);
                tree.put(left, "" + f.substring(0, indexRoot));
                queueFather.add(left);

                right = Heap.rightHeap(father);
                tree.put(right, "" + f.substring(indexRoot + 1, f.length()));
                queueFather.add(right);

            }

        }
        
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

    private float postOrderTree(int keyRoot, HashMap<String, Float> hash) {

        String root = (String) tree.get(keyRoot);
        float a, b;
        int left = Heap.leftHeap(keyRoot);
        int right = Heap.rightHeap(keyRoot);

        if (tree.get(left) == null && tree.get(right) == null) {
            if (hash.get(root) != null) {
                return hash.get(root);
            } else {
                return Float.parseFloat(root);
            }
        } else {
            a = postOrderTree(left, hash);
            b = postOrderTree(right, hash);
            return calc(root, a, b);
        }
        
    }

    private float calc(String root, float a, float b) {

        if (root.equals("+")) {
            return (a + b);
        } else {
            if (root.equals("-")) {
                return (a - b);
            } else {
                if (root.equals("*")) {
                    return (a * b);
                } else {
                    return (a / b);
                }
            }
        }
        
    }
    
}
