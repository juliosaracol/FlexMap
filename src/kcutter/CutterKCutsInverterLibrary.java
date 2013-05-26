package kcutter;

import aig.*;
import java.util.*; 

/**
 * Classe que aplica os K-cutsInverter com Matching em Biblioteca de Células ".genlib"
 * @author Julio Saraçol
 */
public class CutterKCutsInverterLibrary extends CutterKCutsLibrary 
{

    public CutterKCutsInverterLibrary(Aig aig, int limit, String libraryName) throws Exception {
        super(aig, limit, libraryName);
    }

    public CutterKCutsInverterLibrary(Aig aig, NodeAig nodeCurrent, int limit, String libraryName) throws CloneNotSupportedException, Exception  {
        super(aig, nodeCurrent, limit, libraryName);
    }
    
    @Override
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
            if(nodeCurrent.getParents().size() == 1) //inverter
            {
                Set<AigCut> kcut = new HashSet<AigCut>();
                kcut.add(new AigCut(nodeCurrent));
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
              if(nodeCurrent.getParents().size()>1)
              {
               if(!this.cuts.containsKey(nodeCurrent.getParents().get(0)))
                  computeKcuts(nodeCurrent.getParents().get(0));
               Set<AigCut> k1 = this.cuts.get(nodeCurrent.getParents().get(0));
               if(!this.cuts.containsKey(nodeCurrent.getParents().get(1)))
                  computeKcuts(nodeCurrent.getParents().get(1));
               Set<AigCut> k2 = this.cuts.get(nodeCurrent.getParents().get(1));
               Set<AigCut> combined = combineIrredundant(nodeCurrent,k1, k2);
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
