package aig;

/**Classe que define a estrutura de Aig com inversores explicitados a partir de Aig 
 * @author Julio Saraçol
 */
public class AigInverter extends Aig 
{
    public AigInverter(String fileName) 
    {
        super(fileName);
        dfsAigToAigInverter dfs = new dfsAigToAigInverter(this);
        for(NodeAig out: getNodeOutputsAig())
            out.accept(dfs);
    }
    
    
    
}
