package covering;

import FlexMap.CostFunction;
import aig.*;
import java.util.*;

/**
 * Classe aplica SimulatedAnneling 
 * @author Julio Saraçol
 */
public class SimulatedAnneling
{

    protected int initialTemp;
    protected int finalTemp;
    protected Aig myAig;
    protected Covering initialCovering;
    protected Covering actualState;
    protected CostFunction function;
    
    public SimulatedAnneling(Aig myAig, Covering initial, CostFunction function, int tempInitial, int tempFinal)
    {
        this.myAig              = myAig;  
        this.initialCovering    = initial;
        this.function           = function;
        ArrayList<NodeAig> treeNodes = new ArrayList<NodeAig>();
        for(Map.Entry<NodeAig,Set<NodeAig>> covering : this.initialCovering.getCovering().entrySet())
        {
            if((covering.getKey().getChildren().size() > 1)&&(!covering.getKey().isInput()))
            {
                System.out.println("Node TreeNode"+covering.getKey().getName());
                treeNodes.add(covering.getKey());
            }
            if(covering.getValue().size() > 1)
            {
                for(NodeAig node: covering.getValue())
                    if((node.getChildren().size() > 1)&&(!node.isInput()))
                    {
                        System.out.println("Node TreeNode"+node.getName());
                        treeNodes.add(node);
                    }
            }
            this.actualState = run();
         }
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
            stateOriginal.evaluation(function);
            float energy        = stateOriginal.getCost();
            float energyBest    = energy;
            float energyNew;
            float delta;
            int countIterations = 0;

            while (t > 0)
            {                       
                factor = (4 + (Math.random() * 4))/10;
                t *= factor;                 

                stateNew    = perturbation(stateOriginal);
                stateNew.evaluation(function);
                energyNew   = stateNew.getCost();
                energy      = stateOriginal.getCost();
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
        //seleciona um treenode 
        //pega um novo matching
        //gera nova cobertra 
        //retorna
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
