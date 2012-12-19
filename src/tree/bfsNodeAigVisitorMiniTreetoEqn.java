package tree;

import FlexMap.Algorithms;
import aig.NodeAig;
import aig.bfsNodeAigVisitorAigtoEqn;

/**
 * Classe que aplica caminhamento para gerar eqn de cada árvore
 * @author Julio Saraçol
 */
public class bfsNodeAigVisitorMiniTreetoEqn extends bfsNodeAigVisitorAigtoEqn
{
    protected Tree tree;

    public bfsNodeAigVisitorMiniTreetoEqn(Tree tree) {
        this.tree = tree;
    }
    
    @Override
    public void function(NodeAig nodeAigActual) {
        if((nodeAigActual.isInput()))
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
}
