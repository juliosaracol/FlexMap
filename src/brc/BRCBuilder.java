package brc;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class BRCBuilder {

    public BRCBuilder() {
     
    }

    public static TreeMap<String, BRC> getBasicRepresentationCodes(int nVariables, ArrayList<String> variablesSet) {
        
        // Constroi as BRC basicas
        ArrayList<BRC> basicCodesArray = buildBasicRepresentationCodes(nVariables);
        
        // Cria o map com a variavel e sua BRC corespondente
        TreeMap<String, BRC> basicCodes = new TreeMap<String, BRC>();
        String variable;
        BRC brc;
        
        // Cria o Map de BRC diretos e negados
        for(int i=0; i < variablesSet.size(); i++) {
            
            variable = variablesSet.get(i);
            brc = basicCodesArray.get((basicCodesArray.size()-1)-i); // remove do array do final pra o inicio
            // Direct BRC
            basicCodes.put(variable, brc);            
            // Complementary BRC
            variable = "!" + variable;
            basicCodes.put(variable, BRCHandler.not(brc));            
        }        
        
        return basicCodes;
    }
    
    
    private static ArrayList<BRC> buildBasicRepresentationCodes(int nVariables) {

        ArrayList<BRC> BRCList = new ArrayList<BRC>();
        int nBits = (int)(Math.pow(2, nVariables));
        double nInteger = (nBits / 64);
        int iteration = 0;
        
        
        if (nVariables <= 6) {
            return BRCTemplate.getBRCStructure(nVariables);
        } else {
            
            for (int pos = 0; pos < 6; pos++) {
                BRCList.add(new BRC(nBits));

                for (int integer = 0; integer < nInteger; integer++) {
                    BRCList.get(pos).addBRC(BRCTemplate.getIntegerBRC(pos + 1));
                }
            }

            for (int pos = 6; pos < nVariables; pos++) {
                BRCList.add(new BRC(nBits));

                int integer = 0;
                while (integer < nInteger) {

                    /*atribui valor 0 para assinatura*/
                    for (int i = 0; i < Math.pow(2, iteration); i++) {
                        BRCList.get(pos).addBRC(-1);
                        integer++;
                    }

                    /*atribui valor -1 para a assinatura*/
                    for (int j = 0; j < Math.pow(2, iteration); j++) {
                        BRCList.get(pos).addBRC(0);
                        integer++;
                    }
                }
                iteration++;
            }
        }
        
        return BRCList;
        
    }
    
}
