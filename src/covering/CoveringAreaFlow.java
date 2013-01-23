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
    public CoveringAreaFlow(Map<NodeAig, Set<NodeAig>> covering) 
    {
        super(covering);
    }
    
    @Override
    /**Método que aplica avaliação dos custos da cobertura*/
      public void evaluation(CostFunction function)
    {
        this.cost = 0;
        for(Map.Entry<NodeAig,Set<NodeAig>> set : this.covering.entrySet())
        {
            output  = set.getKey().getChildren().size();
            input   = set.getValue().size();          
            this.cost+=function.eval(area, delay, consumption, input, output, other);
        }
    }
} 
