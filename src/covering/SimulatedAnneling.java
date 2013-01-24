package covering;

import FlexMap.CostFunction;
import aig.*;
import java.util.*;
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
                break;
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
        
        Covering newCovering = applyNewCovering(state);
        //seleciona um treenode 
        //pega um novo matching
        //gera nova cobertra 
        //retorna
        return newCovering;
    }

    private Covering applyNewCovering(Covering state) 
    {
        for(NodeAig nodeT: treeNodes)
        {
           System.out.println("TREENODE:"+ nodeT.getName());
           for(Set<NodeAig> set:this.kcuts.getCuts().get(nodeT))
           {
               float output =1;
               if(nodeT.getChildren().size()>1)
                   output = nodeT.getChildren().size();
               float inputs=0;
               for(NodeAig node: set)
               {
                  inputs += state.getCosts().get(node);
                  System.out.print(node.getName()+"-");
               }
               System.out.print(" ("+function.eval(0, 0, 0, inputs,output,1)+") custo\n");
           }
        }
        return null;
    }
    
}
