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
        float cost = 0;
        output=0;
        input=0;
        for(Map.Entry<NodeAig,Set<NodeAig>> set : this.covering.entrySet())
        {
            output  = set.getKey().getChildren().size();
            if(set.getKey().getChildren().isEmpty())
                output =1;//fanouts
            for(NodeAig node: set.getValue())
               input+=costs.get(node);
          cost+=function.eval(area, delay, consumption, input, output,1);
        }
        this.cost = cost;
    }
} 
