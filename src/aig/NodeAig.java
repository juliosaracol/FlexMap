package aig;
import graph.*;
/**
 * Classe que especializa NODO de AIG a partir de Vertex
 * @author Julio Saraçol
 * obs: classe que da forma ao NODO, porém utilizar especialização com métodos 'abstract' implementados
 * */

import java.util.ArrayList;

public abstract class NodeAig extends Vertex {

	/**Atributo baseado na definição do AIGER que nomeia os nodos*/
    protected String name;

    public NodeAig(Integer id, String name) {
        super(id);
        this.name = name;
    }
    /** Método acesso atributo Name*/
    public String getName() {
        return this.name;
    }

    /**Método de acesso a filhos do nodo
     *@return ArrayList de nodos filhos*/
    public ArrayList<NodeAig> getChildren() {
        ArrayList<Edge> adjacenciesNode = getAdjacencies();
        ArrayList<NodeAig> arrayChildren = new ArrayList<NodeAig>();
        for (int i = 0; i < adjacenciesNode.size(); i++) {
            if (adjacenciesNode.get(i).getVertex2().getId() == this.id) {
                arrayChildren.add((NodeAig) adjacenciesNode.get(i).getVertex1());
            }
        }
        return arrayChildren;
    }

    /**Método de acesso aos pais do nodo
     * @return ArrayList com os pais do nodo*/
    public ArrayList<NodeAig> getParents() {
        ArrayList<Edge> adjacenciesNode = getAdjacencies();
        ArrayList<NodeAig> arrayChildren = new ArrayList<NodeAig>();
        for (int i = 0; i < adjacenciesNode.size(); i++) {
            if (adjacenciesNode.get(i).getVertex2().getId() != this.id) {
                arrayChildren.add((NodeAig) adjacenciesNode.get(i).getVertex2());
            }
        }
        return arrayChildren;
    }
    /** Método que será implementado de acordo com o typeNodeAig*/
    public abstract boolean isAnd();
    /** Método que será implementado de acordo com o typeNodeAig*/
    public abstract boolean isOR();
    /** Método que será implementado de acordo com o typeNodeAig*/
    public abstract boolean isInverter();
    /** Método que será implementado de acordo com o typeNodeAig*/
    public abstract boolean isInput();
    /** Método que será implementado de acordo com o typeNodeAig*/
    public abstract boolean isOutput();
    /** Método que será implementado de acordo com o typeNodeAig*/
    public abstract boolean isConstant();
    /** Método que será implementado de acordo com o typeNodeAig*/
    public abstract boolean getConstant();
    /** Método que será implementado de acordo com o typeNodeAig*/
    public abstract TypeNode getTypeNodeAig();
    /** Patterns visitor*/
    public abstract void accept (AigNodeVisitor aigNodeVisitor);
}
