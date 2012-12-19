package tree;
import aig.Aig;

/**
 * Classe transforma o Aig em trees somente segmentando em pontos de reconvergências
 * @author Julio Saraçol
 */
public class TreesBasic extends Trees
{
    public TreesBasic(Aig aig) 
    {
        super(aig);
    }
    
}
