package kcutter;
import aig.*;

/**
 * Classe que modela algoritmos baseados em K-cuts
 * @author Julio Saraçol
 */
public abstract class CutterK extends Cutter
{
    public CutterK(Aig aig, int limit) {
        super(aig, limit);
    }
    
}
