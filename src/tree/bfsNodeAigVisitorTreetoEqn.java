package tree;

import FlexMap.Algorithms;
import aig.*;
import java.util.*;
/**
 * Classe que aplica caminhamento para gerar eqn da Tree
 * @author Julio Saraçol
 */
public class bfsNodeAigVisitorTreetoEqn extends bfsNodeAigVisitor
{
    public Set<NodeAig> handler;
    protected Map<NodeAig,Set<NodeAig>> eqn ;

    public bfsNodeAigVisitorTreetoEqn() {
        super();
        this.handler  = new HashSet<NodeAig>();
        this.eqn      = new HashMap<NodeAig, Set<NodeAig>>();
    }
           
    @Override
    public void function(NodeAig nodeAigActual) {
       if(this.eqn.containsKey(nodeAigActual))
            return;
       if(nodeAigActual.isInput())
       {
           this.eqn.put(nodeAigActual, new HashSet<NodeAig>());
           this.eqn.get(nodeAigActual).add(nodeAigActual);
           return;
       }
       Set<NodeAig> eqnNodes = new HashSet<NodeAig>();
       for(NodeAig father: nodeAigActual.getParents())
          eqnNodes.add(father);
       this.eqn.put(nodeAigActual, eqnNodes);
    }
    
    // Método que a partir de um nodo gera a string que representa a equação da Tree em função das entradas do corte
    public String getEqn(NodeAig nodeAigActual) 
    {
         String type="";
         String subEqn = "";
         if((nodeAigActual.getName().equals("0"))||(nodeAigActual.getName().equals("1"))) //constant
         {
             subEqn += "_"+nodeAigActual.getName();
             return subEqn;
         }
         if(nodeAigActual.isInput())
            return "["+nodeAigActual.getName()+"]";
        else
        {
            Set<NodeAig> nodeEqns =this.eqn.get(nodeAigActual);
            if(nodeEqns == null)
            {
                function(nodeAigActual);
                nodeEqns = this.eqn.get(nodeAigActual);
            }
            for(NodeAig node: nodeEqns)
            {
              type = "*";
              if(node.isOR())
                  type = "+";
              if(Algorithms.isInverter(nodeAigActual, node))
                   subEqn += "!("+getEqn(node)+")"+type;
              else
                   subEqn += getEqn(node)+type;
            }
            if((subEqn.length() > 1)&&(subEqn.substring(subEqn.length()-1,subEqn.length()).equals(type)))
              subEqn = subEqn.substring(0, subEqn.length()-1);
            return subEqn;
        }
    }
    
    public String getEqnDescription(NodeAig nodeAigActual)
    {        
        String eqnNode = getEqn(nodeAigActual);
        return eqnNode;
    }   
    
}
