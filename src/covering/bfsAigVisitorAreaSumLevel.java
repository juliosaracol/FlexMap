package covering;

import aig.*;
import java.util.*;
import kcutter.AigCut;
/**
 * Classe para caminhamento do grafo somando a profundidade do nodo alvo em relação ao cut
 * @author Julio Saraçol
 */
public class bfsAigVisitorAreaSumLevel extends bfsNodeAigVisitor 
{
    protected Map<NodeAig,Integer>  levelNode;
    protected Set<Integer>          pathLevel;
    protected AigCut                cut;
    
    public bfsAigVisitorAreaSumLevel(Map<NodeAig,Integer>  levelNode, AigCut cut)
    {
        super();
        this.pathLevel  = new HashSet<Integer>();
        this.levelNode  = levelNode;
        this.cut        = cut;
    }

    @Override
    public void function(NodeAig nodeAigActual)
    {          
        if(!this.pathLevel.contains(this.levelNode.get(nodeAigActual)))
            this.pathLevel.add(this.levelNode.get(nodeAigActual));
    }
    
    @Override
    public void visit(NodeAigGate nodeAigGate) {
        if(!nodesBfs.contains(nodeAigGate))
        {
            nodesBfs.add(nodeAigGate);
            if(!this.cut.contains(nodeAigGate))
            {
                for(int i=0;i<nodeAigGate.getParents().size();i++)
                {
                    if(!list.contains(nodeAigGate.getParents().get(i))&&(!nodesBfs.contains(nodeAigGate.getParents().get(i))))
                    {
                      list.add(nodeAigGate.getParents().get(i));
                      this.states++;
                    }
                }
            }
            function(nodeAigGate);
        }        
        if(list.size() > 0)
        {
            NodeAig temp =  list.get(0);
            list.remove(0);
            temp.accept(this);
        }
    }

    
    public Integer getLevel()
    {
        int level=0;
        for(Integer path: this.pathLevel)
            level+=path;
        return level;
    }
}
