/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kcutter;

import aig.*;
import java.util.*;

/**
 *
 * @author julio
 */
public class CutterKCutsInverter extends CutterKCuts {

    public CutterKCutsInverter(Aig aig, int k) {
        super(aig, k);
    }
    
    @Override
    protected Set<AigCut> computeKcuts(NodeAig nodeCurrent) 
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
            if(nodeCurrent.getParents().size() == 1) //inverter
            {
                Set<AigCut> kcut = new HashSet<AigCut>();
                kcut.add(new AigCut(nodeCurrent));
                if(!this.cuts.containsKey(nodeCurrent.getParents().get(0)))
                   computeKcuts(nodeCurrent.getParents().get(0));
                kcut.addAll(this.cuts.get(nodeCurrent.getParents().get(0)));                   
                this.cuts.put(nodeCurrent,kcut);
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
               Set<AigCut> combined = combineIrredundant(k1, k2);
               combined.add(new AigCut(nodeCurrent));
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
