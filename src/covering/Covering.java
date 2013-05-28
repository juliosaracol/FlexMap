package covering;

import FlexMap.CostFunction;
import aig.*;
import java.util.*;

/**
 * Classe que define a forma para cobertura assim como seus métodos abstratos
 * @author Julio Saraçol 
 */
public abstract class Covering
{
    protected Map<NodeAig,Set<NodeAig>> covering;
    protected Map<NodeAig,Float>        costs;
    protected float                     cost=0;

    public Covering(Map<NodeAig, Set<NodeAig>> covering,Map<NodeAig,Float> costs) 
    {
        this.covering   = covering;
        this.costs      = costs;
    }
    
    public Map<NodeAig, Set<NodeAig>> getCovering() {
        return Collections.unmodifiableMap(covering);
    }

    public void setCovering(Map<NodeAig, Set<NodeAig>> covering) {
        this.covering = covering;
    }

    public Map<NodeAig, Float> getCosts() {
        return Collections.unmodifiableMap(costs);
    }  
    
    /**Método que aplica avaliação dos custos da cobertura  e atualiza o getCost() de acordo com a função cost passada como parametro*/
    public abstract void evaluation(CostFunction function);
    
    public float getCost(CostFunction function)
    {
        evaluation(function);
        return this.cost;
    }
   
}
