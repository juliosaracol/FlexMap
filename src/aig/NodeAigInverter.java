/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aig;

/**
 * Classe que especializa NodeAig para tipo INVERTER
 * @author Julio Saraçol
 * */
public class NodeAigInverter extends NodeAig 
{

    public NodeAigInverter(Integer id, String name) {
        super(id, name);
    }
    
    @Override
    public boolean isAnd() {
        return false;
    }

    @Override
    public boolean isOR() {
        return false;
    }

    @Override
    public boolean isInverter() {
        return true;
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
        return TypeNode.INVERTER;    
    }

    @Override
    public void accept(AigNodeVisitor aigNodeVisitor) {
        aigNodeVisitor.visit(this);
    }
    
}
