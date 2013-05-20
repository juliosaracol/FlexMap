package covering;

import aig.*;
import java.util.Map;

/**
 * Classe que aplica caminhamento no Aig para AreaFlow
 * @author Julio Saraçol
 * obs: a profundidade se da das entradas para as saidas(saidas possuem a profundidade maior)
 */
public class dfsAigVisitorAreaGetLevel extends dfsNodeAigVisitor
{

    protected Map<NodeAig,Integer> levelNode;
    
    public dfsAigVisitorAreaGetLevel(Map<NodeAig,Integer> levels)
    {
        super();
        this.levelNode  = levels;
    }

    @Override
    public void function(NodeAig nodeAigActual) 
    {
        getLevel(nodeAigActual);
    }

    protected void getLevel(NodeAig nodeAigActual) 
    {
        if(this.levelNode.containsKey(nodeAigActual))
            return;
        int levelMax = 1; 
        for(NodeAig father : nodeAigActual.getParents())
        {
            if(!this.levelNode.containsKey(father))
              function(father);
            if(levelMax < this.levelNode.get(father))
                 levelMax = this.levelNode.get(father);
        }
        levelMax++;
        this.levelNode.put(nodeAigActual,levelMax);        
    }
}
