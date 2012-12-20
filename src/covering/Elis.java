package covering;

import aig.*;
import tree.*;
import FlexMap.Algorithms;

import java.util.*;
/**
 * Classe que aplica a cobertura de acordo com  algoritmo da Elis (Correia,2005)
 * @author Julio Saraçol
 */
public class Elis 
{

    protected Trees trees;
    protected Integer s;
    protected Integer p;
    
    public Elis(TreesElis trees, Integer s, Integer p)
    {
        this.s = s;
        this.p = p;
        this.trees = trees;
        Set<Tree> roots = trees.getRoots();
        for(Tree root : roots)
        {
            dfsNodeAigVisitorOrTree dfsOrGate = new dfsNodeAigVisitorOrTree(trees,root);
            root.getRoot().accept(dfsOrGate);
        }
//        deMorgan();
        
    }

    public Integer getP() {
        return p;
    }

    public Integer getS() {
        return s;
    }

    public Trees getTrees() {
        return trees;
    }
}
