package brc;

import java.util.ArrayList;
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
        
        for(int i=0; i <variablesSet.size(); i++) {
            
            variable = variablesSet.get(i);
            brc = basicCodesArray.get(i);
            basicCodes.put(variable, brc);
        }
        
        return basicCodes;
    }
    
    private static ArrayList<BRC> buildBasicRepresentationCodes(int nVariables) {

        ArrayList<BRC> BRCList = new ArrayList<BRC>();
        double nInteger = (Math.pow(2, nVariables) / 32);
        int exp = nVariables - 1;
        int iteration = 0;
        
        if (nVariables <= 5) {
            return BRCTemplate.getBRCStructure(nVariables);
        } else {
            
            for (int pos = 0; pos < 5; pos++) {
                BRCList.add(new BRC());

                for (int integer = 0; integer < nInteger; integer++) {
                    BRCList.get(pos).addBRC(BRCTemplate.getIntegerBRC(pos + 1));
                }
            }

            for (int pos = 5; pos < nVariables; pos++) {
                BRCList.add(new BRC());

                int integer = 0;
                while (integer < nInteger) {

                    /*atribui valor 0 para assinatura*/
                    for (int i = 0; i < Math.pow(2, iteration); i++) {
                        BRCList.get(pos).addBRC(0);
                        integer++;
                    }

                    /*atribui valor -1 para a assinatura*/
                    for (int j = 0; j < Math.pow(2, iteration); j++) {
                        BRCList.get(pos).addBRC(-1);
                        integer++;
                    }
                }
                iteration++;
            }
        }
        
        return BRCList;
        
    }
    
}
