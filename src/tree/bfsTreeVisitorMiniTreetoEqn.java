package tree;

import FlexMap.Algorithms;
import aig.NodeAig;
import aig.bfsNodeAigVisitorAigtoEqn;

/**
 * Classe que aplica caminhamento para gerar eqn de cada Tree
 * @author Julio Saraçol
 */
public class bfsTreeVisitorMiniTreetoEqn extends bfsNodeAigVisitorAigtoEqn
{
    protected Tree tree;

    public bfsTreeVisitorMiniTreetoEqn(Tree tree) {
        this.tree = tree;
    }
    
    @Override
    public void function(NodeAig nodeAigActual) {
        if((nodeAigActual.isInput())||((nodeAigActual.getName().equals("0"))||(nodeAigActual.getName().equals("1")))) //constant
            return;
        if(this.handler.contains(nodeAigActual))
            return;
        else
        {    
            String type = "*";
            if(nodeAigActual.isOR())
              type = "+";
            this.handler.add(nodeAigActual);
            String eqn = "["+nodeAigActual.getName()+"]=";
            if(Algorithms.isInverter(nodeAigActual, nodeAigActual.getParents().get(0)))
                eqn += "(!["+nodeAigActual.getParents().get(0).getName()+"])"+type+"(";
            else
                eqn += "(["+nodeAigActual.getParents().get(0).getName()+"])"+type+"(";
            if(Algorithms.isInverter(nodeAigActual, nodeAigActual.getParents().get(1)))
               eqn += "!["+nodeAigActual.getParents().get(1).getName()+"]);\n";
            else
               eqn += "["+nodeAigActual.getParents().get(1).getName()+"]);\n";
            this.eqnDescription.put(nodeAigActual,eqn);
        }
    }
}
