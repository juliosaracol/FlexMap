package kcutter;

import aig.*;
import FlexMap.Algorithms;

import java.util.*;
/**
 * Classe de caminhamento para cut 
 * @author Julio Saraçol
 */
public class dfsNodeAigVisitorCutToEqn extends dfsNodeAigVisitor
{
    protected AigCut cut;
    protected Map<NodeAig,Set<NodeAig>> eqn;

    public dfsNodeAigVisitorCutToEqn(AigCut cut) {
        super();
        this.eqn        = new HashMap<NodeAig, Set<NodeAig>>();
        this.cut        = cut;
    }
    
    @Override
    public void function(NodeAig nodeAigActual) {
       if(this.eqn.containsKey(nodeAigActual))
        return;
       if((!nodeAigActual.isInput())&&(this.cut.contains(nodeAigActual))&&(this.cut.size()==1))
       {
            Set<NodeAig> eqnNodes = new HashSet<NodeAig>();
            for(NodeAig father: nodeAigActual.getParents())
                eqnNodes.add(father);
            this.eqn.put(nodeAigActual, eqnNodes);
            return;      
       }        
       if(nodeAigActual.isInput())
       {
           this.eqn.put(nodeAigActual, new HashSet<NodeAig>());
           this.eqn.get(nodeAigActual).add(nodeAigActual);
           return;
       }
       Set<NodeAig> eqnNodes =  new HashSet<NodeAig>();
       for(NodeAig father: nodeAigActual.getParents())
           eqnNodes.add(father);
       this.eqn.put(nodeAigActual, eqnNodes);
    }

    // Método que a partir de um nodo gera a string que representa a equação do corte em função das entradas do corte
    public String getEqn(NodeAig nodeAigActual) 
    {
        if((!nodeAigActual.isInput())&&(this.cut.contains(nodeAigActual))&&(this.cut.size()==1))
        {
            String subEqn = "";
            if((nodeAigActual.getName().equals("0"))||(nodeAigActual.getName().equals("1"))) //constant
            {
                subEqn += "_"+nodeAigActual.getName();
                return subEqn;
            }
            Set<NodeAig> nodeEqns = this.eqn.get(nodeAigActual);
            for(NodeAig node: nodeEqns)
            {
              if(Algorithms.isInverter(nodeAigActual, node))
                   subEqn += "!(["+node.getName()+"])*";
              else
                   subEqn += "["+node.getName()+"]*";
            }
            subEqn = subEqn.substring(0, subEqn.length()-1);
            return subEqn;
        }
        if(cut.contains(nodeAigActual))
            return "["+nodeAigActual.getName()+"]";
        else
        {
            String subEqn ="";
            Set<NodeAig> nodeEqns =this.eqn.get(nodeAigActual);
            if(nodeEqns == null)
            {
                function(nodeAigActual);
                nodeEqns = this.eqn.get(nodeAigActual);
            }
            for(NodeAig node: nodeEqns)
            {
              if(Algorithms.isInverter(nodeAigActual, node))
                   subEqn += "!("+getEqn(node)+")*";
              else
                   subEqn += getEqn(node)+"*";
            }
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
