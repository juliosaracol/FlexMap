package covering;

/**
 * Método que aplica a restrição s p através do caminhamento dfs na árvore em questão
 * @author Julio Saraçol
 */

import FlexMap.*;
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
          System.out.println("CutTree nodo: "+nodeAigActual.getName());
          //cutTree(nodeAigActual);
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
           for(NodeAig father: nodeCurrent.getParents())
           {
              if((!coveringP.containsKey(father.getName()))||(!coveringS.containsKey(father.getName())))
                      function(father);
              if(coveringS.get(father.getName()) > costS)
                costS = coveringS.get(father.getName());
              costP += coveringP.get(father.getName());
           }
       }
       else
       {
           for(NodeAig father: nodeCurrent.getParents())
           {
              if((!coveringP.containsKey(father.getName()))||(!coveringS.containsKey(father.getName())))
                      function(father);
              if(coveringP.get(father.getName()) > costP)
               costP = coveringP.get(father.getName());
              costS += coveringS.get(father.getName());
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
        ArrayList<ArrayList<Integer>> cost      = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<NodeAig>> choices   = new ArrayList<ArrayList<NodeAig>>();
        boolean solution=false;
        int selected = choiceDinamic(nodeAigActual,choices,cost);
        for(int i=0;i<choices.get(selected).size();i++)
        {
            if((coveringP.get(choices.get(selected).get(i).getName()) > 1)||
                    (coveringS.get(choices.get(selected).get(i).getName()) > 1))
            {   solution = false; break; }            
        }
        if(solution == false) //caso de corte na árvore
        {
           Tree newTree = new Tree();
           bfsNodeTreeVisitorCopy bfsCopy =  new bfsNodeTreeVisitorCopy(null, newTree);
           nodeAigActual.accept(bfsCopy);
           this.newTrees.add(newTree);
           for(NodeAig deleted: newTree.getTree())
               tree.removeVertex(deleted.getId());
        }
        else //nodos com cost 1,1
        {
            
        }
    }
    
    //**Método que faz todas a combinações de corte e seleciona a melhor alternativa do conjunto de nodos disponiveis
    private int choiceDinamic(NodeAig nodeAigActual, ArrayList<ArrayList<NodeAig>> choices, ArrayList<ArrayList<Integer>> cost)
    {
        int costS,costP,indexBest=0,levelMax=0,combination;
        ArrayList<Integer> levelCombination = new ArrayList<Integer>();
        if(nodeAigActual.isOR())
            combination = p;
        else
            combination = s;           
            
        for(int i=1;i<=combination;i++)
        {
          //gera todas as combinações dos filhos
          ArrayList<ArrayList<Integer>> indexCombinations  = 
                  CombinationGenerator.getCombinations(nodeAigActual.getParents().size(),i);
          for(int j=0;j<indexCombinations.size();j++) 
          {
              choices.add(new ArrayList<NodeAig>()); //separa combinacao
              cost.add(new ArrayList<Integer>());    //calcula o custo da combinacao
              costS =0;
              costP =0;
              levelMax=0;
              for(int w=0;w<indexCombinations.get(j).size();w++)
              {
                  choices.get(choices.size()-1).add(nodeAigActual.getParents().get(indexCombinations.get(j).get(w)));
                  if(nodeAigActual.isOR())
                  {
                     if(costS < coveringS.get(nodeAigActual.getParents().get(indexCombinations.get(j).get(w)).getName()))
                         costS = coveringS.get(nodeAigActual.getParents().get(indexCombinations.get(j).get(w)).getName());
                     costP += coveringP.get(nodeAigActual.getParents().get(indexCombinations.get(j).get(w)).getName());                  
                     levelMax +=level.get(nodeAigActual.getParents().get(indexCombinations.get(j).get(w)).getName());
                  }    
                  else
                  {
                     costS += coveringS.get(nodeAigActual.getParents().get(indexCombinations.get(j).get(w)).getName());
                     if(costP < coveringP.get(nodeAigActual.getParents().get(indexCombinations.get(j).get(w)).getName()))
                        costP = coveringP.get(nodeAigActual.getParents().get(indexCombinations.get(j).get(w)).getName());                     
                     levelMax +=level.get(nodeAigActual.getParents().get(indexCombinations.get(j).get(w)).getName());
                  }
              }
              levelCombination.add(levelMax);
              if(nodeAigActual.isOR())
              {
                  cost.get(cost.size()-1).add(costP);
                  cost.get(cost.size()-1).add(costS);
                  if((cost.get(indexBest).get(0) <= costP)&&(costP <= p))
                    if((cost.get(indexBest).get(1) <= costS)&&(costS <= s))
                        if((cost.get(indexBest).get(0) < costP)||(cost.get(indexBest).get(1) < costS))
                            indexBest = cost.size()-1;                        
                        else
                            if(levelCombination.get(levelCombination.size()-1) <= levelCombination.get(indexBest))
                                indexBest = cost.size()-1;
              }
              else
              {
                  cost.get(cost.size()-1).add(costS);
                  cost.get(cost.size()-1).add(costP);
                  if((cost.get(indexBest).get(0) <= costS)&&(costS <= s))
                    if((cost.get(indexBest).get(1) <= costP)&&(costP <= p))
                        if((cost.get(indexBest).get(0) < costS)||(cost.get(indexBest).get(1) < costP))
                             indexBest = cost.size()-1;
                        else
                            if(levelCombination.get(levelCombination.size()-1) <= levelCombination.get(indexBest))
                             indexBest = cost.size()-1;
              }
           } 
        }
        return indexBest;
    }    
}
