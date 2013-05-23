package functionsClasses;

import brc.BRC;
import brc.BRCBuilder;
import brc.BRCHandler;
import expanderExpression.ExpressionTokenizer;
import java.util.ArrayList;
import java.util.TreeMap;

public class SetFunction {

    public SetFunction() {

    }

    public static BitRepresentation builFunctionRepresentationCode(String expression, int nVariables) {

        ExpressionTokenizer parser = new ExpressionTokenizer(expression);
        ArrayList<ArrayList<String>> cubes = parser.tokenizer();
        
        TreeMap<String, BRC> basicCodes = BRCBuilder.getBasicRepresentationCodes(nVariables, parser.getVariablesSet());
        BRC functionBRC = BRCHandler.builFunctionRepresentationCode(cubes, basicCodes);
        
        return getBitRepresentatio(functionBRC, nVariables);
    }
    
    public static BitRepresentation builFunctionRepresentationCode(BRC functionBRC, int nVariables) {
        
        return getBitRepresentatio(functionBRC, nVariables);
        
    }
    
    private static BitRepresentation getBitRepresentatio(BRC fubnctionBRC, int nVariables) {

        int nBits = (int) Math.pow(2, nVariables);
        
        BitRepresentation function = new BitRepresentation(nBits, nVariables);
        String binary = BRCHandler.toBinaryString(fubnctionBRC);
        int index = 0;
        
        for(int i=(binary.length() - nBits); i < binary.length(); i++, index++) {
            if(binary.charAt(i) == '1') {
                function.flip(index);
            }
        }
        
        return function;
    }

}
