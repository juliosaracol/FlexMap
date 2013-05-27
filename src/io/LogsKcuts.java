package io;
import aig.*;
import kcutter.*;

import java.io.*;
import java.util.*;
/**
 * Classe que gera arquivos de saida para cortes
 * @author  Julio Saraçol
 */
public class LogsKcuts extends Logs {
      //*-------------------------For Kcuts---------------------------------------
  public static void LogsAigKcuts(String fileLog, CutterK cutterK)throws FileNotFoundException, IOException
  {
        File outFile = new File(fileLog);
        FileOutputStream buffStart;
        
        outFile.setWritable(true);       
        buffStart = new FileOutputStream(outFile,true);
        String out = "#############KCUTS###################\nSize Cut K= "+cutterK.getLimit()+"\n"+"Cuts:\n";
        buffStart.write(out.getBytes());
        String nodes = "Node:";
        Iterator<Map.Entry<NodeAig,Set<AigCut>>> node = cutterK.getCuts().entrySet().iterator();
        while(node.hasNext())
        {
           Map.Entry<NodeAig,Set<AigCut>> cuts = node.next();
           buffStart.write(nodes.getBytes());
           buffStart.write(cuts.getKey().getName().getBytes());
           buffStart.write('\n');
           for(AigCut singleCut: cuts.getValue())
             buffStart.write(singleCut.getBytesForLog().getBytes());
        }
        nodes = "########################################";
        buffStart.write(nodes.getBytes());
        buffStart.close();
        System.out.println("#Log Kcuts Executado com Sucesso");
  }
  public static void LogsAigKcuts(String fileLog, CutterK cutterK,NodeAig nodeSelect)throws FileNotFoundException, IOException
  {
        File outFile = new File(fileLog);
        FileOutputStream buffStart;
        
        outFile.setWritable(true);       
        buffStart = new FileOutputStream(outFile,true);
        String out = "#############KCUTS###################\nCut K= "+cutterK.getLimit()+"\n";
        buffStart.write(out.getBytes());
        String nodes = "Node:";
        Set<AigCut> cuts = cutterK.getCuts().get(nodeSelect);
        buffStart.write(nodes.getBytes());
        buffStart.write(nodeSelect.getName().getBytes());
        buffStart.write('\n');
        for(AigCut singleCut: cuts)
             buffStart.write(singleCut.getBytesForLog().getBytes());
        nodes = "########################################";
        buffStart.write(nodes.getBytes());
        buffStart.close();
        System.out.println("#Log Kcuts Executado com Sucesso");
  }
  public static void LogsAigKcuts(String fileLog, CutterKCutsLibrary cutterKLibrary)throws FileNotFoundException, IOException
  {
        File outFile = new File(fileLog);
        FileOutputStream buffStart;
        
        outFile.setWritable(true);       
        buffStart = new FileOutputStream(outFile,true);
        String out = "#############KCUTS###################\nSize Cut K= "+cutterKLibrary.getLimit()+"\n"+"Cuts:\n";
        buffStart.write(out.getBytes());
        String nodes = "Node:";
        Iterator<Map.Entry<NodeAig,Set<AigCutBrc>>> node = cutterKLibrary.getCutsBrc().entrySet().iterator();
        while(node.hasNext())
        {    
           Map.Entry<NodeAig,Set<AigCutBrc>> cuts = node.next();
           buffStart.write(nodes.getBytes());
           buffStart.write(cuts.getKey().getName().getBytes());
           buffStart.write('\n');
           for(AigCut singleCut: cuts.getValue())
             buffStart.write(singleCut.getBytesForLog().getBytes());
        }
        nodes = "########################################";
        buffStart.write(nodes.getBytes());
        buffStart.write(cutterKLibrary.logs.getBytes());
        buffStart.close();
        System.out.println("#Log Kcuts Library Executado com Sucesso");
  }
  public static void LogsAigKcuts(String fileLog, CutterKCutsLibrary cutterKLibrary,NodeAig nodeSelect)throws FileNotFoundException, IOException
  {
        File outFile = new File(fileLog);
        FileOutputStream buffStart;
        
        outFile.setWritable(true);       
        buffStart = new FileOutputStream(outFile,true);
        String out = "#############KCUTS###################\nCut K= "+cutterKLibrary.getLimit()+"\n";
        buffStart.write(out.getBytes());
        String nodes = "Node:";
        Set<AigCutBrc> cuts = cutterKLibrary.getCutsBrc().get(nodeSelect);
        buffStart.write(nodes.getBytes());
        buffStart.write(nodeSelect.getName().getBytes());
        buffStart.write('\n');
        for(AigCutBrc singleCut: cuts)
             buffStart.write(singleCut.getBytesForLog().getBytes());
        nodes = "########################################";
        buffStart.write(nodes.getBytes());
        buffStart.close();
        System.out.println("#Log Kcuts Library Executado com Sucesso");
  }
  //*---------------------------------------------------------------------------   
    
}
