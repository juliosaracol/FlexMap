package kcutter;

import aig.*;
import java.util.*;

/**
 * Classe que define a estrutura de algoritmos para cortes
 * @author Julio Saraçol
 */
public abstract class Cutter 
{
    protected int limit;
    /**Referencia ao circuito inicial*/
    protected Aig aig;
    /**estrutura de controle dos cortes de cada nodo*/
    protected Map<NodeAig, Set<AigCut>> cuts;
    /**Construtor
    * @param aig do circuito
    * @param limit (k ou l)
    */
    public Cutter(Aig aig, int limit) {
        this.aig    = aig;
        this.limit  = limit;
        this.cuts   =  new HashMap<NodeAig, Set<AigCut>>();
    }
    /**
     * Getter for the cuts already calculated
     * @return  The Cuts
     */
    public Map<NodeAig, Set<AigCut>> getCuts() {
        if (cuts == null) {
            computeAllCuts();
        }
        return cuts;
    }
    /**Método Básico de interface dos Cuts*/
    protected abstract void computeAllCuts();
    /**
     * Combines two sets of cuts and assures that the cuts in the resulting set
     * of cuts are irredundant
     * @param cuts0 Set of cuts to be combined
     * @param cuts1 Set of cuts to be combined
     * @return  A set of cuts, irredundant, and respecting size limit
     */
    protected Set<AigCut> combineIrredundant(Set<AigCut> k1, Set<AigCut> k2)
    {
        //Combine all cuts
        Set<AigCut> combinedCuts = combineCuts(k1, k2);
        //Remove reduntant cuts
        makeIrredundantCuts(combinedCuts);
        return combinedCuts;
    }
    /** Método que combina os dois sub-Cuts*/
    protected Set<AigCut> combineCuts(Set<AigCut> k1, Set<AigCut> k2)
    {
        Set<AigCut> combinedCuts = new HashSet<AigCut>();
        for (AigCut aigCut1 : k1) 
        {
            for (AigCut aigCut2 : k2) 
            {
                AigCut currentCut = new AigCut();
                currentCut.addAll(aigCut1);
                currentCut.addAll(aigCut2);
                if (currentCut.size() <= this.limit)
                    combinedCuts.add(currentCut);
            }
        }
        return combinedCuts;        
    }
    /** Método que a partir do combinado retira os irredundantes*/
    protected void makeIrredundantCuts(Set<AigCut> combinedCuts)
    {       
        Set<AigCut> removeCuts = new HashSet<AigCut>();
        for (AigCut aigCut0 : combinedCuts) 
        {
            for (AigCut aigCut1 : combinedCuts) 
            {
                if ((aigCut1.getSign() & ~aigCut0.getSign()) != 0) 
                   continue;
                if ((aigCut0 != aigCut1) && aigCut0.containsAll(aigCut1)) {
                    removeCuts.add(aigCut0);
                    break;
                }
            }
        }
        combinedCuts.removeAll(removeCuts);
    }
    public void showAllcuts()
    {
       System.out.println("############CUTS#######################");
       Iterator<Map.Entry<NodeAig,Set<AigCut>>> node = cuts.entrySet().iterator();
       while(node.hasNext())
       {
           Map.Entry<NodeAig,Set<AigCut>> cuts = node.next();
           System.out.println("Node:"+cuts.getKey().getName());
           for(AigCut singleCut: cuts.getValue())
           {
             singleCut.showCut();
           }   
       }
       System.out.println(cuts.size()+" Cortes");
       System.out.println("########################################");
    }   
    public void showAllcuts(NodeAig node)
    {
       System.out.println("############CUTS#######################");
       System.out.println("Node:"+node.getName());
           for(AigCut singleCut: this.cuts.get(node))
           {
             singleCut.showCut();
           }   
       System.out.println(this.cuts.get(node).size()+" Cortes");
       System.out.println("########################################");
    }

    public Aig getAig() {
        return aig;
    }

    public int getLimit() {
        return limit;
    }
    
    
}
