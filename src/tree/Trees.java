package tree;

import aig.*;
import java.util.*;


/**
 * Classe que da forma a descrição de Trees a partir de um Aig 
 * @author Julio Saraçol
 */
public abstract class Trees 
{
    protected Aig                   aig;
    protected Set<Tree>             roots       = new HashSet<Tree>();
    protected ArrayList<NodeAig>    TreeNodes   = new ArrayList<NodeAig>();
    public Trees(Aig aig) 
    {
        this.aig = aig;
        Trees();
    }

    protected void Trees()
    {
        for(NodeAig node : aig.getAllNodesAig())
        {
            if((!node.isAnd())||(!node.isOutput()))
              if(node.getChildren().size() > 1)
              {
                  this.TreeNodes.add(node);
                  this.roots.add(new Tree(node));
                  //bfs carregando resto dos nodos 
              }
            
        }
        
        
    }
    
}
