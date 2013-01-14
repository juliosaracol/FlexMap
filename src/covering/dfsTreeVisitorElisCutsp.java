package covering;

/**
 * Método que aplica a restrição s p através do caminhamento dfs na árvore em questão
 * @author Julio Saraçol
 */

import FlexMap.*;
import aig.*;
import tree.*;
import java.util.*;

public class dfsTreeVisitorElisCutsp extends dfsNodeAigVisitor
{
    protected int s;
    protected int p;
    protected Trees         trees;
    protected Tree          tree;
    protected Set<Tree>     newTrees;
    protected TreeMap<String,Integer>    coveringS    = new TreeMap<String,Integer>();
    protected TreeMap<String,Integer>    coveringP    = new TreeMap<String,Integer>(); 
    protected TreeMap<String,Integer>    level        = new TreeMap<String,Integer>(); 


    public dfsTreeVisitorElisCutsp(Trees trees, Tree tree, int s, int p)
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
            level.put(nodeAigActual.getName(),1);
            System.out.println(nodeAigActual.getName()+" nivel: "+level.get(nodeAigActual.getName()) +" custo s p: 1-1");
            return;
        }
        if(!sumBestCost(nodeAigActual))
        {
          System.out.println("CutTree nodo: "+nodeAigActual.getName());
          cutTree(nodeAigActual);
          function(nodeAigActual);
        }
    }
    
   /**Método que aplica o cálculo dos custos do nodo onde caso o custo ultrapasse s p retorna false
    * @param nodeCurrent (node actual)
    * @return true (cost OK) false (cost > sp)*/
    private boolean sumBestCost(NodeAig nodeAigActual) 
    {
       if(coveringP.containsKey(nodeAigActual.getName())&&coveringS.containsKey(nodeAigActual.getName()))
           return true;
       int costS=0,costP=0;
       if(nodeAigActual.isOR()) //caso OR
       {
         for(NodeAig father: nodeAigActual.getParents())
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
           for(NodeAig father: nodeAigActual.getParents())
           {
              if((!coveringP.containsKey(father.getName()))||(!coveringS.containsKey(father.getName())))
                  function(father);
              if(coveringP.get(father.getName()) > costP)
                  costP = coveringP.get(father.getName());
              costS += coveringS.get(father.getName());
           }   
       }
       if((costP > p)||(costS > s))
           return false;
       coveringS.put(nodeAigActual.getName(), costS);
       coveringP.put(nodeAigActual.getName(), costP);
       level.put(nodeAigActual.getName(), (getBiggerLevel(nodeAigActual)+1));
       System.out.println("Custo do nodo: "+nodeAigActual.getName()+" nivel: "+level.get(nodeAigActual.getName())+
               " é s:"+costS+" p:"+costP);
       return true;
   }

    private int getBiggerLevel(NodeAig nodeAigActual) 
    {
        int maxLevel = 1;
        for(NodeAig father: nodeAigActual.getParents()){
            if(this.level.get(father.getName()) > maxLevel)
                maxLevel = this.level.get(father.getName());
        }
        return maxLevel;
    }
    /**Método responsável por identificar o nodo a ser cortado, cortar e inserir nodosFake na árvore de acordo com a restrição*/
    private void cutTree(NodeAig nodeAigActual) 
    {
        NodeAig newInput = null;
        ArrayList<ArrayList<Integer>> cost      = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<NodeAig>> choices   = new ArrayList<ArrayList<NodeAig>>();
        boolean solution=false;
        int selected = choiceDinamic(nodeAigActual,choices,cost); //escolhe melhores nodos pra cortar
        for(int i=0;i<choices.get(selected).size();i++)
           if((coveringP.get(choices.get(selected).get(i).getName()) > 1)||(coveringS.get(choices.get(selected).get(i).getName()) > 1))
            { solution = false; break; }            
        if(solution == false) //caso de corte na árvore
        {
          if(choices.get(selected).size()>1)//caso de gerar nodeFake do nodoAigActual
          {
              newInput = copyAndCutTree(nodeAigActual, choices, selected, cost, solution);
              tree.addEdge(nodeAigActual, newInput,false);
              coveringS.put(newInput.getName(),1);
              coveringP.put(newInput.getName(),1);
              level.put(newInput.getName(),1);
          }
          else
          {
            boolean inverter = Algorithms.isInverter(nodeAigActual, choices.get(selected).get(0));
            newInput = new NodeAigInput(choices.get(selected).get(0).getId(),choices.get(selected).get(0).getName());
            copyAndCutTree(nodeAigActual, choices, selected, cost, solution);
            tree.addEdge(nodeAigActual, newInput,inverter);
          } 
          System.out.println("Nodo novo: "+newInput.getName()
                       +" com cobertura:"+coveringP.get(newInput.getName())+"-"+coveringP.get(newInput.getName())+" nivel:"+this.level.get(newInput.getName()));            
        }
        else //nodos com custo 1,1
           copyAndCutTree(nodeAigActual,choices,selected,cost,solution);           
    }
    
    /**Método responsável cortar árvore na subárvore e caso necessário aplicar a cópia da subárvore
     * @param nodeAigActual
     * @param choices
     * @param selected
     * @param cost 
     * @param solution (avisa se os nodos filhos são somente entradas primarias ou subárvores)
     */
    private NodeAig copyAndCutTree(NodeAig nodeAigActual, ArrayList<ArrayList<NodeAig>> choices, int selected, 
            ArrayList<ArrayList<Integer>> cost, boolean solution) 
    {
        if(solution == false)
        {
            NodeAig root = null, inputFake = null;
            if(choices.get(selected).size() > 1) //nodoFake
            {
             if(nodeAigActual.isOR())
                root    = new NodeAigGateOr(tree.getEdgesCount(),createName(nodeAigActual.getName(),"X")); 
             else
                root    = new NodeAigGate(tree.getEdgesCount(),createName(nodeAigActual.getName(),"X")); 
             inputFake  = new NodeAigInput(root.getId(),root.getName());   
             Tree newTree = new Tree(root);
             for(NodeAig node: choices.get(selected))
             {
               bfsTreeVisitorElisCopyAndCut bfsCopy =  new bfsTreeVisitorElisCopyAndCut();
               node.accept(bfsCopy);
               for(NodeAig deleted: bfsCopy.getDeletedNodes())
                    tree.removeVertex(deleted);
               newTree.getTree().addAll(bfsCopy.getTree().getTree());
               newTree.addEdge(root, bfsCopy.getTree().getRoot(), false);               
             }
             this.newTrees.add(newTree);
             return inputFake;
            }
            else
            {
               if(choices.get(selected).get(0).isOR())
                root    = new NodeAigGateOr(choices.get(selected).get(0).getId(),choices.get(selected).get(0).getName()); 
               else
                root    = new NodeAigGate(choices.get(selected).get(0).getId(),choices.get(selected).get(0).getName()); 
               Tree newTree = new Tree(root);       
               bfsTreeVisitorCopy bfsCopy =  new bfsTreeVisitorCopy(new ArrayList<String>(), newTree);
               nodeAigActual.accept(bfsCopy);
               coveringP.put(nodeAigActual.getName(),1);
               coveringS.put(nodeAigActual.getName(),1);
               this.newTrees.add(newTree);
               for(NodeAig deleted: newTree.getTree())
                  if(nodeAigActual.getId() != deleted.getId())
                    tree.removeVertex(deleted.getId());
               return null;
            }
        }
        else //SOMENTE ENTRADAS CUSTO 1,1
        {
          ArrayList<NodeAig> newFathersInput = new ArrayList<NodeAig>();
          Integer index;
          if(nodeAigActual.isOR())
              index = p;
          else
              index = s;             
          while(solution != false)
          {                       
            int newNodesCut = nodeAigActual.getParents().size()/index; //quantidade de vezes que vai aplica o corte de entradas 1,1 
            while(newNodesCut > 0)
            {
               NodeAig newInput = new NodeAigInput(tree.getEdgesCount(),createName(nodeAigActual.getName(),"X"));
               NodeAig newRoot  = new NodeAigGate(newInput.getId(),newInput.getName());               
               Tree newTree     = new Tree(newRoot);
               for(NodeAig fatherDeleted: choices.get(selected))
               {
                   boolean inverter = Algorithms.isInverter(nodeAigActual, fatherDeleted);
                   tree.removeVertex(fatherDeleted);
                   newTree.addEdge(newRoot, fatherDeleted,inverter);
                   newTree.add(newRoot);
               }    
               coveringS.put(newInput.getName(),1);
               coveringP.put(newInput.getName(),1); 
               level.put(newInput.getName(),2);
               newFathersInput.add(newInput);                   
               System.out.println("Novo corte na árvore novo NodoInput:"+newInput.getName());
               newNodesCut--;
               if(newNodesCut > 0)
               {
                cost.clear();
                choices.clear();
                selected = choiceDinamic(nodeAigActual,choices,cost);
               }
               this.newTrees.add(newTree);
            }
            for(NodeAig nodesFake: newFathersInput)
              tree.addEdge(nodeAigActual, nodesFake, false);
            if(nodeAigActual.getParents().size() >= index ) //caso nao necessite cortar mais as entradas
              solution = false; 
          }
          return null;
        }
    }

    /** Método que faz todas a combinações de corte e seleciona a melhor alternativa do conjunto de nodos disponiveis
     * @param nodeAigActual (node em questão)
     * @param choices (indice das combinações de nodos pai)
     * @param cost  (custo do corte dessa combinação)
     * @return (indice do choices a ser utilizado para cortar)
     */
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
    
    /**Método que cria nomes para nodosFake sem redundância*/
    private String createName(String nodeCurrentName, String type)
    {
        String name="";
        name = nodeCurrentName+type;
        for(NodeAig node: this.tree.getTree())
            if(node.getName().equals(name))
              name = createName(name, type);
        for(Tree newT : this.newTrees)
            for(NodeAig node: newT.getTree())
                if(node.getName().equals(name))
                  name = createName(name, type);
        return name;
    }

    public TreeMap<String, Integer> getCoveringP() {
        return coveringP;
    }

    public TreeMap<String, Integer> getCoveringS() {
        return coveringS;
    }

    public Set<Tree> getNewTrees() {
        return newTrees;
    }

    public Trees getTrees() {
        return trees;
    }
    
}
