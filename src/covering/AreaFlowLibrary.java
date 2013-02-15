/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package covering;

import FlexMap.CostAreaFlow;
import aig.Aig;
import aig.NodeAig;
import kcutter.*;
import library.*;
import java.util.*;

/**
 * Classe que estende ArreaFlow utilizando apenas K-cuts com Matching na biblioteca
 * @author Julio Saraçol
 */
public class AreaFlowLibrary extends AreaFlow
{
    //**Atributo que é referente aos matchings selecionados
    protected Map<AigCutBrc,Set<FunctionData>> signature;
            
    public AreaFlowLibrary(Aig myAig, int size, CutterKCutsLibrary cutterK, CostAreaFlow function) {
        super(myAig, size, cutterK, function);
        signature = cutterK.getMatchings();
    }

    @Override
    protected void getBestArea(NodeAig nodeActual) {
        super.getBestArea(nodeActual);
    }

    @Override
    protected void choiceBestArea(NodeAig nodeActual, Map<AigCut, Float> tableCost) {
        super.choiceBestArea(nodeActual, tableCost);
    }
    
    @Override
    protected float sumCost(AigCut cut, NodeAig nodeActual) {
        return super.sumCost(cut, nodeActual);
    }

    
    
    
}
