package expanderExpression;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author renato
 */
public class BuilderTree {

    private Tree root;
    private ArrayList<Node> queue;
    private boolean flagDeMorgan;

    public BuilderTree() {
        this.root = new Tree();
        this.queue = new ArrayList<Node>();
        this.flagDeMorgan = false;
    }

    public Tree run(String expression) {

        expression = deletSpace(expression);

        ArrayList<String> product;
        Node nodo = new Node(expression);
        queue.add(nodo);

        for (int i = 0; i < queue.size(); i++) {

            if (queue.get(i) != null) {
                expression = (String) queue.get(i).getKeyValeu();

                while (checkParentheses(expression)) {
                    if (expression.length() > 1) {
                        expression = expression.substring(1, expression.length() - 1);
                    } else {
                        break;
                    }
                }

                int index = findRoot(expression);

                if (index == 0) {

                    queue.get(i).setKeyValeu(expression);
                    product = new ArrayList<String>();
                    product.add(expression);
                    queue.get(i).addProduct(product);
                    queue.add(null);
                    queue.add(null);

                } else {

                    if (index == -1) {
                        flagDeMorgan = true;
                        queue.get(i).setKeyValeu("" + expression.charAt(0));

                        Node nodeLeft = new Node("" + expression.substring(1, expression.length()));
                        queue.get(i).setLeftNode(nodeLeft);
                        nodeLeft.setFather(queue.get(i));
                        queue.add(nodeLeft);


                    } else {
                        queue.get(i).setKeyValeu("" + expression.charAt(index));

                        Node nodeLeft = new Node("" + expression.substring(0, index));
                        queue.get(i).setLeftNode(nodeLeft);
                        nodeLeft.setFather(queue.get(i));
                        queue.add(nodeLeft);

                        Node nodeRigth = new Node("" + expression.substring(index + 1, expression.length()));
                        queue.get(i).setRightNode(nodeRigth);
                        nodeRigth.setFather(queue.get(i));
                        queue.add(nodeRigth);
                    }
                }
            }
        }

        this.root.setRoot(queue.get(0));

        if (flagDeMorgan) {
            dfs(this.root.getRoot());
        }

        inorderTree(this.root.getRoot());

        return this.root;
    }

    private int findRoot(String s) {

        int contParentheses = 0;
        int signalRoot = -1;
        int posAuxSignal = 0;
        int pos = 0;
        boolean iteration;

        while (pos < s.length()) {

            if (s.charAt(pos) == '*' || s.charAt(pos) == '+' || s.charAt(pos) == '(' || (s.charAt(pos) == '!') && s.charAt(pos+1) == '(') {

                posAuxSignal = pos;
                contParentheses = 0;

                if (s.charAt(pos) == '(') {
                    contParentheses++;
                }

                iteration = true;

                while (iteration) {
                    pos++;

                    while (s.charAt(pos) == '!') {
                        pos++;
                    }

                    if (s.charAt(pos) == '(') {
                        contParentheses++;
                    } else {
                        if (s.charAt(pos) == ')') {
                            contParentheses--;
                        }
                    }

                    if (contParentheses == 0) {
                        iteration = false;
                    }
                }

                if (pos == s.length() - 1) {

                    if ((s.charAt(0) == '!' && s.charAt(1) == '(') && signalRoot == -1) {
                        return -1;
                    } else {
                        if (signalRoot == -1) {
                            return posAuxSignal;
                        } else {
                            return signalRoot;
                        }
                    }
                } else {
                    pos++;
                    if ((s.charAt(posAuxSignal) == '!' && s.charAt(posAuxSignal+1) == '(') || s.charAt(posAuxSignal) == '(') {
                        signalRoot = pos;
                    } else {
                        if (s.charAt(posAuxSignal) == '+' && s.charAt(pos) == '+') {
                            signalRoot = pos;
                        } else {
                            if (s.charAt(posAuxSignal) == '*' && (s.charAt(pos) == '+' || s.charAt(pos) == '*')) {
                                signalRoot = pos;
                            } else {
                                if (s.charAt(pos) != '*' && s.charAt(pos) != '+' && s.charAt(pos) != '!') {
                                    signalRoot = posAuxSignal;
                                }
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

    private boolean checkParentheses(String s) {

        int contParentheses = 0;

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '(') {
                contParentheses++;
            } else {
                if (s.charAt(i) == ')') {
                    contParentheses--;
                }
            }

            if (contParentheses == 0) {
                if (i == s.length() - 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;

    }

    public String deletSpace(String expression) {

        String s = "";
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) != ' ') {
                s += expression.charAt(i);
            }
        }

        return s;
    }

    private void dfs(Node root) {

        deMorgan(root);

        if (root.getLeftNode() == null && root.getRightNode() == null) {
            return;
        } else {
            if (root.getLeftNode() != null) {
                dfs(root.getLeftNode());
            }
            if (root.getRightNode() != null) {
                dfs(root.getRightNode());
            }
        }
    }

    private void deMorgan(Node current) {

        if (current != null && current.getKeyValeu().equals("!")) {

            if (current.getLeftNode().getKeyValeu().equals("+") || current.getLeftNode().getKeyValeu().equals("*")) { // caso do filho ser um + ou *

                if (current.getLeftNode().getKeyValeu().equals("+")) {
                    current.setKeyValeu("*");
                } else {
                    current.setKeyValeu("+");
                }

                Node auxLeft = new Node("!");
                Node auxRight = new Node("!");

                auxLeft.setLeftNode(current.getLeftNode().getLeftNode());
                current.getLeftNode().getLeftNode().setFather(auxLeft);
                auxRight.setLeftNode(current.getLeftNode().getRightNode());
                current.getLeftNode().getRightNode().setFather(auxRight);

                current.setLeftNode(auxLeft);
                auxLeft.setFather(current);
                current.setRightNode(auxRight);
                auxRight.setFather(current);
            } else {
                if (current.getLeftNode().getKeyValeu().equals("!")) {
                    current.getLeftNode().getLeftNode().setFather(current.getFather());

                    if (current.getFather() != null) {
                        if (current.getFather().getLeftNode().equals(current)) {
                            current.getFather().setLeftNode(current.getLeftNode().getLeftNode());
                        } else {
                            current.getFather().setRightNode(current.getLeftNode().getLeftNode());
                        }
                    } else {
                        root.setRoot(current.getLeftNode().getLeftNode());
                    }

                }
            }

        }

    }

    private void inorderTree(Node root) {

        if (root == null) {
            return;
        } else {
            if (root.getLeftNode() == null && root.getRightNode() == null) {
                return;
            } else {
                inorderTree(root.getLeftNode());
                inorderTree(root.getRightNode());
                newTree(root);
            }
        }
    }

    private void newTree(Node root) {

        ArrayList<ArrayList<String>> arrayProduct = new ArrayList<ArrayList<String>>();
        ArrayList<String> arrayString;

        if (root.getKeyValeu().equals("+")) {

            for (int i = 0; i < root.getLeftNode().getProduct().size(); i++) { //array externo left

                arrayString = new ArrayList<String>();

                for (int j = 0; j < root.getLeftNode().getProduct().get(i).size(); j++) { //array interno left
                    arrayString.add(root.getLeftNode().getProduct().get(i).get(j));
                }

                arrayProduct.add(arrayString);

            }

            for (int i = 0; i < root.getRightNode().getProduct().size(); i++) { //array externo right

                arrayString = new ArrayList<String>();

                for (int j = 0; j < root.getRightNode().getProduct().get(i).size(); j++) { //array interno right
                    arrayString.add(root.getRightNode().getProduct().get(i).get(j));
                }

                arrayProduct.add(arrayString);
            }

        } else {

            if (root.getKeyValeu().equals("*")) {

                ArrayList<String> arrayStringLeft;

                for (int i = 0; i < root.getLeftNode().getProduct().size(); i++) { //array externo left

                    arrayStringLeft = new ArrayList<String>();
                    copyArrayString(root.getLeftNode().getProduct().get(i), arrayStringLeft);

                    for (int j = 0; j < root.getRightNode().getProduct().size(); j++) { //array externo right

                        arrayString = new ArrayList<String>();
                        copyArrayString(arrayStringLeft, arrayString);

                        for (int l = 0; l < root.getRightNode().getProduct().get(j).size(); l++) { //array interno right
                            if (!arrayString.contains(root.getRightNode().getProduct().get(j).get(l))) {
                                arrayString.add(root.getRightNode().getProduct().get(j).get(l));
                            }
                        }

                        arrayProduct.add(arrayString);

                    }

                }

            } else {
                //case of "!"
                for (int i = 0; i < root.getLeftNode().getProduct().size(); i++) {

                    arrayString = new ArrayList<String>();

                    for (int j = 0; j < root.getLeftNode().getProduct().get(i).size(); j++) {
                        if (root.getLeftNode().getProduct().get(i).get(j).charAt(0) == '!') {
                            arrayString.add(root.getLeftNode().getProduct().get(i).get(j).substring(1, root.getLeftNode().getProduct().get(i).get(j).length()));
                        } else {
                            arrayString.add("!" + root.getLeftNode().getProduct().get(i).get(j));
                        }
                    }

                    arrayProduct.add(arrayString);
                }
            }

        }

        root.setProduct(arrayProduct);
        deleteChildren(root);


        for (int i = 0; i < arrayProduct.size(); i++) {
            Collections.sort(arrayProduct.get(i));
        }
    }

    private void deleteChildren(Node root) {

        root.setLeftNode(null);
        root.setRightNode(null);

    }

    private void copyArrayString(ArrayList<String> copy, ArrayList<String> paste) {

        for (int i = 0; i < copy.size(); i++) {
            paste.add(copy.get(i));
        }

    }
}
