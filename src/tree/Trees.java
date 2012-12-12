package tree;

import aig.*;
import java.util.*;


/**
 * Classe que da forma a descrição de Trees a partir de um Aig 
 * @author Julio Saraçol
 */
public class Trees 
{
    protected Aig                   aig;
    protected Set<Tree>             roots       = new HashSet<Tree>();
    protected ArrayList<NodeAig>    treeNodes   = new ArrayList<NodeAig>();
    
    public Trees(Aig aig) 
    {
        this.aig = aig;
        for(NodeAig node : aig.getAllNodesAig())
        {
            if(!node.isInput())
            {
              if((node.getChildren().size() > 1)||(node.isOutput()))
              {
                  this.treeNodes.add(node);
                  Tree newTree = new Tree(node);
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
