package covering;

import FlexMap.CostFunction;
import aig.Aig;
import aig.AigInverter;
import io.LogsCoveringToEqn;
import java.io.FileNotFoundException;
import kcutter.CutterK;

/**
 * Classe que aplica simlated anneling com inversores expostos
 * @author Julio Saraçol 
 */
public class SimulatedAnnelingInverter extends SimulatedAnneling{

    public SimulatedAnnelingInverter(AigInverter myAig, CutterK kcuts, CostFunction function, int tempInitial, float coolingRate) {
        super(myAig, kcuts, function, tempInitial, coolingRate);
    }

    @Override
    public String getEqn() throws FileNotFoundException {
        return LogsCoveringToEqn.coveringToEqn((AigInverter)this.myAig, bestCut);
    }
    
    
    
    
}
