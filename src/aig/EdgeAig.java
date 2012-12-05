package aig;
import graph.*;
/**
 * Classe que especializa aresta de AIG 
 * @author Julio Saraçol
 * obs: instância inicial sempre sem inversor, caso necessário negar chamar createInverter();
 * */
public class EdgeAig extends Edge 
{

    protected boolean inverter;
    
    protected EdgeAig(int edgesCount, Vertex vertex1, Vertex vertex2)
    {
        super(edgesCount, vertex1, vertex2);
        this.inverter = false;
    }
    
    public boolean isInverter()
    {
        return inverter;
    }
    
    /**Método transforma aresta com inversor*/
    public boolean createInverter()
    {
        return this.inverter=true;
    }        
    
    /**Método que inverte a polaridade da aresta*/
    public void edgeInverter()
    {
        if(this.inverter==true)
            this.inverter=false;
        else
            this.inverter=true;
    }
}
