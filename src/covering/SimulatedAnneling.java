package covering;

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

    protected int initialTemp;
    protected int finalTemp;
    protected Aig myAig;
    protected CutterK   kcuts;
    protected Covering  initialCovering;
    protected Covering  actualState;
    protected CostFunction function;
    protected ArrayList<NodeAig> treeNodes;
    protected Map<NodeAig,Integer> levelNode;
    
    
    public SimulatedAnneling(Aig myAig,CutterK kcuts, Covering initial, CostFunction function, int tempInitial, int tempFinal)
    {
        this.initialTemp        = tempInitial;
        this.finalTemp          = tempFinal;
        this.myAig              = myAig;  
        this.kcuts              = kcuts;
        this.initialCovering    = initial;
        this.function           = function;
        
        this.treeNodes = new ArrayList<NodeAig>();
        for(Map.Entry<NodeAig,Set<NodeAig>> covering : this.initialCovering.getCovering().entrySet())
        {
            if((covering.getKey().getChildren().size() > 1)&&(!covering.getKey().isInput()))
            {
                System.out.println("Node TreeNode"+covering.getKey().getName());
                treeNodes.add(covering.getKey());
            }
         }
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
         this.actualState = run();
     }
        
     //**Motor da engine simullatedAnelling*/
     public Covering run()
     {        
            Covering stateOriginal  = this.initialCovering; 
            Covering stateBest      = stateOriginal; 
            Covering stateNew; 
            double factor;
            double prob;
            double t = getInitialTemp();
            float energy        = stateOriginal.getCost(function);
            float energyBest    = energy;
            float energyNew;
            float delta;
            int countIterations = 0;

            while (t > 0)
            {                       
                factor = (4 + (Math.random() * 4))/10;
                t *= factor;                 

                stateNew    = perturbation(stateOriginal);
                energyNew   = stateNew.getCost(function);
                energy      = stateOriginal.getCost(function);
                delta = energyNew - energy;
                prob = Math.pow(Math.E,(-delta/t));

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
            }
            return stateBest;     
    }
        
    /* Metodo responsavel por gerar o valor da temperatura inicial entre min e max */
    private double getInitialTemp() 
    {
        return this.finalTemp + (Math.random() * this.initialTemp);
    }
    
    /* Metodo responsavel por gerar um numero aleatorio entre 0  e 1 */
    private double randomize() 
    {
        return (double) (Math.random() * 1);
    }

    private Covering perturbation(Covering state)
    {        
        Map<NodeAig,Set<NodeAig>> cuts  = getSetCutsTreeNodes(treeNodes);
        Covering newCovering            = applyNewCovering(state,cuts);
        return newCovering;
    }

    private Covering applyNewCovering(Covering state,Map<NodeAig,Set<NodeAig>> cuts) 
    {
        Map<NodeAig,Set<NodeAig>> tableCuts = new HashMap<NodeAig, Set<NodeAig>>();
//        for(Map.Entry<NodeAig,Set<NodeAig> node state.getCovering())
        
//        System.out.println("TREENODE:"+ nodeT.getName());
//           for(NodeAig set: cuts.get(nodeT))               
//           {
//               float output =1;
//               if(nodeT.getChildren().size()>1)
//                   output = nodeT.getChildren().size();
//               float inputs=0;
//            int index = (int) Math.random() * (cuts.size()-1);
//               {
//                 
//                  inputs += state.getCosts().get(node);
//                  System.out.print(node.getName()+"-");
//               }
//               //**pegando profundidade do nodo********************************/
//               AigCut auxAigCut = new AigCut();
//               auxAigCut.addAll(set);
//               bfsAigVisitorAreaSumLevel bfs = new bfsAigVisitorAreaSumLevel(levelNode,auxAigCut);
//               nodeT.accept(bfs);
//               /***************************************************************/
//               System.out.print(" ("+function.eval(0, 0, 0, inputs,output,1)+") custo, level"+bfs.getLevel()+"\n");
//           }
//        }
//        
//        
//          covering();       
            return null;
       }
    
    //**Método que aplica a cobertura baseado em bfs utilizando as areas calculadas
    protected void covering()
    {
//        bfsAigVisitorAreaCovering bfs = new bfsAigVisitorAreaCovering(this);
//        for(NodeAig nodeActual: myAig.getNodeOutputsAig())
//        {
//            System.out.println("saida: "+nodeActual.getName());
//            if(!this.covering.containsKey(nodeActual))
//            {
//                this.covering.put(nodeActual, this.bestCut.get(nodeActual));
//                nodeActual.accept(bfs);     
//            }
//        }
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
    }
    //**Método que seleciona os possiveis cortes dos treeNodes*/
    private Map<NodeAig, Set<NodeAig>> getSetCutsTreeNodes(ArrayList<NodeAig> treeNodes) 
    {
         Map<NodeAig, Set<NodeAig>> treeNodesCut = new HashMap<NodeAig, Set<NodeAig>>();
         for(NodeAig treeNode: treeNodes)
         {
            Set<AigCut> cuts        = kcuts.getCuts().get(treeNode);
            Set<NodeAig> setCuts    = new HashSet<NodeAig>();
            for(AigCut cut: cuts)
               setCuts.addAll(cut.getCut());
            treeNodesCut.put(treeNode,setCuts);
         }
         return treeNodesCut;       
    }

}
