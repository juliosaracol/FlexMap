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
        if(nodeAigActual.isInput()) //caso de entrada primaria do circuito
        {
            boolean flag = false;
            NodeAigInput newNode;  
            newNode = new NodeAigInput(nodeAigActual.getId(), nodeAigActual.getName());
            for(NodeAig child: nodeAigActual.getChildren())
            {   
                if(((this.treeNodes.contains(child.getName()))&&(this.tree.getRoot().getName().equals(child.getName())))
                   ||((!this.treeNodes.contains(child.getName()))&&(this.nodesNames.containsKey(child.getName()))))//insere se o treenode pai é a raiz, ou se o pai esta na árvore
               {
                   flag= true;
                   if(!this.nodesNames.containsKey(nodeAigActual.getName()))
                   {
                    tree.addEdge(this.nodesNames.get(child.getName()),newNode, Algorithms.isInverter(child, nodeAigActual));
                    this.nodesNames.put(newNode.getName(),newNode);
                    tree.add(newNode);
                   }
                   else
                    tree.addEdge(this.nodesNames.get(child.getName()),this.nodesNames.get(nodeAigActual.getName()), Algorithms.isInverter(child, nodeAigActual));                               
               }                
            }
            if(flag == false) //nodos reconvergentes
                this.nodesBfs.remove(nodeAigActual);  
            return;
        }
        if(this.treeNodes.contains(nodeAigActual.getName())&&(!this.tree.getRoot().getName().equals(nodeAigActual.getName()))) //caso de treeNode no meio do subgrafo
        {
          boolean flag = false;
          for(NodeAig child: nodeAigActual.getChildren())
          {
            if((this.nodesNames.containsKey(child.getName()))&&(!(this.nodesNames.get(child.getName()).isInput())))
            {
              flag = true;  
              NodeAigInput newNode;
              newNode = new NodeAigInput(nodeAigActual.getId(), nodeAigActual.getName());
              if(!this.nodesNames.containsKey(nodeAigActual.getName()))
              {
               tree.add(newNode);
               tree.addEdge(this.nodesNames.get(child.getName()),newNode, Algorithms.isInverter(child, nodeAigActual));
               this.nodesNames.put(newNode.getName(),newNode);
              }
              else
                tree.addEdge(this.nodesNames.get(child.getName()),this.nodesNames.get(nodeAigActual.getName()), Algorithms.isInverter(child, nodeAigActual));                               
            }
          }
          if(flag == false) //nodos reconvergentes
           this.nodesBfs.remove(nodeAigActual);  
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
               else
                 tree.addEdge(this.nodesNames.get(child.getName()),this.nodesNames.get(nodeAigActual.getName()), Algorithms.isInverter(child, nodeAigActual));                               
          }          
    }

    @Override
    public void visit(NodeAigGate NodeAigGate) {
        if(!nodesBfs.contains(NodeAigGate))
        {
            nodesBfs.add(NodeAigGate);
            for(int i=0;i<NodeAigGate.getParents().size();i++)
            {
                if(!list.contains(NodeAigGate.getParents().get(i))&&(!nodesBfs.contains(NodeAigGate.getParents().get(i))))
                {
                    list.add(NodeAigGate.getParents().get(i));
                    this.states++;
                }
            }
        }        
        function(NodeAigGate);
        if(list.size() > 0)
        {
            NodeAig temp =  list.get(0);
            list.remove(0);
            temp.accept(this);
        }
    }    
}
