package covering;

/**
 * Classe que aplica o caminhamento da subarvore a partir do nodo "cortado" na Elis
 * @author Julio Saraçol
 */

import FlexMap.Algorithms;
import aig.*;
import tree.*;
import java.util.*;

public class bfsTreeVisitorElisCopyAndCut extends bfsNodeAigVisitor
{

    protected Tree tree;
    protected TreeMap<String,NodeAig> nodesNames;
    protected Set<NodeAig> deletedNodes;
    
    public bfsTreeVisitorElisCopyAndCut() 
    {
        super();
        nodesNames          = new TreeMap<String, NodeAig>();
        this.deletedNodes   = new HashSet<NodeAig>();
    }

    
    @Override
    public void function(NodeAig nodeAigActual) 
    {
        NodeAig newNode=null;
        if(tree == null)
        {
           if(nodeAigActual.isInput())
               newNode = new NodeAigInput(nodeAigActual.getId(), nodeAigActual.getName());
           if(nodeAigActual.isOR())
               newNode = new NodeAigGateOr(nodeAigActual.getId(), nodeAigActual.getName());
           if(nodeAigActual.isAnd()||nodeAigActual.isOutput())
               newNode = new NodeAigGate(nodeAigActual.getId(), nodeAigActual.getName());
           this.deletedNodes.add(nodeAigActual);
           this.nodesNames.put(newNode.getName(), newNode);
           this.tree = new Tree(newNode);           
        }
        for(NodeAig father: nodeAigActual.getParents())
        {
           if(!this.nodesNames.containsKey(father.getName()))
           {
               if(father.isInput())
                   newNode = new NodeAigInput(father.getId(), father.getName());
               if(father.isOR())
                   newNode = new NodeAigGateOr(father.getId(), father.getName());
               if(father.isAnd()||father.isOutput())
                   newNode = new NodeAigGate(father.getId(), father.getName());
               NodeAig child = this.nodesNames.get(nodeAigActual.getName());
               tree.addEdge(child,newNode, Algorithms.isInverter(nodeAigActual,father));
               this.deletedNodes.add(father);
               this.nodesNames.put(newNode.getName(),newNode);
               tree.add(newNode);
           }
           else
              tree.addEdge(this.nodesNames.get(nodeAigActual.getName()),this.nodesNames.get(father.getName()),Algorithms.isInverter(nodeAigActual,father));                  
        }
    }

    public TreeMap<String, NodeAig> getNodesNames() {
        return nodesNames;
    }

    public Tree getTree() {
        return tree;
    }

    public Set<NodeAig> getDeletedNodes() {
        return deletedNodes;
    }
    
    
    
}
