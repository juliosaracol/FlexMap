package tree;

import aig.*;
import io.*;
import java.io.*;
import java.util.*;


/**
 *
 * @author julio
 */
public class TreesElis extends Trees{

    public TreesElis(Aig aig) {
        super(aig);
    }

    @Override
    public String getEqn() throws FileNotFoundException {
       String outString ="";
       outString = Logs.createTreesEqn(this);
       String outString1="";
       for(Tree tree: getRoots())
       {
           if((Integer.parseInt(tree.getRoot().getName())%2) != 0)
           {
                bfsNodeAigVisitorTreetoEqn bfsEqn = new bfsNodeAigVisitorTreetoEqn();
                if(tree.getRoot().getParents().get(0).getParents().size() > 1)
                {
                 tree.getRoot().getParents().get(0).accept(bfsEqn);  
                 outString1 += "["+tree.getRoot().getParents().get(0).getName()+"]="+bfsEqn.getEqnDescription(tree.getRoot().getParents().get(0))+";\n";   
                }
           }
           else
           {
               bfsNodeAigVisitorTreetoEqn bfsEqn = new bfsNodeAigVisitorTreetoEqn();
               tree.getRoot().accept(bfsEqn);  
               outString1 += "["+tree.getRoot().getName()+"]="+bfsEqn.getEqnDescription(tree.getRoot())+";\n";   
           }
       }
       outString+=outString1;
       System.out.println(outString);
       return outString;
    }
    
    
    
    
    
}
