package aig;
import graph.*;
/**
 * Classe que especializa NodeAig para o tipo LATCH
 * @author Julio Saraçol
 * */

public class NodeAigLatch extends NodeAig
{

    public NodeAigLatch(int id,String name) 
    {
        super(id,name);
    }
    
    @Override
    public boolean isAnd()
    {
        return false;
    }
    
    @Override
    public boolean isOR()
    {
        return false;
    }
    
    @Override
    public boolean isInverter()
    {
    return false;
    }

    @Override
    public boolean isInput() 
    {
        return false;
    }

    @Override
    public boolean isOutput() 
    {
        return false;
    }

    @Override
    public boolean isConstant() 
    {
        return true;    
    }

    @Override
    public boolean getConstant() 
    {
        return false;
    }

    @Override
    public TypeNode getTypeNodeAig() 
    {
        return TypeNode.LATCH;    
    }
    
    /** Patterns visitor*/
    @Override
    public void accept(AigNodeVisitor aigNodeVisitor) 
    {
        aigNodeVisitor.visit(this);
    }
}
