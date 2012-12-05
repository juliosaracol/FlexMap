package aig;

import FlexMap.Algorithms;
import java.util.*;
/**
 * Classe que aplica o caminhamento do AIG para gerar descrição EQN
 * @author Julio Saraçol
 */
public class bfsNodeAigVisitorAigtoEqn extends bfsNodeAigVisitor{
    
    public Set<NodeAig> handler                  = new HashSet<NodeAig>();
    protected Map<NodeAig,String> eqnDescription = new HashMap<NodeAig, String>();
    
    @Override
    public void function(NodeAig nodeAigActual) {
        if((nodeAigActual.isInput())||(nodeAigActual.isOutput()))
            return;
        if(this.handler.contains(nodeAigActual))
            return;
        else
        {            
            this.handler.add(nodeAigActual);
            String eqn = "["+nodeAigActual.getName()+"]=";
            if(Algorithms.isInverter(nodeAigActual, nodeAigActual.getParents().get(0)))
                eqn += "(!["+nodeAigActual.getParents().get(0).getName()+"])*(";
            else
                eqn += "(["+nodeAigActual.getParents().get(0).getName()+"])*(";
            if(Algorithms.isInverter(nodeAigActual, nodeAigActual.getParents().get(1)))
                eqn += "!["+nodeAigActual.getParents().get(1).getName()+"]);\n";
            else
                eqn += "["+nodeAigActual.getParents().get(1).getName()+"]);\n";
            this.eqnDescription.put(nodeAigActual,eqn);
        }
    }
    public String getEqnDescription()
    {
        String eqn ="";
        for(Map.Entry<NodeAig,String> node: this.eqnDescription.entrySet())
            eqn+=node.getValue();
        return eqn;
    }   
    
}
