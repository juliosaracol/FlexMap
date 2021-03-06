package kcutter;

import aig.*;
import brc.*;
import FlexMap.Algorithms;
import java.util.TreeMap;

/**
 * Classe que implementa o caminhamento bfs com cálculo do BRC no Kcut
 * @author Julio Saraçol
 */
public class bfsNodeAigVisitorKCutsBrc extends bfsNodeAigVisitor{

    protected AigCutBrc cut;
    
    public bfsNodeAigVisitorKCutsBrc(AigCutBrc cut){
        super();
        this.cut = cut;
    }
    
    @Override
    public void function(NodeAig nodeAigActual) 
    {      
        BRC  father1,father2;
        if((nodeAigActual.isInput())||((this.cut.contains(nodeAigActual))))
            return;
        if((nodeAigActual.isOutput())&&(nodeAigActual.getParents().size()==1))
        {
                nodeAigActual.getParents().get(0).accept(this);
                String father = nodeAigActual.getParents().get(0).getName();
                father1 = cut.getBrcVariables().get(father);
                BRC brcNode = BRCHandler.not(father1);
                TreeMap<String,BRC> newCutBrc = cut.getBrcVariables();
                newCutBrc.put(nodeAigActual.getName(),brcNode);
                cut.setBrcVariables(newCutBrc);
                return;
        }           
        else
        {
            if((cut.size() > 1))
            {
               if(nodeAigActual.isInverter()){
                   //if(this.cut.contains(nodeAigActual)){
                    //father1 = cut.getBrcVariables().get(nodeAigActual.getName());
                    //BRC brcNode = BRCHandler.not(father1);
                    //TreeMap<String,BRC> newCutBrc = cut.getBrcVariables();
                    //newCutBrc.put(nodeAigActual.getName(),brcNode);
                    //cut.setBrcVariables(newCutBrc);
                    //return;
                   //}
                   //else{
                    if(!this.cut.contains(nodeAigActual.getParents().get(0)))
                        nodeAigActual.getParents().get(0).accept(this);
                    String father1Name = nodeAigActual.getParents().get(0).getName();
                    father1 = cut.getBrcVariables().get(father1Name);
                    father1 = BRCHandler.not(father1);                
                    TreeMap<String,BRC> newCutBrc = cut.getBrcVariables();
                    newCutBrc.put(nodeAigActual.getName(),father1);
                    cut.setBrcVariables(newCutBrc);
                    return;                                           
                   //}
               } 
               if(nodeAigActual.getParents().size() > 1){            
                if(!this.cut.contains(nodeAigActual.getParents().get(0)))
                    nodeAigActual.getParents().get(0).accept(this);
                if(!this.cut.contains(nodeAigActual.getParents().get(1)))
                    nodeAigActual.getParents().get(1).accept(this);                
                String father1Name = nodeAigActual.getParents().get(0).getName();
                father1 = cut.getBrcVariables().get(father1Name);
                if(Algorithms.isInverter(nodeAigActual, nodeAigActual.getParents().get(0)))
                    father1 = BRCHandler.not(father1);                
                String father2Name = nodeAigActual.getParents().get(1).getName();
                father2 = cut.getBrcVariables().get(father2Name);               
                if(Algorithms.isInverter(nodeAigActual, nodeAigActual.getParents().get(1)))
                    father2 = BRCHandler.not(father2);                
                BRC brcNode = BRCHandler.and(father1, father2);
                TreeMap<String,BRC> newCutBrc = cut.getBrcVariables();
                newCutBrc.put(nodeAigActual.getName(),brcNode);
                cut.setBrcVariables(newCutBrc);
                return;
              }
            }
            else{
              if((this.cut.size()==1)&&(this.cut.contains(nodeAigActual)))
                return;                                   
              if(!this.cut.contains(nodeAigActual.getParents().get(0)))
                 nodeAigActual.getParents().get(0).accept(this);
              String father1Name = nodeAigActual.getParents().get(0).getName();
              father1 = cut.getBrcVariables().get(father1Name);
              father1 = BRCHandler.not(father1);                
              TreeMap<String,BRC> newCutBrc = cut.getBrcVariables();
              newCutBrc.put(nodeAigActual.getName(),father1);
              cut.setBrcVariables(newCutBrc);
              return;                                           
            }
        }
    }
    
    /** Override pois precisa abandonar a busca caso encontre entrada do corte*/
    @Override
    public void visit(NodeAigGate nodeAigGate) 
    {
        if(nodesBfs.contains(nodeAigGate)){
            return;
        }
        else
        {
            nodesBfs.add(nodeAigGate);
            function(nodeAigGate);
            if(!this.cut.contains(nodeAigGate))
            {
                for(int i=0;i<nodeAigGate.getParents().size();i++)
                {
                    if(!this.cut.contains(nodeAigGate.getParents().get(i))){
                     list.add(nodeAigGate.getParents().get(i));
                     this.states++;
                    }
                }
            }
        }
        if(list.size() > 0)
        {
            NodeAig temp =  list.get(0);
            list.remove(0);        
            temp.accept(this);
        }
    }
    
    /** Override pois precisa abandonar a busca caso encontre entrada do corte*/
    @Override
    public void visit(NodeAigOutput nodeAigOutput) 
    {
        if(nodesBfs.contains(nodeAigOutput)){
            return;
        }
        else
        {
            nodesBfs.add(nodeAigOutput);
            function(nodeAigOutput);
            if(!this.cut.contains(nodeAigOutput))
            {
                for(int i=0;i<nodeAigOutput.getParents().size();i++)
                {
                    if(!this.cut.contains(nodeAigOutput.getParents().get(i))){
                     list.add(nodeAigOutput.getParents().get(i));
                     this.states++;
                    }
                }
            }
        }
        if(list.size() > 0)
        {
            NodeAig temp =  list.get(0);
            list.remove(0);        
            temp.accept(this);
        }
    }
    
    
    public void visit(NodeAigInverter nodeAigInverter) 
    {
        if(nodesBfs.contains(nodeAigInverter)){
            return;
        }
        else
        {
            nodesBfs.add(nodeAigInverter);
            function(nodeAigInverter);
            if(!this.cut.contains(nodeAigInverter))
            {
                for(int i=0;i<nodeAigInverter.getParents().size();i++)
                {
                    if(!this.cut.contains(nodeAigInverter.getParents().get(i))){
                        list.add(nodeAigInverter.getParents().get(i));
                        this.states++;
                    }
                }
            }
        }
        if(list.size() > 0)
        {
            NodeAig temp =  list.get(0);
            list.remove(0);        
            temp.accept(this);
        }
    }
}
