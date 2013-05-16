
package covering;

import FlexMap.CostAreaFlow;
import aig.*;
import kcutter.*;
import io.*;
import java.io.FileNotFoundException;

/**
 * Classe que aplica a cobertura de acordo com algoritmo de AreaFlow em Aig_Inverter (Valavan,2006)
 * @author Julio Saraçol
 */
public class AreaFlowInverter extends AreaFlow{
   
    public AreaFlowInverter(AigInverter myAig, int size, CutterK cutterK, CostAreaFlow function) {
        super(myAig, size, cutterK, function);
    }

    @Override
    public String getEqn() throws FileNotFoundException
    {
       String eqn = Logs.coveringToEqn((AigInverter)myAig, getCoveringCuts());
       return eqn;
    }    
    
}
