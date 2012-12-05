package expanderExpression;

/**
 *
 * @author renato
 */
public class ExpanderExpression {

    public static String runExpanderExpression(String expression) {

        BuilderTree builderTree = new BuilderTree();

        if (expression.contains("(")) {

            Tree tree = builderTree.run(expression);
            Expression s = new Expression(tree.getRoot().getProduct());
            return s.getExpression();

        } else {

            return builderTree.deletSpace(expression);
        }
    }

}
