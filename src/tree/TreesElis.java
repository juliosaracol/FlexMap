package tree;

import aig.*;
import io.*;
import java.io.*;

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
           tree.show();
           if((!tree.getRoot().getName().contains("X"))&&((Integer.parseInt(tree.getRoot().getName())%2) != 0))
           {
                bfsTreeVisitorToEqn bfsEqn = new bfsTreeVisitorToEqn();
                if(tree.getRoot().getParents().get(0).getParents().size() > 1)
                {
                 tree.getRoot().getParents().get(0).accept(bfsEqn);  
                 outString1 += "["+tree.getRoot().getParents().get(0).getName()+"]="+bfsEqn.getEqnDescription(tree.getRoot().getParents().get(0))+";\n";   
                }
           }
           else
           {
               bfsTreeVisitorToEqn bfsEqn = new bfsTreeVisitorToEqn();
               tree.getRoot().accept(bfsEqn);  
               outString1 += "["+tree.getRoot().getName()+"]="+bfsEqn.getEqnDescription(tree.getRoot())+";\n";   
           }
       }
       outString+=outString1;
       System.out.println(outString);
       return outString;
    }
    
    
    
    
    
}
