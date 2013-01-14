package tree;

import aig.*;
import FlexMap.Algorithms;
/**
 * Classe que aplica caminhamento transformando nodos da TREE em nodos OR
 * @author Julio Saraçol
 */
public class dfsTreeVisitorOr extends dfsNodeAigVisitor
{
    protected Trees trees;
    protected Tree tree;

    public dfsTreeVisitorOr(Trees trees,Tree tree) 
    {
        super();
        this.trees  = trees;
        this.tree   = tree;
    }

    @Override
    public void function(NodeAig nodeAigActual) 
    {
        if(nodeAigActual.isInput()||nodeAigActual.isOutput())
            return;
        if(nodeAigActual.getChildren().size() > 1)
            return;
        for(NodeAig father : nodeAigActual.getParents())
            if(!Algorithms.isInverter(nodeAigActual, father))
                return;
        if(Algorithms.isInverter(nodeAigActual.getChildren().get(0),nodeAigActual))
            setOr(nodeAigActual);        
    }

    private void setOr(NodeAig nodeAigActual)
    {
        NodeAigGateOr newNode = new NodeAigGateOr(nodeAigActual.getId(),nodeAigActual.getName());
        for(NodeAig neighbours : nodeAigActual.getParents())
            tree.addEdge(newNode, neighbours, false);
        tree.add(newNode);
        if(((nodeAigActual.getChildren().get(0).isOutput())&&((Integer.parseInt(nodeAigActual.getChildren().get(0).getName()))%2!=0)))
        {
            tree.getTree().remove(nodeAigActual.getChildren().get(0));
            tree.setRoot(newNode);            
        }
        else
            tree.addEdge(nodeAigActual.getChildren().get(0),newNode, false);
       for(NodeAig neighbours : nodeAigActual.getParents())
            if(Algorithms.isInverter(nodeAigActual, neighbours))
                tree.removeEdge(Algorithms.getEdge(nodeAigActual,neighbours).getId());
        tree.removeEdge(Algorithms.getEdge(nodeAigActual.getChildren().get(0),nodeAigActual).getId());
        this.tree.getTree().remove(nodeAigActual);
        System.out.println("SET NODO :"+newNode.getName()+" "+newNode.isOR());
    }
}
