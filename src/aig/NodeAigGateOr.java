package aig;

/**
 * Classe que especializa NodeAig de AIG a partir de Vertex pa
 * @author Julio Saraçol
 * obs: classe que da forma ao NODO, porém utilizar especialização com métodos 'abstract' implementados
 */

public class NodeAigGateOr extends NodeAig
{

    public NodeAigGateOr(Integer id, String name) {
        super(id, name);
    }
    
    @Override
    public boolean isOR() {
        return true;
    }
    
    @Override
    public boolean isAnd() {
        return false;
    }

    @Override
    public boolean isInverter() {
        return false;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public boolean isOutput() {
        return false;
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public boolean getConstant() {
        return false;
    }

    @Override
    public TypeNode getTypeNodeAig() {
        return TypeNode.OR;
    }

    /*patterns visitor*/
    @Override
    public void accept(AigNodeVisitor aigNodeVisitor) 
    {
        aigNodeVisitor.visit(this);
    }
    
}
