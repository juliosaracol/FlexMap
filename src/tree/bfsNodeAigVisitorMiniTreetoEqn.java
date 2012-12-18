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
            if(nodeAigActual.getParents().size()> 1)
            {if(Algorithms.isInverter(nodeAigActual, nodeAigActual.getParents().get(1)))
               eqn += "!["+nodeAigActual.getParents().get(1).getName()+"]);\n";
            else
               eqn += "["+nodeAigActual.getParents().get(1).getName()+"]);\n";
            }
             else
            {
                if(this.tree.contains(nodeAigActual.getChildren().get(0)))
                    System.out.println("NODO SOLITARIO"+ nodeAigActual.getName()+" "+ eqn+ " ligado em :"+ nodeAigActual.getChildren().get(0).getName()+" na arvore ligado a "+nodeAigActual.getChildren().get(0).getChildren().get(0).getName() );
                else
                    System.out.println("NODO SOLITARIO"+ nodeAigActual.getName()+" "+ eqn+ " ligado em :"+ nodeAigActual.getChildren().get(0).getName()+" NAO esta na arvore e mais "+nodeAigActual.getChildren().size() );
                eqn=eqn.substring(0,eqn.length()-2)+";\n";
            }
            this.eqnDescription.put(nodeAigActual,eqn);
        }
    }
}
