package tree;

import FlexMap.Algorithms;
import aig.*;
import java.util.*;
/**
 * Classe que implementa caminhamento para gerar a cópia do subgrafo do Aig
 * @author Julio Saraçol
 */
public class bfsNodeTreeVisitorCopy extends bfsNodeAigVisitor
{
    protected ArrayList<String> treeNodes;
    protected Tree tree ;
    protected TreeMap<String,NodeAig> nodesNames;

    public bfsNodeTreeVisitorCopy(ArrayList<String> TreeNodes,Tree tree) {
        super();
        this.treeNodes  = TreeNodes;
        this.tree       = tree;
        this.nodesNames = new TreeMap<String, NodeAig>();
        this.nodesNames.put(this.tree.getRoot().getName(),this.tree.getRoot());
    }

    @Override
    public void function(NodeAig nodeAigActual) 
    {
       if(this.nodesNames.containsKey(nodeAigActual.getName()))
       {
        NodeAig node=null;
        if(nodeAigActual.isInput())
           return;
        if((treeNodes.contains(nodeAigActual.getName()))&&(!this.tree.getRoot().getName().equals(nodeAigActual.getName())))
            return;
        for(NodeAig father: nodeAigActual.getParents())
        {
            if(this.treeNodes.contains(father.getName()))
            {
              if(!this.nodesNames.containsKey(father.getName()))
              {
                NodeAig newNode = new NodeAigInput(father.getId(), father.getName());
                tree.addEdge(this.nodesNames.get(nodeAigActual.getName()),newNode, Algorithms.isInverter(nodeAigActual,father));
                this.nodesNames.put(newNode.getName(),newNode);
                tree.add(newNode);
              }
              else
                tree.addEdge(this.nodesNames.get(nodeAigActual.getName()),this.nodesNames.get(father.getName()),
                        Algorithms.isInverter(nodeAigActual,father));                  
            }
            else
            {
              if(!this.nodesNames.containsKey(father.getName()))
              {
                if(father.isInput())
                  node = new NodeAigInput(father.getId(), father.getName());                
                if(father.isAnd())
                  node = new NodeAigGate(father.getId(), father.getName());
                if(father.isOR())
                  node = new NodeAigGateOr(father.getId(), father.getName());
                tree.addEdge(this.nodesNames.get(nodeAigActual.getName()),node, Algorithms.isInverter(nodeAigActual,father));
                this.nodesNames.put(node.getName(),node);
                tree.add(node);                
              }
              else
                tree.addEdge(this.nodesNames.get(nodeAigActual.getName()),this.nodesNames.get(father.getName()), 
                        Algorithms.isInverter(nodeAigActual,father));                             
            }
         }
        }
      }         
}
