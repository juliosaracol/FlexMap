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
    protected Map<NodeAig,Set<NodeAig>>  bestCut;
    protected Covering  initialCovering;
    protected Covering  actualState;
    protected CostFunction function;
    protected ArrayList<NodeAig> treeNodes;
    protected Map<NodeAig,Integer> levelNode;
    
    
    public SimulatedAnneling(Aig myAig,CutterK kcuts,Map<NodeAig,Set<NodeAig>> bestCut, Covering initial, CostFunction function, int tempInitial, int tempFinal)
    {
        this.initialTemp        = tempInitial;
        this.finalTemp          = tempFinal;
        this.myAig              = myAig;  
        this.kcuts              = kcuts;
        this.initialCovering    = initial;
        this.function           = function;
        this.bestCut            = bestCut;
        
        this.treeNodes = new ArrayList<NodeAig>();
        for(Map.Entry<NodeAig,Set<NodeAig>> covering : this.initialCovering.getCovering().entrySet())
        {
            if((covering.getKey().getChildren().size() > 1)&&(!covering.getKey().isInput()))
            {
                System.out.println("Node TreeNode"+covering.getKey().getName());
                treeNodes.add(covering.getKey());
            }
         }
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
         this.actualState = run();
     }
        
     //**Motor da engine simullatedAnelling*/
     public Covering run()
     {        
//         
//                 double temperature = temperatureInitial;
//        Random random = new Random();
//
//        int deltaEnergy;
//        double probability;
//        Board newState;
//
//        do {
//            newState = createNewState(candidateState);
//            
//            if (newState.getAttacksQueens() == 0)
//                return newState;
//
//            deltaEnergy = newState.getAttacksQueens() - candidateState.getAttacksQueens();
//
//            probability = Math.pow(Math.E, -deltaEnergy/temperature);
//
//            if ( (deltaEnergy <= 0) || ( probability > ((double)(random.nextInt(10)/10.0)) ) ) {
//                candidateState = newState.clone();
//                path.add(candidateState);
//            }
//
//            temperature = ( (6 + random.nextInt(10 - 6))/10.0 ) * temperature;
//
//        } while(candidateState.getAttacksQueens() != 0 && temperature >= 1 );
//
//
//        return candidateState;

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
        
    //**Método que gera nova cobertura utilizando os Cuts dos teeeNodes*/
    private Covering applyNewCovering(Covering state,Map<NodeAig,ArrayList<Set<NodeAig>>> cuts) 
    {
        System.out.println("###########NEWCOVERING#########################");
        for(Map.Entry<NodeAig,Set<NodeAig>> set : this.bestCut.entrySet())
        {
           System.out.print("Nodo: "+ set.getKey().getName()+" [");            
           if(treeNodes.contains(set.getKey()))
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
           System.out.print("] -("+function.eval(0, 0, 0, inputs,output,1)+") custo, level"+bfs.getLevel()+"\n");
         }
         System.out.println("#################################################");
         return covering();
    }
    
    //**Método que aplica a cobertura baseado em bfs utilizando as areas calculadas
    protected Covering covering()
    { return null;}
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
 //   }

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

    /**Método responsável por gerar uma nova cobertura alterando cortes de treenodes*/
    private Covering perturbation(Covering state)
    {        
        Map<NodeAig,ArrayList<Set<NodeAig>>> cuts  = getSetCutsTreeNodes(treeNodes);
        Covering newCovering = applyNewCovering(state,cuts);
        return newCovering;
    }

    //**Método que seleciona os possiveis Cuts dos treeNodes*/
    private Map<NodeAig, ArrayList<Set<NodeAig>>> getSetCutsTreeNodes(ArrayList<NodeAig> treeNodes) 
    {
         Map<NodeAig,ArrayList<Set<NodeAig>>> treeNodesCut = new HashMap<NodeAig, ArrayList<Set<NodeAig>>>();
         for(NodeAig treeNode: treeNodes)
         {
            ArrayList<Set<NodeAig>> sets = new ArrayList<Set<NodeAig>>();   
            Set<AigCut>  cuts            = kcuts.getCuts().get(treeNode);
            for(AigCut cut: cuts)
            {
                if(cut.size() > 1)
                {
                 Set<NodeAig> setCuts  = new HashSet<NodeAig>();
                 setCuts.addAll(cut.getCut());
                 sets.add(setCuts);
                }
            }
            treeNodesCut.put(treeNode, sets);
         }
         return treeNodesCut;       
    }

}
