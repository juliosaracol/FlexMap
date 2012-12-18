package tree;

import aig.*;
import io.*;
import java.io.FileNotFoundException;
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
            if((!node.isInput())&&(!this.roots.contains(node)))
            {
              if((aig.getNodeOutputsAig().contains(node))||(node.getChildren().size() > 1))
              {
                  if(!(((Integer.parseInt(node.getName())%2)!= 0)&&(node.getParents().get(0).getChildren().size() > 1)))
                  {
                      NodeAig newNode = null;
                      this.treeNodes.add(node.getName());
                      newNode = new NodeAigOutput(node.getId(), node.getName());
                      Tree newTree = new Tree(newNode);
                      this.roots.add(newTree);
                  }
              }
              
            }
        }
        for(Tree tree: this.roots)
        {
            bfsNodeTreeVisitorCopy bfs = new bfsNodeTreeVisitorCopy(treeNodes,tree);
            aig.getVertexName(tree.getRoot().getName()).accept(bfs);
        }
    }

    public void show() {
        for(Tree cone: roots)
        {
            cone.show();
            cone.getEqn();
        }
    }

    public Aig getAig() {
        return aig;
    }

    public Set<Tree> getRoots() {
        return roots;
    }

    public ArrayList<String> getTreeNodes() {
        return treeNodes;
    }
    
    public String getEqn() throws FileNotFoundException
    {
        String out = Logs.TreeToEqn(this);
        return out;
    }
    
    
}
