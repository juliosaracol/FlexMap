package covering;

import aig.*;
import kcutter.*;
import java.util.Map;
/**
 * Classe que aplica o caminhamento para cobertura no AreaLibrary
 * @author Julio Saraçol
 */
public class bfsAigVisitorAreaLibraryCovering extends bfsNodeAigVisitor{

    protected AreaFlowLibrary myArea;

    public bfsAigVisitorAreaLibraryCovering(AreaFlowLibrary myArea) 
    {
        super();
        this.myArea = myArea;  
    }    
    
    @Override
    public void function(NodeAig nodeAigActual) 
    {
        boolean flag =false;
        if(!myArea.covering.containsKey(nodeAigActual))
        {                     
            for(Map.Entry<NodeAig,AigCutBrc> entry: myArea.covering.entrySet()) 
            {
                if(entry.getValue().contains(nodeAigActual))
                    flag = true;
            }
            if(flag == true)
              myArea.covering.put(nodeAigActual,myArea.bestCut.get(nodeAigActual));        
        }
    }    
}
