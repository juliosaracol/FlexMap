package aig;
import graph.*;
/**
 * Classe que especializa NodeAig para tipo GATE
 * @author Julio Saraçol
 * */
public class NodeAigGate extends NodeAig
{

    public NodeAigGate(int id,String name)
    {
        super(id,name);
    }
    
    @Override
    public boolean isAnd()
    {
        return true;
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
        return false;    
    }

    @Override
    public boolean getConstant() 
    {
        return false;
    }

    @Override
    public TypeNode getTypeNodeAig() 
    {
        return TypeNode.AND;    
    }
    
    /**Patterns visitor*/
    @Override
    public void accept(AigNodeVisitor aigNodeVisitor) 
    {
        aigNodeVisitor.visit(this);
    }
}
