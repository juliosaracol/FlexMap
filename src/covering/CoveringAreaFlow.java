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
        float costFinal = 0;
        for(Map.Entry<NodeAig,Set<NodeAig>> covered: covering.entrySet())
            costFinal+=this.costs.get(covered.getKey());
        this.cost = costFinal;
    }
} 
