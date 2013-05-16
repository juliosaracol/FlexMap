package aig;
/**
 * Classe que especializa NodeAig para o tipo INPUT
 * @author Julio Saraçol
 * */
public class NodeAigOutput extends NodeAig 
{

    public NodeAigOutput(int id,String name)
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
        return true;
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
        return TypeNode.OUTPUT;    
    }
    

    /* Patterns visitor*/
    @Override
    public void accept(AigNodeVisitor aigNodeVisitor) 
    {
        aigNodeVisitor.visit(this);
    } 
}
