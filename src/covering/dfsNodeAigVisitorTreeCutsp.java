package covering;

/**
 * Método que aplica a restrição s p através do caminhamento dfs na árvore em questão
 * @author Julio Saraçol
 */

import aig.*;
import tree.*;
import java.util.*;

public class dfsNodeAigVisitorTreeCutsp extends dfsNodeAigVisitor
{
    protected int s;
    protected int p;
    protected Trees         trees;
    protected Tree          tree;
    protected Set<Tree>     newTrees;
    protected TreeMap<String,Integer>    coveringS    = new TreeMap<String,Integer>();
    protected TreeMap<String,Integer>    coveringP    = new TreeMap<String,Integer>(); 
    protected TreeMap<String,Integer>    level        = new TreeMap<String,Integer>(); 


    public dfsNodeAigVisitorTreeCutsp(Trees trees, Tree tree, int s, int p)
    {
        super();
        this.s = s;
        this.p = p;
        this.trees    = trees;
        this.tree     = tree;
        this.newTrees = new HashSet<Tree>();
        coveringS.clear();
        coveringP.clear();
        level.clear();
    }

    @Override
    public void function(NodeAig nodeAigActual) 
    {
        if(nodeAigActual.isInput())
        {
            coveringS.put(nodeAigActual.getName(), 1);
            coveringP.put(nodeAigActual.getName(), 1);
            level.put(nodeAigActual.getName(),0);
            System.out.println(nodeAigActual.getName()+" nivel: "+level.get(nodeAigActual.getName()) +" custo s p: 1-1");
            return;
        }
        if(sumBestCost(nodeAigActual))
            this.level.put(nodeAigActual.getName(), (getBiggerLevel(nodeAigActual)+1));
        else
        {
          cutTree(nodeAigActual);
          function(nodeAigActual);
        }
    }
    
   /**Método que aplica o cálculo dos custos do nodo onde caso o custo ultrapasse s p retorna false
    * @param nodeCurrent (node actual)
    * @return true (cost OK) false (cost > sp)*/
    private boolean sumBestCost(NodeAig nodeCurrent) 
    {
       int costS=0,costP=0;
       if(nodeCurrent.isOR()) //caso OR
       {
           for(int i=0;i<nodeCurrent.getParents().size();i++)
           {
              if(coveringS.get(nodeCurrent.getParents().get(i).getName()) > costS)
                costS = coveringS.get(nodeCurrent.getParents().get(i).getName());
              costP += coveringP.get(nodeCurrent.getParents().get(i).getName());
           }
       }
       else
       {
           for(int i=0;i<nodeCurrent.getParents().size();i++)
           {
              if(coveringP.get(nodeCurrent.getParents().get(i).getName()) > costP)
               costP = coveringP.get(nodeCurrent.getParents().get(i).getName());
              costS += coveringS.get(nodeCurrent.getParents().get(i).getName());
           }   
       }
       if((costP > p)||(costS > s))
       {
           return false;
       }
       coveringS.put(nodeCurrent.getName(), costS);
       coveringP.put(nodeCurrent.getName(), costP);
       System.out.println("Custo do nodo: "+nodeCurrent.getName()+" é s:"+costS+" p:"+costP);
       return true;
   }

    private int getBiggerLevel(NodeAig nodeAigActual) 
    {
        int maxLevel = 0;
        for(NodeAig father: nodeAigActual.getParents())
            if(this.level.get(father.getName()) > maxLevel)
                maxLevel = this.level.get(father.getName());
        return maxLevel;
    }

    private void cutTree(NodeAig nodeAigActual) 
    {
        //faz todas a combinações -> seleciona melhor cost
        //define qual nodo corta cria nodo fake e chama a parte debaixo ae 
        
        Tree newTree = new Tree();
        bfsNodeTreeVisitorCopy bfsCopy =  new bfsNodeTreeVisitorCopy(null, newTree);
        nodeAigActual.accept(bfsCopy);
        this.newTrees.add(newTree);
    }
    
    
    
    
    
}
