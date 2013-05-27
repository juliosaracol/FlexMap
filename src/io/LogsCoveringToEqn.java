package io;
import aig.*;
import kcutter.*;

import java.io.*;
import java.util.*;
import library.FunctionData;

/**
 * Classe que gera saídas eqn a partir de uma cobertura
 * @author Julio Saraçol
 */
public class LogsCoveringToEqn extends Logs{
       /**Método que gera descrição eqn a partir da cobertura em Kcuts*/
   public static String coveringToEqn(Aig myAig, Map<NodeAig,AigCut> covering) throws FileNotFoundException 
   {
        String outString = createEqn(myAig); 
        ArrayList<NodeAig> nodesPath = new ArrayList<NodeAig>();
        String outString1="";
        for(Map.Entry<NodeAig,AigCut> cell: covering.entrySet())
        {
            if((!cell.getKey().isInput())&&(!nodesPath.contains(cell.getKey())))
            {
                if(!(((Integer.parseInt(cell.getKey().getName())%2) != 0)&&(cell.getValue().size()==1)))
                {
                  dfsNodeAigVisitorCutToEqn dfsEqn = new dfsNodeAigVisitorCutToEqn(cell.getValue());
                  if((Integer.parseInt(cell.getKey().getName())%2) != 0)
                  {
                      if(!nodesPath.contains(cell.getKey().getParents().get(0)))
                      {
                        nodesPath.add(cell.getKey().getParents().get(0));
                        cell.getKey().getParents().get(0).accept(dfsEqn);
                        outString1 += "["+(String.valueOf(Integer.parseInt(cell.getKey().getName())-1))+"]="+dfsEqn.getEqnDescription(cell.getKey().getParents().get(0))+";\n";
                      }
                  }
                  else
                  {
                      if(!nodesPath.contains(cell.getKey()))
                      {
                        nodesPath.add(cell.getKey());
                        cell.getKey().accept(dfsEqn);
                        outString1 += "["+cell.getKey().getName()+"]="+dfsEqn.getEqnDescription(cell.getKey())+";\n";
                      }
                  }
                }
            }
        }
        outString+=outString1;
        System.out.println(outString);
        return outString;        
  }    
   /**Método que gera descrição eqn a partir da cobertura em KcutsInverters*/
   public static String coveringToEqn(AigInverter myAigInverter, Map<NodeAig,AigCut> covering) throws FileNotFoundException 
   {
        String outString = createEqn(myAigInverter); 
        ArrayList<NodeAig> nodesPath = new ArrayList<NodeAig>();
        String outString1="";
        for(Map.Entry<NodeAig,AigCut> cell: covering.entrySet())
        {
            if((!cell.getKey().isInput())&&(!nodesPath.contains(cell.getKey())))
            {
                if(!(((Integer.parseInt(cell.getKey().getName())%2) != 0)&&(cell.getValue().size()==1)))
                {
                  dfsNodeAigVisitorCutInverterToEqn dfsEqn = new dfsNodeAigVisitorCutInverterToEqn(cell.getValue());
                  if(((Integer.parseInt(cell.getKey().getName())%2) != 0)&&(cell.getKey().isOutput()))
                  {
                      if((!nodesPath.contains(cell.getKey().getParents().get(0)))
                             &&(!covering.containsKey(cell.getKey().getParents().get(0))))
                      {
                        nodesPath.add(cell.getKey());
                        nodesPath.add(cell.getKey().getParents().get(0));
                        cell.getKey().getParents().get(0).accept(dfsEqn);
                        outString1 += "["+(String.valueOf(Integer.parseInt(cell.getKey().getName())-1))+"]="+dfsEqn.getEqnDescription(cell.getKey().getParents().get(0))+";\n";
                        outString1 += "["+cell.getKey().getName()+"]=!(["+cell.getKey().getParents().get(0).getName()+"]);\n";
                      }
                  }
                  else
                  {
                      if(!nodesPath.contains(cell.getKey())){
                        nodesPath.add(cell.getKey());
                        cell.getKey().accept(dfsEqn);
                        outString1 += "["+cell.getKey().getName()+"]="+dfsEqn.getEqnDescription(cell.getKey())+";\n";
                      }
                  }
                }
                else{
                        if(!nodesPath.contains(cell.getKey())){
                         nodesPath.add(cell.getKey());
                         outString1 += "["+cell.getKey().getName()+"]=!["+cell.getKey().getParents().get(0).getName()+"];\n";
                        }
                    }
            }
        }
            
        outString+=outString1;
        System.out.println(outString);
        return outString;
  } 
   /**Método que gera descrição eqn a partir da cobertura em KcutsInverters e biblioteca  .genlib*/
   public static String coveringToEqn(AigInverter myAigInverter, Map<NodeAig,AigCutBrc> covering,
           Map<AigCutBrc,FunctionData> matchings) throws FileNotFoundException{
        String outString = createEqn(myAigInverter); 
        ArrayList<NodeAig> nodesPath = new ArrayList<NodeAig>();
        String outString1="";
        for(Map.Entry<NodeAig,AigCutBrc> cell: covering.entrySet())
        {
            if((!cell.getKey().isInput())&&(!nodesPath.contains(cell.getKey())))
            {
                if(!(((Integer.parseInt(cell.getKey().getName())%2) != 0)&&(cell.getValue().size()==1)))
                {
                  dfsNodeAigVisitorCutInverterToEqn dfsEqn = new dfsNodeAigVisitorCutInverterToEqn(cell.getValue());
                  if(((Integer.parseInt(cell.getKey().getName())%2) != 0)&&(cell.getKey().isOutput()))
                  {
                      if((!nodesPath.contains(cell.getKey().getParents().get(0)))
                             &&(!covering.containsKey(cell.getKey().getParents().get(0))))
                      {
                        nodesPath.add(cell.getKey());
                        nodesPath.add(cell.getKey().getParents().get(0));
                        cell.getKey().getParents().get(0).accept(dfsEqn);
                        outString1 += "["+(String.valueOf(Integer.parseInt(cell.getKey().getName())-1))+"]="+dfsEqn.getEqnDescription(cell.getKey().getParents().get(0))+";\n";
                        outString1 += "["+cell.getKey().getName()+"]=!(["+cell.getKey().getParents().get(0).getName()+"]);\n";
                      }
                  }
                  else
                  {
                      if(!nodesPath.contains(cell.getKey())){
                        nodesPath.add(cell.getKey());
                        cell.getKey().accept(dfsEqn);
                        outString1 += "["+cell.getKey().getName()+"]="+dfsEqn.getEqnDescription(cell.getKey())+";\n";
                      }
                  }
                }
                else{
                        if(!nodesPath.contains(cell.getKey())){
                         nodesPath.add(cell.getKey());
                         outString1 += "["+cell.getKey().getName()+"]=!["+cell.getKey().getParents().get(0).getName()+"];\n";
                        }
                    }
            }
        }
            
        outString+=outString1;
        System.out.println(outString);
        return outString;
   }
   /**Método que gera descrição eqn a partir da cobertura em KcutsInverters e biblioteca  .genlib*/
   public static String coveringToEqn(Aig myAig, Map<NodeAig,AigCutBrc> covering,
           Map<AigCutBrc,FunctionData> matchings) throws FileNotFoundException{
        String outString = createEqn(myAig); 
        ArrayList<NodeAig> nodesPath = new ArrayList<NodeAig>();
        String outString1="";
        for(Map.Entry<NodeAig,AigCutBrc> cell: covering.entrySet())
        {
            if((!cell.getKey().isInput())&&(!nodesPath.contains(cell.getKey())))
            {
                if(!(((Integer.parseInt(cell.getKey().getName())%2) != 0)&&(cell.getValue().size()==1)))
                {
                  dfsNodeAigVisitorCutToEqn dfsEqn = new dfsNodeAigVisitorCutToEqn(cell.getValue());
                  if(((Integer.parseInt(cell.getKey().getName())%2) != 0)&&(cell.getKey().isOutput()))
                  {
                      if((!nodesPath.contains(cell.getKey().getParents().get(0)))
                             &&(!covering.containsKey(cell.getKey().getParents().get(0))))
                      {
                        nodesPath.add(cell.getKey());
                        nodesPath.add(cell.getKey().getParents().get(0));
                        cell.getKey().getParents().get(0).accept(dfsEqn);
                        outString1 += "["+(String.valueOf(Integer.parseInt(cell.getKey().getName())-1))+"]="+dfsEqn.getEqnDescription(cell.getKey().getParents().get(0))+";\n";
                        outString1 += "["+cell.getKey().getName()+"]=!(["+cell.getKey().getParents().get(0).getName()+"]);\n";
                      }
                  }
                  else
                  {
                      if(!nodesPath.contains(cell.getKey())){
                        nodesPath.add(cell.getKey());
                        cell.getKey().accept(dfsEqn);
                        outString1 += "["+cell.getKey().getName()+"]="+dfsEqn.getEqnDescription(cell.getKey())+";\n";
                      }
                  }
                }
                else{
                        if(!nodesPath.contains(cell.getKey())){
                         nodesPath.add(cell.getKey());
                         outString1 += "["+cell.getKey().getName()+"]=!["+cell.getKey().getParents().get(0).getName()+"];\n";
                        }
                    }
            }
        }
            
        outString+=outString1;
        System.out.println(outString);
        return outString;
   }
}