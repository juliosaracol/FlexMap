package tree;

import FlexMap.Algorithms;
import aig.*;
import java.util.*;
/**
 * Classe que implementa caminhamento para gerar a cópia do subgrafo do Aig
 * @author Julio Saraçol
 */
public class bfsNodeTreeVisitor extends bfsNodeAigVisitor
{
    protected ArrayList<String> treeNodes;
    protected Tree tree ;
    protected ArrayList<String> nodesNames;

    public bfsNodeTreeVisitor(ArrayList<String> TreeNodes,Tree tree) {
        this.treeNodes  = TreeNodes;
        this.tree       = tree;
        this.nodesNames = new ArrayList<String>();
        this.nodesNames.add(this.tree.getRoot().getName());
    }

    @Override
    public void function(NodeAig nodeAigActual) 
    {
       if(!tree.contains(nodeAigActual))
       {
        if(nodeAigActual.isOutput())
        {
          NodeAigOutput newNode;
          if(treeNodes.contains(nodeAigActual.getName()))
          {
              newNode = new NodeAigOutput(nodeAigActual.getId(), nodeAigActual.getName());
              tree.add(newNode);
              for(NodeAig child: nodeAigActual.getChildren())
                  if(this.nodesNames.contains(child.getName()))
                      tree.addEdge(child,newNode,  Algorithms.isInverter(child, newNode));              
          }
        }
        if(nodeAigActual.isInput())
        {
          if(!this.nodesNames.contains(nodeAigActual.getName()))
          {
            NodeAigInput newNode;  
            newNode = new NodeAigInput(nodeAigActual.getId(), nodeAigActual.getName());
            for(NodeAig child: nodeAigActual.getChildren())
               if((this.treeNodes.contains(child.getName()))&&(this.tree.getRoot().getName().equals(child.getName())))
               {
                   tree.addEdge(child,newNode,  Algorithms.isInverter(child, newNode));
                   tree.add(newNode);
                   this.nodesNames.add(newNode.getName());
               }
          }
        }
        if(nodeAigActual.isAnd())
        {
          if(!this.nodesNames.contains(nodeAigActual.getName()))
          {
            for(NodeAig child: nodeAigActual.getChildren())
               if((this.treeNodes.contains(child.getName()))&&(this.tree.getRoot().getName().equals(child.getName())))
               {
                   NodeAigGate newNode;  
                   newNode = new NodeAigGate(nodeAigActual.getId(), nodeAigActual.getName());
                   tree.addEdge(child,newNode,  Algorithms.isInverter(child, newNode));
                   tree.add(newNode);
                   this.nodesNames.add(newNode.getName());
               }
          }

         }
       }
    }
}
