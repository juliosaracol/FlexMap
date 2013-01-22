package tree;

import aig.*;
import io.*;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Classe que da forma a descrição de Trees a partir de um Aig 
 * @author Julio Saraçol
 */
public abstract class Trees 
{
    protected Aig                 aig;
    protected Set<Tree>           roots     = new HashSet<Tree>();
    protected ArrayList<String> treeNodes   = new ArrayList<String>();
    
    public Trees(Aig aig) 
    {
        this.aig = aig;
        for(NodeAig node :aig.getAllNodesAig())
        {
            if((!node.isInput())&&(!this.treeNodes.contains(node.getName())))
            {
              if((aig.getNodeOutputsAig().contains(node))||(node.getChildren().size() > 1))
              {
                  if((Integer.parseInt(node.getName())%2)!= 0)
                  {
                     if(node.getParents().get(0).getChildren().size() == 1)
                     {
                            NodeAig newNode = null;
                            this.treeNodes.add(node.getName());
                            newNode = new NodeAigOutput(node.getId(),node.getName());
                            Tree newTree = new Tree(newNode);
                            this.roots.add(newTree);
                     }
//                     else
//                     {
//                        NodeAig newNode   = null;
//                        NodeAig newFather = null;
//                        this.treeNodes.add(node.getName());
//                        newNode = new NodeAigOutput(node.getId(),node.getName());
//                        Tree newTree = new Tree(newNode);
//                        this.roots.add(newTree);
//                        if(node.getParents().get(0).isOR())
//                            newFather = new NodeAigGateOr(node.getParents().get(0).getId(),node.getParents().get(0).getName());
//                        else
//                            newFather = new NodeAigGate(node.getParents().get(0).getId(),node.getParents().get(0).getName());
//                        newTree.add(newFather);
//                        newTree.addEdge(newTree.getRoot(),newFather, true);
//                     }
                  }
                  else
                  {
                        NodeAig newNode = null;
                        this.treeNodes.add(node.getName());
                        newNode = new NodeAigOutput(node.getId(),node.getName());
                        Tree newTree = new Tree(newNode);
                        this.roots.add(newTree);
                  }
              }
              
            }
        }
        for(Tree tree: this.roots)
        {
             bfsTreeVisitorCopy bfs = new bfsTreeVisitorCopy(treeNodes,tree);
             aig.getVertexName(tree.getRoot().getName()).accept(bfs);
        }
    }

    public void show() {
        for(Tree cone: roots)
        {
            cone.show();
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
        String out = Logs.TreesToEqn(this);
        return out;
    }
    
    public ArrayList<String> getEqnTrees() throws FileNotFoundException
    {
        ArrayList<String> out= new ArrayList<String>();
        for(Tree tree: this.getRoots())
        {
            if(tree.getTree().size()>2)
            {
             String out1 = tree.getEqn();
             out.add(out1);
             System.out.println(out.get(out.size()-1));
            }
        }
        return out;
    }
    
}
