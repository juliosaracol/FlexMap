package kcutter;

import aig.*;
import java.util.*;

/**
 * Classe que re-implementa o SET de nodeAig
 * @author Julio Saraçol
 */
public class AigCut implements Set<NodeAig> 
{
    /**Hash evita cortes redundantes*/
    protected  int sign = 0;
    /**Nodos do corte*/
    protected Set<NodeAig> cut = new HashSet<NodeAig>();

    /**
     * Construtor
     */
    public AigCut() {
    }

    /**
     * Construtor para um Cut
     * @param node The node for the cut
     */
    public AigCut(NodeAig node) {
        super();
        this.add(node);
    }

    public void showCut()
    {
       if(cut.size() == 0)
           return;
       System.out.print("[");
       int i=0;
       for(NodeAig node: cut)
       {
           if(i < cut.size()-1)
                System.out.print(node.getName()+",");
           else
               System.out.print(node.getName()+"]\n");
           i++;
       }
    }
    
    /**Método para geração de logs*/
    public String getBytesForLog()
    {
       String cutString="";
       cutString +="[";
       int i=0;
       for(NodeAig node: this.cut)
       {
           if(i < this.cut.size()-1)
               cutString +=node.getName()+",";
           else
               cutString +=node.getName()+"]\n";
           i++;
       }
       return cutString;
    }
    
    public int getSign(){
        return sign;
    }
    //-------Basic Methods--------------
    @Override
    public int size() {
        return cut.size();
    }

    @Override
    public boolean isEmpty() {
        return cut.isEmpty();
    }

    @SuppressWarnings("element-type-mismatch")
    @Override
    public boolean contains(Object o) {
        return cut.contains(o);
    }

    @Override
    public Iterator<NodeAig> iterator() {
        return cut.iterator();
    }

    @Override
    public Object[] toArray() {
        return cut.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return cut.toArray(a);
    }

    @Override
    public boolean add(NodeAig e) {
        sign |= 1 << (e.hashCode() % Integer.SIZE);
        return cut.add(e);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return cut.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends NodeAig> c) {
       for (NodeAig aigNode : c) {
            sign |= 1 << (aigNode.hashCode() % Integer.SIZE);
        }
        return cut.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        cut.clear();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AigCutBrc)) {
            return false;
        }
        return cut.equals(((AigCutBrc) obj));
    }

    @Override
    public int hashCode() {
        return cut.hashCode();
    }

    @Override
    public String toString() {
        return cut.toString();
    }

    public Set<NodeAig> getCut() {
        return cut;
    }
    
    
}