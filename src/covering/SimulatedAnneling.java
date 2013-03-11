package covering;

import FlexMap.CostAreaFlow;
import FlexMap.CostFunction;
import aig.*;
import java.util.*;
import kcutter.AigCut;
import kcutter.CutterK;

/**
 * Classe aplica SimulatedAnneling 
 * @author Julio Saraçol
 */
public class SimulatedAnneling
{
    protected int temp;
    protected int initialTemp;
    protected int finalTemp;
    protected float step;
    protected Aig myAig;
    protected CutterK   kcuts;
    protected Map<NodeAig,Set<NodeAig>>  bestCut;
    protected Map<NodeAig,Float>  bestCost;
    protected Covering  initialCovering;
    protected Covering  actualState;
    protected CostFunction function;
    protected ArrayList<NodeAig> treeNodes;
    protected Map<NodeAig,Integer> levelNode;
    
    
    public SimulatedAnneling(Aig myAig,CutterK kcuts,Map<NodeAig,Set<NodeAig>> bestCut, Covering initial, CostFunction function, int tempInitial, int tempFinal,int steps)
    {
        this.step               = (initialTemp-finalTemp)/steps;
        this.initialTemp        = tempInitial;
        this.finalTemp          = tempFinal;
        this.temp               = tempInitial;
        this.myAig              = myAig;  
        this.kcuts              = kcuts;
        this.initialCovering    = initial;
        this.function           = function;
        this.bestCut            = bestCut;
        
         //**Profundidade de todos os nodos do grafo****************************
         levelNode = new HashMap<NodeAig, Integer>();
         dfsAigVisitorAreaGetLevel dfs = new dfsAigVisitorAreaGetLevel(this.levelNode);
         for(NodeAig node: myAig.getNodeInputsAig())
            this.levelNode.put(node,1);
         for(NodeAig node: myAig.getNodeOutputsAig())
         {
           if((node.isOutput())&&(node.getParents().isEmpty())) //constant
               this.levelNode.put(node,1);
           else
               node.accept(dfs);
         }
         //*********************************************************************
         System.out.println("##############Solução Inicial com custo:"+initialCovering.getCost(function));
        this.actualState = run();
     }
        
     //**Motor da engine simullatedAnelling*/
     public Covering run()
     {        
            Covering stateOriginal  = this.initialCovering; 
            Covering stateBest      = stateOriginal; 
            Covering stateNew; 
            double prob;
            double t = getTemp();
            float energy        = stateOriginal.getCost(function);
            float energyBest    = energy;
            float energyNew;
            float delta;
            int countIterations = 0;

            while (t > this.finalTemp)
            {                       
                stateNew    = perturbation(stateOriginal);
                energyNew   = stateNew.getCost(function);
                energy      = stateOriginal.getCost(function);
                delta       = energyNew - energy;
                prob        = Math.pow(Math.E,(-delta/t));

                if ((delta <= 0) || (prob > randomize())) 
                {
                    stateOriginal = stateNew; 
                    energy        = energyNew;
                }

                if (energy < energyBest) 
                {
                    stateBest   = stateNew; 
                    energyBest  = energyNew;
                }
                countIterations++;
                t = getTemp();                 
            }
            return stateBest;     
    }
        
    //**Método que gera nova cobertura utilizando os Cuts dos teeeNodes*/
    private Covering applyNewCovering(Covering state,Map<NodeAig,ArrayList<Set<NodeAig>>> cuts) 
    {
        Map<NodeAig,Float> cost = new HashMap<NodeAig, Float>();
        System.out.println("###########NEWCOVERING#########################");
        for(Map.Entry<NodeAig,Set<NodeAig>> set : this.bestCut.entrySet())
        {
           System.out.print("Nodo: "+ set.getKey().getName()+" [");            
           if(cuts.containsKey(set.getKey()))
           {
               int index = (int) Math.random() * (cuts.get(set.getKey()).size()-1);
               set.setValue(cuts.get(set.getKey()).get(index));
           }
           float output =1;
           float inputs=0;
           for(NodeAig cut: set.getValue())               
           {
               if(set.getKey().getChildren().size()>1)
                   output = set.getKey().getChildren().size();
               inputs += state.getCosts().get(cut);
               System.out.print(cut.getName()+"-");
           }
           //**pegando profundidade do nodo********************************/
           AigCut auxAigCut = new AigCut();
           auxAigCut.addAll(set.getValue());
           bfsAigVisitorAreaSumLevel bfs = new bfsAigVisitorAreaSumLevel(levelNode,auxAigCut);
           set.getKey().accept(bfs);
           /***************************************************************/
           if(set.getKey().isInput())
               cost.put(set.getKey(),(float)0);
           else
               cost.put(set.getKey(),function.eval(0, 0, 0, inputs,output,1));
           System.out.print("] -("+cost.get(set.getKey())+") custo, level"+bfs.getLevel()+"\n");
         }
         this.bestCost = cost;
         Covering finalCovering = covering(cost);         
         System.out.println("################################################# custo:");
         finalCovering.getCost(function);
         return finalCovering;
    }
    
    //**Método que aplica a cobertura baseado em bfs utilizando as areas calculadas
    protected Covering covering(Map<NodeAig,Float> cost)
    {
        Map<NodeAig,Set<NodeAig>> selectedCuts = new HashMap<NodeAig, Set<NodeAig>>();
        bfsAigVisitorSimulatedCovering bfs = new bfsAigVisitorSimulatedCovering(this,selectedCuts);
        for(NodeAig nodeActual: myAig.getNodeOutputsAig())
        {
            if(!selectedCuts.containsKey(nodeActual))
            {
                selectedCuts.put(nodeActual, this.bestCut.get(nodeActual));
                nodeActual.accept(bfs);     
            }
        }
        Covering newState = new CoveringAreaFlow(selectedCuts,bestCost);
        return newState;
    }
//        boolean signalOk = true;
//        while(signalOk == true)
//        {
//          Map<NodeAig,AigCut> list = new HashMap<NodeAig, AigCut>();
//          signalOk = false;  
//          for(Map.Entry<NodeAig,AigCut> element: this.covering.entrySet())
//              for(NodeAig node: element.getValue().getCut())
//                if((!node.isInput())&&(!this.covering.containsKey(node)))
//                {
//                    list.put(node, this.bestCut.get(node));
//                    signalOk = true;
//                }
//          this.covering.putAll(list);
//        }
 //   }

    /* Metodo responsável por gerar o valor da temperatura inicial entre min e max */
    private float getTemp() 
    {
        this.temp = (int) ((float)(Math.pow(temp, 2))-(temp*step));
        System.out.println("TEMPERATURA: "+ temp);
        return temp;
    }
    
    /* Metodo responsavel por gerar um numero aleatorio entre 0  e 1 */
    private double randomize() 
    {
        return (double) (Math.random() * 1);
    }

    /**Método responsável por gerar uma nova cobertura alterando cortes de treenodes*/
    private Covering perturbation(Covering state)
    {        
        Map<NodeAig,ArrayList<Set<NodeAig>>> cuts  = getSetCutsTreeNodes();
        Covering newCovering = applyNewCovering(state,cuts);
        return newCovering;
    }

    //**Método que seleciona os possiveis Cuts que referenciam treeNodes*/
    private Map<NodeAig, ArrayList<Set<NodeAig>>> getSetCutsTreeNodes() 
    {
         Map<NodeAig,ArrayList<Set<NodeAig>>> treeNodesCut = new HashMap<NodeAig, ArrayList<Set<NodeAig>>>();
         for(Map.Entry<NodeAig,Set<NodeAig>> set: this.bestCut.entrySet())
         {
//           for(NodeAig nodeT: treeNodes)
//           {
//            if(set.getValue().contains(nodeT)) 
//            {
//              System.out.println("NOVO CORTE PARA NODO:"+ set.getKey().getName());  
//              ArrayList<Set<NodeAig>> sets = new ArrayList<Set<NodeAig>>();   
//              Set<AigCut>  cuts            = kcuts.getCuts().get(set.getKey());
//              for(AigCut cut: cuts)
//              {
//                if(cut.size() > 1)
//                {
//                 Set<NodeAig> setCuts  = new HashSet<NodeAig>();
//                 setCuts.addAll(cut.getCut());
//                 if(!sets.contains(setCuts))
//                     sets.add(setCuts);
//                }
//              }
//              treeNodesCut.put(set.getKey(), sets);
  //          }
 //          }
         }
         return treeNodesCut;       
    }

}
