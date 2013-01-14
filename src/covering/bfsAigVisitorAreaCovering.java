package covering;

import aig.*;
import kcutter.*;
import java.util.*;
/**
 * Classe para aplicar cobertura baseada em Area
 * @author Julio Saraçol
 */
public class bfsAigVisitorAreaCovering  extends bfsNodeAigVisitor
{
    protected AreaFlow myArea;

    public bfsAigVisitorAreaCovering(AreaFlow myArea) 
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
            for(Map.Entry<NodeAig,AigCut> entry: myArea.covering.entrySet()) 
            {
                if(entry.getValue().contains(nodeAigActual))
                    flag = true;
            }
            if(flag == true)
              myArea.covering.put(nodeAigActual,myArea.bestCut.get(nodeAigActual));        
        }
    } 
}
