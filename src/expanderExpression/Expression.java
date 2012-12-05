package expanderExpression;

import java.util.ArrayList;

/**
 *
 * @author renato
 */
public class Expression {

    private ArrayList<ArrayList<String>> expressionArray;
    private String expression;

    public Expression(ArrayList<ArrayList<String>> array) {

        this.expressionArray = new ArrayList<ArrayList<String>>();
        this.expression = "";
        
        for (int i = 0; i < array.size(); i++) {
            this.expressionArray.add(array.get(i));
        }

        builderExpression();
    }

    private void builderExpression() {

        for (int i = 0; i < expressionArray.size(); i++ ) {
            
            for (int y = 0; y < expressionArray.get(i).size(); y++) {
                expression += expressionArray.get(i).get(y) + "*";   
            }
            
            expression = expression.substring(0, expression.length()-1);
            expression += "+";
        }
        
        expression = expression.substring(0, expression.length()-1);
        
    }

    public String getExpression() {
        return expression;
    }

    public ArrayList<ArrayList<String>> getExpressionArray() {
        return expressionArray;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setExpressionArray(ArrayList<ArrayList<String>> expressionArray) {
        this.expressionArray = expressionArray;
    }
        
}
