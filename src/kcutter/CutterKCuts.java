package kcutter;
import aig.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Classe que aplica os K-cuts classico
 * @author Julio Saraçol
 */
public class CutterKCuts extends CutterK
{    
    public CutterKCuts(Aig aig, int k) 
    {
        super(aig,k);
        computeAllCuts();
    }

    public CutterKCuts(Aig aig,NodeAig node, int k) throws CloneNotSupportedException 
    {
        super(aig,k);
        if(node == null){
            System.out.println("NODO NÃO EXISTE NO CIRCUITO");
            System.exit(-1);
        }
        computeAllCuts(node);
    }

    @Override
    protected void computeAllCuts() 
    {
        for(NodeAig node : aig.getAllNodesAig())
           try {
            computeKcuts(node);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(CutterKCuts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void computeAllCuts(NodeAig node) throws CloneNotSupportedException 
    {
        computeKcuts(node);
    }

    protected Set<AigCut> computeKcuts(NodeAig nodeCurrent) throws CloneNotSupportedException 
    {
        
        if(this.cuts.containsKey(nodeCurrent))
            return Collections.unmodifiableSet(this.cuts.get(nodeCurrent)); 
        if((nodeCurrent.isInput())||((nodeCurrent.isOutput())&&(nodeCurrent.getParents().isEmpty()))) //constant
        {
            Set<AigCut> kcut = new HashSet<AigCut>();
            kcut.add(new AigCut(nodeCurrent));
            this.cuts.put(nodeCurrent,kcut);
            return null;
        }
        else
        {               
            if((nodeCurrent.isOutput())&&(nodeCurrent.getParents().size() == 1)) //inverter
            {
                if(!this.cuts.containsKey(nodeCurrent.getParents().get(0)))
                   computeKcuts(nodeCurrent.getParents().get(0));
                Set<AigCut> clone =  new HashSet<AigCut>();
                for(AigCut father: this.cuts.get(nodeCurrent.getParents().get(0)))
                    clone.add(father.clone());
                this.cuts.put(nodeCurrent,clone);
                this.cuts.get(nodeCurrent).add(new AigCut(nodeCurrent.getParents().get(0)));
            }
            else
            {
              if(nodeCurrent.getParents().size()>0)
              {
               if(!this.cuts.containsKey(nodeCurrent.getParents().get(0)))
                  computeKcuts(nodeCurrent.getParents().get(0));
               Set<AigCut> k1 = this.cuts.get(nodeCurrent.getParents().get(0));
               if(!this.cuts.containsKey(nodeCurrent.getParents().get(1)))
                  computeKcuts(nodeCurrent.getParents().get(1));
               Set<AigCut> k2 = this.cuts.get(nodeCurrent.getParents().get(1));
               Set<AigCut> combined = combineIrredundant(nodeCurrent,k1,k2);
               //combined.add(new AigCut(nodeCurrent));
               if(this.cuts.containsKey(nodeCurrent))
                   this.cuts.get(nodeCurrent).addAll(combined);
               else
                   this.cuts.put(nodeCurrent, combined);              
              }
            }
            return null;
        }
    }
}
