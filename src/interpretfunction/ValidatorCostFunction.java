package interpretfunction;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 *
 * @author Renato
 */
public class ValidatorCostFunction {

    ArrayList<String> setReservedWords;
    
    public ValidatorCostFunction() {        
        setReservedWords = new ArrayList<String>();
        setReservedWords.add("input");
        setReservedWords.add("output");
        setReservedWords.add("area");
        setReservedWords.add("delay");
        setReservedWords.add("power");
        setReservedWords.add("other");
    }

    public boolean validator(String function) {
        
        String token;
        StringTokenizer splited = new StringTokenizer(function, "+-*/() ");

        while (splited.hasMoreTokens()) {

            token = splited.nextToken();
                       
            // "(\\d)+.(\\d)+"
            if ( !(Pattern.matches ("\\d+(\\.\\d+)?", token)) && !(isReservedWork(token)) ) {
                return false;
            }
            
        }
        
        return true;
    }

    private boolean isReservedWork(String token) {
        
        token = token.toLowerCase();
        
        if (setReservedWords.contains(token)) {
            return true;
        } else {
            return false;
        }
    
    }
        
}