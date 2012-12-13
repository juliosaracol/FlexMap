package tree;

import aig.*;
import java.util.*;


/**
 * Classe que da forma a descrição de Trees a partir de um Aig 
 * @author Julio Saraçol
 */
public class Trees 
{
    protected Aig                  aig;
    protected Set<Tree>            roots       = new HashSet<Tree>();
    protected ArrayList<String>    treeNodes   = new ArrayList<String>();
    
    public Trees(Aig aig) 
    {
        this.aig = aig;
        for(NodeAig node :aig.getAllNodesAig())
        {
            if(!node.isInput())
            {
              if((node.getChildren().size() > 1)||(node.isOutput()))
              {
                  NodeAig newNode = null;
                  this.treeNodes.add(node.getName());
                  if(node.isOutput())
                      newNode = new NodeAigOutput(node.getId(), node.getName());
                  if(node.isAnd())
                      newNode = new NodeAigGate(node.getId(), node.getName());
                  Tree newTree = new Tree(newNode);
                  this.roots.add(newTree);
                  bfsNodeTreeVisitor bfs = new bfsNodeTreeVisitor(treeNodes,newTree);
                  newTree.getRoot().accept(bfs);
              }
            }
        }
    }

    public void show() {
        for(Tree cone: roots)
            cone.show();
    }
}
