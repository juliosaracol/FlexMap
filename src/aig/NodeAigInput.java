package aig;
/**
 * Classe que especializa NodeAig para o tipo INPUT
 * @author Julio Saraçol
 * */
public class NodeAigInput extends NodeAig 
{
    public NodeAigInput(int id,String name) 
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
        return true;    
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TypeNode getTypeNodeAig() 
    {
        return TypeNode.INPUT;
    }
    
    /*patterns visitor*/
    @Override
    public void accept(AigNodeVisitor aigNodeVisitor) 
    {
        aigNodeVisitor.visit(this);
    }
}
