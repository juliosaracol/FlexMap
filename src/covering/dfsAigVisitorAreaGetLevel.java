package covering;

import aig.*;

/**
 * Classe que aplica caminhamento no Aig para AreaFlow
 * @author Julio Saraçol
 */
public class dfsAigVisitorAreaGetLevel extends dfsNodeAigVisitor
{

    protected AreaFlow myArea;
    
    public dfsAigVisitorAreaGetLevel(AreaFlow areaFlow)
    {
        super();
        this.myArea  = areaFlow;
    }

    @Override
    public void function(NodeAig nodeAigActual) 
    {
        getLevel(nodeAigActual);
    }

    protected void getLevel(NodeAig nodeAigActual) 
    {
        if(myArea.levelNode.containsKey(nodeAigActual))
            return;
        int levelMax = 1; 
        for(NodeAig father : nodeAigActual.getParents())
        {
            if(!myArea.levelNode.containsKey(father))
              function(father);
            if(levelMax < myArea.levelNode.get(father))
                 levelMax = myArea.levelNode.get(father);
        }
        levelMax++;
        myArea.levelNode.put(nodeAigActual,levelMax);        
    }
}
