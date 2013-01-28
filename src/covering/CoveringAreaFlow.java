package covering;

import FlexMap.CostFunction;
import aig.*;
import java.util.*;

/**
 * Classe que define a forma para cobertura assim como seus métodos abstratos
 * @author Julio Saraçol 
 */
public class CoveringAreaFlow extends Covering
{

    public CoveringAreaFlow(Map<NodeAig, Set<NodeAig>> covering,Map<NodeAig,Float> costs)
    {
        super(covering,costs);
    }
    
    @Override
    /**Método que aplica avaliação dos custos da cobertura é acessado pelo getCost()*/
      public void evaluation(CostFunction function)
    {
        float costActual = 0;
        this.output=0;
        this.input=0;
        for(Map.Entry<NodeAig,Set<NodeAig>> set : this.covering.entrySet())
        {
            if(!set.getKey().isInput())
            {
                this.output  = set.getKey().getChildren().size();
             if(set.getKey().getChildren().isEmpty())
                this.output =1;//fanouts
             for(NodeAig node: set.getValue())
                   this.input+=costs.get(node);
              costActual+=function.eval(area, delay, consumption, input, output,1);
            }
        }
        this.cost = costActual;
    }
} 
