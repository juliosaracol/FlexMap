package covering;

import aig.NodeAig;
import aig.bfsNodeAigVisitor;
import kcutter.AigCut;
import java.util.*;

/**
 *
 * @author julio
 */
public class bfsAigVisitorCoveringSimulated extends bfsNodeAigVisitor{

    protected  Map<NodeAig,AigCut> coveringActual;
    protected  Map<NodeAig,AigCut> bestCuts;
    protected  Map<NodeAig,Float> tableCost;
    
    public bfsAigVisitorCoveringSimulated(Map<NodeAig,AigCut> coveringActual,Map<NodeAig,AigCut> bestCuts) {
        this.bestCuts             = bestCuts;  
        this.coveringActual       = coveringActual;
    }
   
    @Override
    public void function(NodeAig nodeAigActual) {
        boolean flag =false;
        if(!this.coveringActual.containsKey(nodeAigActual))
        {                     
            for(Map.Entry<NodeAig,AigCut> entry: coveringActual.entrySet()) 
            {                
                if(entry.getValue().contains(nodeAigActual))
                    flag = true;
            }
            if(flag == true){
                coveringActual.put(nodeAigActual,this.bestCuts.get(nodeAigActual));
                //System.out.println("trocando a cobertura"+nodeAigActual.getName()+" cut novo : "+this.bestCuts.get(nodeAigActual));
            }
        }
    }
    
}
