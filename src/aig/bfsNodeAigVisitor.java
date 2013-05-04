package aig;
import java.util.*;
/**
 *  Classe que implementa DFS através do Patterns Visitor
 *  @author Julio Saraçol 
 * */

public abstract class bfsNodeAigVisitor implements AigNodeVisitor
{
    public ArrayList<NodeAig> nodesBfs;
    public ArrayList<NodeAig> list;
    public int states;
    
    public bfsNodeAigVisitor() 
    {
      nodesBfs  = new ArrayList<NodeAig>();
      list      = new ArrayList<NodeAig>();        
      states    = 0;
    }

    @Override
    public void visit(NodeAigGate nodeAigGate) {
        if(!nodesBfs.contains(nodeAigGate))
        {
            nodesBfs.add(nodeAigGate);
            for(int i=0;i<nodeAigGate.getParents().size();i++)
            {
                if(!list.contains(nodeAigGate.getParents().get(i))&&(!nodesBfs.contains(nodeAigGate.getParents().get(i))))
                {
                    list.add(nodeAigGate.getParents().get(i));
                    this.states++;
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

    @Override
    public void visit(NodeAigGateOr nodeAigGateOr) {
        if(!nodesBfs.contains(nodeAigGateOr))
        {
            nodesBfs.add(nodeAigGateOr);
            for(int i=0;i<nodeAigGateOr.getParents().size();i++)
            {
                if(!list.contains(nodeAigGateOr.getParents().get(i))&&(!nodesBfs.contains(nodeAigGateOr.getParents().get(i))))
                {
                    list.add(nodeAigGateOr.getParents().get(i));
                    this.states++;
                }
            }
            function(nodeAigGateOr);
        }        
        if(list.size() > 0)
        {
            NodeAig temp =  list.get(0);
            list.remove(0);
            temp.accept(this);
        }
    }

    @Override
    public void visit(NodeAigLatch nodeAigLatch) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visit(NodeAigInput nodeAigInput) {
        if(!nodesBfs.contains(nodeAigInput))
        {
            nodesBfs.add(nodeAigInput);
            function(nodeAigInput);
            if(list.size() <= 0)
                return;
        }
        if(list.size() > 0)
        {
            NodeAig temp = list.get(0);
            list.remove(0);
            temp.accept(this);
        }
    }

    @Override
    public void visit(NodeAigOutput nodeAigOutput) {
        if(!nodesBfs.contains(nodeAigOutput))            
        {
            nodesBfs.add(nodeAigOutput);
            for(int i=0;i<nodeAigOutput.getParents().size();i++)
            {
                if(!list.contains(nodeAigOutput.getParents().get(i))&&(!nodesBfs.contains(nodeAigOutput.getParents().get(i))))
                {
                    list.add(nodeAigOutput.getParents().get(i));
                    this.states++;
                }
            }
            function(nodeAigOutput);
        }
        if(list.size() > 0)
        {
            NodeAig temp =  list.get(0);
            list.remove(0);
            temp.accept(this);
        }
    }
    
    @Override
    public void visit(NodeAigInverter nodeAigInverter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

            

   //--------------------GET & SET----------------------------------
    public ArrayList<NodeAig> getNodesBfs() {
        return nodesBfs;
    }

    public void setNodesBfs(ArrayList<NodeAig> nodesBfs) {
        this.nodesBfs = nodesBfs;
    }

    public ArrayList<NodeAig> getlist() {
        return list;
    }

    public void setlist(ArrayList<NodeAig> list) {
        this.list = list;
    }

    public int getStates() {
        return states;
    }

    public void setStates(int states) {
        this.states = states;
    }

    /** Método que define a função(objetivo) que será aplicaco a dfs
     * obs: Necessário implementar quando utilizar*/
    public abstract void function(NodeAig nodeAigActual);

}
