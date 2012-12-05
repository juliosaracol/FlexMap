package expanderExpression;


import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author vini
 */
public class ExpressionTokenizer {
    
    private String expression;
    private ArrayList<String> variablesSet;
    
    public ExpressionTokenizer(String expression) { 
        this.expression = expression;
        this.variablesSet = new ArrayList<String>();
    } 

    public ArrayList<ArrayList<String>> tokenizer() {

        int last;
        String product;
        String literal;
        ArrayList<ArrayList<String>> products = new ArrayList<ArrayList<String>>();
        
        StringTokenizer splitedProducts = new StringTokenizer(expression, "+"); // separa por +

        while (splitedProducts.hasMoreTokens()) {

            product = splitedProducts.nextToken(); 
            StringTokenizer splitedLiterals = new StringTokenizer(product, "*"); // separa por *
            ArrayList<String> literals = new ArrayList<String>();

            while (splitedLiterals.hasMoreTokens()) {
                literal = splitedLiterals.nextToken();
                literals.add(literal);
                
                last = literal.length();
                if(literal.charAt(0) == '!') {
                    literal = literal.substring(1, last);
                }
                if(!variablesSet.contains(literal)) {
                    variablesSet.add(literal);
                }
            }

            products.add(literals);
        }        
        
        return products;
    }

    
    /***************** GETER AND SETTER ****************/
    
    public String getLine() {
        return expression;
    }

    public void setLine(String line) {
        this.expression = line;
    }

    public ArrayList<String> getVariablesSet() {
        return variablesSet;
    }

    public void setVariablesSet(ArrayList<String> variablesSet) {
        this.variablesSet = variablesSet;
    }

}
