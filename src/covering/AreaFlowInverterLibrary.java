package covering;

import FlexMap.CostAreaFlow;
import aig.Aig;
import aig.AigInverter;
import io.LogsCoveringToEqn;
import java.io.FileNotFoundException;
import kcutter.CutterKCutsLibrary;

/**
 * Classe para aplicar o areaflow com aig_inverters
 * @author Julio Saraçol
 */
public class AreaFlowInverterLibrary extends AreaFlowLibrary{

    public AreaFlowInverterLibrary(Aig myAig, int size, CutterKCutsLibrary cutterK, CostAreaFlow function) {
        super(myAig, size, cutterK, function);
    }
    
    @Override
    public String getEqn() throws FileNotFoundException
    {
       String eqn = LogsCoveringToEqn.coveringToEqn((AigInverter)myAig, getCoveringCuts(),this.bestCell);
       return eqn;
    }

}
