package kcutter;
import FlexMap.*;
import aig.*;
import java.util.*;

/**
 * Classe aplica o caminhamento para gerar EQN em Aig_Inverter
 * @author Julio Saraçol
 */
public class dfsNodeAigVisitorCutInverterToEqn extends dfsNodeAigVisitorCutToEqn{

    public dfsNodeAigVisitorCutInverterToEqn(AigCut cut) {
        super(cut);
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
        String subEqn = "";
        if((nodeAigActual.isInverter())||(nodeAigActual.getParents().size()==1))
            subEqn = "!("+subEqn;
        if((!nodeAigActual.isInput())&&(this.cut.contains(nodeAigActual))&&(this.cut.size()==1))
        {           
            if((nodeAigActual.getName().equals("0"))||(nodeAigActual.getName().equals("1"))) {//constant
                subEqn += "_"+nodeAigActual.getName();
                return subEqn;
            }
            Set<NodeAig> nodeEqns = this.eqn.get(nodeAigActual);
            for(NodeAig node: nodeEqns)
                   subEqn += "["+node.getName()+"]*";
            subEqn = subEqn.substring(0, subEqn.length()-1);
            if((nodeAigActual.isInverter())||(nodeAigActual.getParents().size()==1))
                return subEqn+")";
            return subEqn;
        }
        if(cut.contains(nodeAigActual))
            return "["+nodeAigActual.getName()+"]";
        else
        {
            Set<NodeAig> nodeEqns = this.eqn.get(nodeAigActual);
            if(nodeEqns == null){
                function(nodeAigActual);
                nodeEqns = this.eqn.get(nodeAigActual);
            }
            for(NodeAig node: nodeEqns){
                   subEqn += getEqn(node)+"*";
            }
            subEqn = subEqn.substring(0, subEqn.length()-1);
            if((nodeAigActual.isInverter())||(nodeAigActual.getParents().size()==1))
                return subEqn+")";
            else
                return subEqn;
        }
    }
    
    @Override
    public String getEqnDescription(NodeAig nodeAigActual)
    {
        String eqnNode = getEqn(nodeAigActual);
        return eqnNode;
    }
}
