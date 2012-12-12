package tree;

import aig.NodeAig;
import aig.NodeAigGate;
import aig.NodeAigInput;
import aig.NodeAigOutput;
import aig.bfsNodeAigVisitor;
import expanderExpression.Node;
import java.util.ArrayList;

/**
 * Classe que implementa caminhamento para gerar a cópia do subgrafo do Aig
 * @author Julio Saraçol
 */
public class bfsNodeTreeVisitor extends bfsNodeAigVisitor
{
    protected ArrayList<NodeAig> treeNodes;
    protected Tree tree ;
    public bfsNodeTreeVisitor(ArrayList<NodeAig> TreeNodes,Tree tree) {
        this.treeNodes  = TreeNodes;
        this.tree       = tree;
    }

    @Override
    public void function(NodeAig nodeAigActual) 
    {
       if(!treeNodes.contains(nodeAigActual))
       {
        if(nodeAigActual.isInput())
        {
          NodeAigInput newNode;  
          if(!tree.contains(nodeAigActual))
          {
           newNode = new NodeAigInput(nodeAigActual.getId(), nodeAigActual.getName());
           tree.add(newNode);
          }
          else
              newNode = (NodeAigInput) nodeAigActual;
          for(NodeAig node: nodeAigActual.getChildren())
              if(tree.contains(node.getName()))
                tree.addEdge(node, newNode, false);              
        }
        if(nodeAigActual.isOutput())
        {
          NodeAigOutput newNode;
          if(!tree.contains(nodeAigActual))
          {
              newNode = new NodeAigOutput(nodeAigActual.getId(), nodeAigActual.getName());
              tree.add(newNode);
          }
          else
              newNode = (NodeAigOutput) nodeAigActual;
          for(NodeAig node: nodeAigActual.getChildren())
             if(tree.contains(node.getName()))
               tree.addEdge(node,newNode, false);              
        }
        if(nodeAigActual.isAnd())
        {
          NodeAigGate newNode;
          if(!tree.contains(nodeAigActual))
          {
            newNode = new NodeAigGate(nodeAigActual.getId(), nodeAigActual.getName());
            tree.add(newNode);
          }
          else
              newNode = (NodeAigGate) nodeAigActual;
          for(NodeAig node: nodeAigActual.getChildren())
              if(tree.contains(node.getName()))
                tree.addEdge(node,newNode, false);              
        }
       }
    }
}
