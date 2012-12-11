package aig;
import java.util.*;

/**
 *  Classe que implementa DFS através do Patterns Visitor
 *  @author Julio Saraçol 
 * */
 
public abstract class dfsNodeAigVisitor implements AigNodeVisitor 
{
    public ArrayList<NodeAig> nodesDfs;
    public ArrayList<NodeAig> list;
    public int states; 
    
    public dfsNodeAigVisitor() 
    {
      nodesDfs      = new ArrayList<NodeAig>();
      list          = new ArrayList<NodeAig>();
      this.states   = 0;
    }

    @Override
    public void visit(NodeAigGate nodeAigGate) {
        if((!nodesDfs.contains(nodeAigGate)))
        {
            nodesDfs.add(nodeAigGate);
            for(int i=0;i<nodeAigGate.getParents().size();i++)
            {
                if(!list.contains(nodeAigGate.getParents().get(i))&&(!nodesDfs.contains(nodeAigGate.getParents().get(i))))
                {
                    list.add(nodeAigGate.getParents().get(i));                    
                    this.states++;
                }
            }
        }
        if(list.size()>0)
        {
            int sizeList = list.size()-1;
            NodeAig temp = list.get(sizeList);
            list.remove(sizeList); 
            temp.accept(this);
        }
        function(nodeAigGate);
    }

    @Override
    public void visit(NodeAigGateOr NodeAigGateOr) {
        if((!nodesDfs.contains(NodeAigGateOr)))
        {
            nodesDfs.add(NodeAigGateOr);
            for(int i=0;i<NodeAigGateOr.getParents().size();i++)
            {
                if(!list.contains(NodeAigGateOr.getParents().get(i))&&(!nodesDfs.contains(NodeAigGateOr.getParents().get(i))))
                {
                    list.add(NodeAigGateOr.getParents().get(i));                    
                    this.states++;
                }
            }
        }
        if(list.size()>0)
        {
            int sizeList = list.size()-1;
            NodeAig temp = list.get(sizeList);
            list.remove(sizeList); 
            temp.accept(this);
        }
        function(NodeAigGateOr);
    }

    @Override
    public void visit(NodeAigLatch nodeAigLatch) {
        return;
    }

    @Override
    public void visit(NodeAigInput nodeAigInput) {
        if(!nodesDfs.contains(nodeAigInput))
        {
            nodesDfs.add(nodeAigInput);
            if(list.size() <= 0)
                return;
        }
        if(list.size()>0)
        {
            int sizeList = list.size()-1;
            NodeAig temp = list.get(sizeList);
            NodeAig remove = list.remove(sizeList);
            temp.accept(this);
        }
        function(nodeAigInput);
    }

    @Override
    public void visit(NodeAigOutput nodeAigOutput) {
        if((!nodesDfs.contains(nodeAigOutput)))
        {
            nodesDfs.add(nodeAigOutput);
            for(int i=0;i<nodeAigOutput.getParents().size();i++)
            {
                if(!list.contains(nodeAigOutput.getParents().get(i))&&(!nodesDfs.contains(nodeAigOutput.getParents().get(i))))
                {
                    list.add(nodeAigOutput.getParents().get(i));
                    this.states++;
                }
            }
        }
        if(list.size()>0)
        {
            int sizeList = list.size()-1;
            NodeAig temp = list.get(sizeList);
            list.remove(sizeList);
            temp.accept(this);
        }
        function(nodeAigOutput);
    }

    public ArrayList<NodeAig> getList() {
        return list;
    }

    public void setList(ArrayList<NodeAig> list) {
        this.list = list;
    }

    public ArrayList<NodeAig> getNodesDfs() {
        return nodesDfs;
    }

    public void setNodesDfs(ArrayList<NodeAig> nodesDfs) {
        this.nodesDfs = nodesDfs;
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
