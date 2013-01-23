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
    protected float cost=0;
    protected float area=0, delay=0, consumption=0, input=0, output=0, other=0;

    public Covering(Map<NodeAig, Set<NodeAig>> covering) 
    {
        this.covering  = covering;
    }
    
    /**Método que aplica avaliação dos custos da cobertura  e atualiza o getCost() de acordo com a função cost passada como parametro*/
    public abstract void evaluation(CostFunction function);

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public float getConsumption() {
        return consumption;
    }

    public void setConsumption(float consumption) {
        this.consumption = consumption;
    }

    public Map<NodeAig, Set<NodeAig>> getCovering() {
        return covering;
    }

    public void setCovering(Map<NodeAig, Set<NodeAig>> covering) {
        this.covering = covering;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public float getInput() {
        return input;
    }

    public void setInput(float input) {
        this.input = input;
    }

    public float getOther() {
        return other;
    }

    public void setOther(float other) {
        this.other = other;
    }

    public float getOutput() {
        return output;
    }

    public void setOutput(float output) {
        this.output = output;
    }

    public float getCost() 
    {
        return cost;
    }

    
}
