package functionsClasses;

import java.util.ArrayList;
import java.util.Iterator;

public class PermutationGenerator {

    public static Iterator getPermutations(ArrayList<BitRepresentation> variables) {
        
        ArrayList<ArrayList<BitRepresentation>> permutacoes = new ArrayList<ArrayList<BitRepresentation>>();
        
        if (variables.size() == 1) {
            permutacoes.add(variables);
        } else {
            for (int x = 0; x < variables.size(); x++) {

                ArrayList<BitRepresentation> clone = BitHandler.cloneBitRepresentationArray(variables);
                
                clone.remove(0);
                for (Iterator it1 = getPermutations(clone); it1.hasNext();) {
                    ArrayList<BitRepresentation> linha = new ArrayList<BitRepresentation>();
                    linha.add(variables.get(0));
                    for (Iterator it2 = ((ArrayList<BitRepresentation>) it1.next()).iterator(); it2.hasNext();) {
                        linha.add((BitRepresentation) it2.next());
                    }
                    permutacoes.add(linha);
                }
                
                variables.add((BitRepresentation) variables.remove(0));
            }
        }

        System.gc();
        
        return permutacoes.iterator();
    }
    
}