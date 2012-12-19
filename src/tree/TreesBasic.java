package tree;

import aig.Aig;

/**
 * Classe que aplica árvores no Aig somente segmentando em ponstos de reconvergências
 * @author Julio Saraçol
 */
public class TreesBasic extends Trees
{
    public TreesBasic(Aig aig) 
    {
        super(aig);
    }
    
}
