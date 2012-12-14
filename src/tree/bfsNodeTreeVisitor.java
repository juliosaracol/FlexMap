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
    protected TreeMap<String,NodeAig> nodesNames;

    public bfsNodeTreeVisitor(ArrayList<String> TreeNodes,Tree tree) {
        this.treeNodes  = TreeNodes;
        this.tree       = tree;
        this.nodesNames = new TreeMap<String, NodeAig>();
        this.nodesNames.put(this.tree.getRoot().getName(),this.tree.getRoot());
    }

    @Override
    public void function(NodeAig nodeAigActual) 
    {
        if(!this.nodesNames.containsKey(nodeAigActual.getName()))
        {
            if(this.treeNodes.contains(nodeAigActual.getName())&&(!this.tree.getRoot().getName().equals(nodeAigActual.getName()))) //caso de treeNode no meio do subgrafo
            {
              for(NodeAig child: nodeAigActual.getChildren())
                if((this.nodesNames.containsKey(child.getName()))&&(!(this.nodesNames.get(child.getName()).isInput())))
                {
                      NodeAigInput newNode;
                      newNode = new NodeAigInput(nodeAigActual.getId(), nodeAigActual.getName());
                      if(!this.nodesNames.containsKey(nodeAigActual.getName()))
                      {
                       tree.add(newNode);
                       tree.addEdge(this.nodesNames.get(child.getName()),newNode, Algorithms.isInverter(child, nodeAigActual));
                       this.nodesNames.put(newNode.getName(),newNode);
                      }
                }
              return;
            }
            if(nodeAigActual.isInput()) //caso de entrada primaria do circuito
            {
                NodeAigInput newNode;  
                newNode = new NodeAigInput(nodeAigActual.getId(), nodeAigActual.getName());
                for(NodeAig child: nodeAigActual.getChildren())
                   if(((this.treeNodes.contains(child.getName()))&&(this.tree.getRoot().getName().equals(child.getName())))
                       ||((!this.treeNodes.contains(child.getName()))&&(this.nodesNames.containsKey(child.getName()))))//insere se o treenode pai é a raiz ou se não é treenode e o pai esta na arvore
                   {
                           if(!this.nodesNames.containsKey(nodeAigActual.getName()))
                           {
                            tree.addEdge(this.nodesNames.get(child.getName()),newNode, Algorithms.isInverter(child, nodeAigActual));
                            this.nodesNames.put(newNode.getName(),newNode);
                            tree.add(newNode);
                           }
                   }                       
                return;
            }
            for(NodeAig child: nodeAigActual.getChildren())
                   if(((this.treeNodes.contains(child.getName()))&&(this.tree.getRoot().getName().equals(child.getName())))
                       ||((!this.treeNodes.contains(child.getName()))&&(this.nodesNames.containsKey(child.getName()))))
                   {
                       NodeAigGate newNode;  
                       newNode = new NodeAigGate(nodeAigActual.getId(), nodeAigActual.getName());
                       if(!this.nodesNames.containsKey(nodeAigActual.getName()))
                       {
                         tree.addEdge(this.nodesNames.get(child.getName()),newNode, Algorithms.isInverter(child, nodeAigActual));
                         tree.add(newNode);
                         this.nodesNames.put(newNode.getName(),newNode);
                       }
                   }          
        }
    }
}
