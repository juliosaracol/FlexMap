package FlexMap;
import aig.*;
import io.*;
import kcutter.*;
import covering.*;
import tree.*;
import java.io.*;

/**
 * Classe principal ferramenta FlexMap
 * Universidade Federal de Pelotas-UFPel
 * Grupo de Arquiteturas e Circuitos Integrados-GACI
 * @version 1.0
 * @author Julio Saraçol
 * */
public class Main 
{
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception 
    {
      if(args.length < 2) 
      { //não foram inseridos todos os argumentos necessários
        warning();
      }
      else
      { 
        if(!args[0].contains(".aag")){ // se nao conseguiu ler o arquivo
          warning();
          return;
        }  
        //--------------------ÁRVORES-----------------------------------------
        if((args[1].equals("-T")||args[1].equals("-TM"))&&(args.length >= 2))
        {
             Aig myAigTree      = new Aig(args[0]);
             TreesBasic myTrees = new TreesBasic(myAigTree);
             if(args[1].equals("-TM"))
                     myTrees.getEqnTrees();
             myTrees.getEqn();
             if((args.length > 2)&&(args[2].contains(".eqn"))) 
             { //caso arquivo de log
               Logs.LogsWriteEqn(myTrees.getEqn(),args[2]);  
             }
             return;
        }   
        //------------------------------------------------------------------
        //--------------------KCUTS---------------------------------------------
        if(args[1].equals("-K")&&(args.length >= 3)){
          Aig myAig = new Aig(args[0]);
          //Algoritmo de kcuts 
          int sizeCut = Integer.valueOf(args[2]);
          if(args.length > 3){
            if(!args[3].contains(".txt")){
                NodeAig nodeSelect = myAig.getVertexName(args[3]);
                CutterKCuts Kcuts  = new CutterKCuts(myAig, nodeSelect, sizeCut);
                Kcuts.showAllcuts(nodeSelect);
                if((args.length > 4)&&(args[4].contains(".txt")))
                    Logs.LogsAigKcuts(args[4],Kcuts,nodeSelect);
                return;
            }
            else{
                CutterKCuts Kcuts  = new CutterKCuts(myAig, sizeCut); 
                Kcuts.showAllcuts();
                if(args[3].contains(".txt"))
                    Logs.LogsAigKcuts(args[3],Kcuts);
                return;                
            }
          }
          if((args.length == 3)){
               CutterKCuts Kcuts  = new CutterKCuts(myAig, sizeCut); 
               Kcuts.showAllcuts();
               return;
          }
        }
        //----------------------------------------------------------------------        
        //--------------------KCUTS C/LIMITE TREENODES--------------------------
        if(args[1].equals("-KT")&&(args.length >= 3))
        {
          Aig myAig = new Aig(args[0]);
          //Algoritmo de kcuts 
          int sizeCut = Integer.valueOf(args[2]);
          if(args.length > 3){
            if(!args[3].contains(".txt")){
                NodeAig nodeSelect = myAig.getVertexName(args[3]);
                CutterKCutsTreeNodes KcutsTN  = new CutterKCutsTreeNodes(myAig, nodeSelect, sizeCut);
                KcutsTN.showAllcuts(nodeSelect);
                if((args.length > 4)&&(args[4].contains(".txt")))
                    Logs.LogsAigKcuts(args[4],KcutsTN,nodeSelect);
                return;
            }
            else{
                CutterKCutsTreeNodes KcutsTN  = new CutterKCutsTreeNodes(myAig, sizeCut); 
                KcutsTN.showAllcuts();
                if(args[3].contains(".txt"))
                    Logs.LogsAigKcuts(args[3],KcutsTN);
                return;                
            }
          }
          if((args.length == 3)){
               CutterKCutsTreeNodes KcutsTN  = new CutterKCutsTreeNodes(myAig, sizeCut); 
               KcutsTN.showAllcuts();
               return;
          }
        }
        //--------------------------------------------------------------------        
        //--------------------KCUTS C/BIBLIOTECA--------------------------
        if(args[1].equals("-KB")&&(args.length >= 4))
        {
          Aig myAig = new Aig(args[0]);
          //Algoritmo de kcuts 
          int sizeCut = Integer.valueOf(args[2]);
          if(!args[3].contains(".genlib"))
          {
              System.out.print("BIBLIOTECA EM FORMATO INVALIDO");
              System.exit(-1);
          }
          String library =  args[3];
          if(args.length > 4){
            if(!args[4].contains(".txt")){
                NodeAig nodeSelect = myAig.getVertexName(args[4]);
                CutterKCutsLibrary KcutsLibrary  = new CutterKCutsLibrary(myAig, nodeSelect, sizeCut,library);
                KcutsLibrary.showAllcutsLibrary(nodeSelect);
                if((args.length > 5)&&(args[5].contains(".txt")))
                    Logs.LogsAigKcuts(args[5],KcutsLibrary, nodeSelect);
                return;
            }
            else{
                CutterKCutsLibrary KcutsLibrary = new CutterKCutsLibrary(myAig, sizeCut, library); 
                KcutsLibrary.showAllcutsLibrary();
                if(args[4].contains(".txt"))
                    Logs.LogsAigKcuts(args[4],KcutsLibrary);
                return;                
            }
          }
          if((args.length == 4)){
               CutterKCutsLibrary KcutsLibrary = new CutterKCutsLibrary(myAig, sizeCut, library); 
               KcutsLibrary.showAllcutsLibrary();
               return;
          }
        }
        //----------------------------------------------------------------------
        //--------------------MAP AREA FLOW-------------------------------------
        if(args[1].equals("-A")&&(args.length >= 4))
        {
             Aig myAig              = new Aig(args[0]);
             int sizeCut            = Integer.valueOf(args[2]);
             CutterKCuts    kcuts   = new CutterKCuts(myAig, sizeCut);
             int areaCut            = Integer.parseInt(args[3]);
             CostAreaFlow   function= new CostAreaFlow(0,0,0,1,1,areaCut);
             AreaFlow area              = new AreaFlow(myAig,sizeCut,kcuts,function);
             area.showCovering();
             if((args.length > 4)&&(args[4].contains(".eqn"))) 
             { //caso arquivo de log
               Logs.LogsWriteEqn(area.getEqn(),args[4]);  
             }
             return;
        }
        //----------------------------------------------------------------------        
        //--------------------MAP AREA FLOW SIMULATED ANNELING------------------
        if(args[1].equals("-ASA")&&(args.length >= 4))
        {
             Aig myAig                          = new Aig(args[0]);
             int sizeCut                        = Integer.valueOf(args[2]);
             CutterKCuts    kcuts               = new CutterKCuts(myAig, sizeCut);
             CostAreaFlow   function            = new CostAreaFlow(0,0,0,1,1,1);
             AreaFlow area                      = new AreaFlow(myAig,sizeCut,kcuts,function);
             CoveringAreaFlow initialCovering   = area.getCovering();
             area.showCovering();
             SimulatedAnneling SA               = new SimulatedAnneling(myAig,kcuts,area.getBestCut(),initialCovering,function,1000,100);
             if((args.length > 4)&&(args[4].contains(".eqn"))) 
             { //caso arquivo de log
               Logs.LogsWriteEqn(area.getEqn(),args[4]);  
             }
             return;
        }
        //----------------------------------------------------------------------
        ////--------------------MAP AREA FLOW LIBRARY---------------------------
        //if(args[1].equals("-AL")&&(args.length >= 4))
        //{
             //Aig myAig = new Aig(args[0]);
             ////algoritmo de areaflow
             //int sizeCut = Integer.valueOf(args[2]);
             //String out  = AreaFlowLibrary.mapAreaFlow(myAig, sizeCut,args[3]);
             //if((args.length > 4)&&(args[4].contains(".eqn"))) 
             //{ //caso arquivo de log
               //Logs.LogsAigEqn(args[4],out);  
             //}
             //return;
        //}
        ////--------------------------------------------------------------------
        //--------------------ELIS----------------------------------------------
        if(args[1].equals("-E")&&(args.length >= 4))
        {
             Aig myAigTree = new Aig(args[0]);
             int s = Integer.parseInt(args[2]);
             int p = Integer.parseInt(args[3]);
             TreesElis myTrees  = new TreesElis(myAigTree);
             Elis elis          = new Elis(myTrees,s,p);
             elis.getTrees().getEqn();
             if((args.length > 4)&&(args[4].contains(".eqn"))) 
             { //caso arquivo de log
               Logs.LogsWriteEqn(elis.getTrees().getEqn(),args[4]);  
             }
             return;
        }   
        //----------------------------------------------------------------------
        ////--------------------SWITCHING---------------------------------------
        //if(args[1].equals("-S")&&(args.length >= 3))
        //{
                //Simulator simulator = new Simulator(args[0], args[2]);
                //simulator.run();
        //}
        ////--------------------------------------------------------------------
       //--------------------DFS------------------------------------------------
        if(args[1].equals("-DFS")&&(args.length >= 2))
        {
              Aig myAig = new Aig(args[0]);
              dfsNodeAigVisitorBasic myDfs = new dfsNodeAigVisitorBasic();
              if(args.length == 3)
              {
                  NodeAig out= (NodeAig)myAig.getVertexName(args[2]);
                  out.accept(myDfs);
              }
              else
              {
                   for(NodeAig out : myAig.getNodeOutputsAig())
                    out.accept(myDfs);                   
              }
              System.out.println("DFS executada com sucesso");
              System.out.println("\n############ DFS #############################");
              System.out.println(myDfs.getStates()+": Estados visitados;");
              for(NodeAig element: myDfs.getNodesDfs())
              {
                  if(element.isOutput())
                      System.out.print("\n");
                  System.out.print(element.getName()+"-");                 
              }
              System.out.println("\n#########################################");   
              return;
        }
        //--------------------BFS-----------------------------------------------
        if(args[1].equals("-BFS")&&(args.length >= 2))
        {
              bfsNodeAigVisitorBasic myBfs = new bfsNodeAigVisitorBasic();
              Aig myAig = new Aig(args[0]);
              if(args.length == 3)
              {
                  NodeAig out = (NodeAig)myAig.getVertexName(args[2]);
                  out.accept(myBfs);
              }
              else
              {
                  for(NodeAig out : myAig.getNodeOutputsAig())
                    out.accept(myBfs);
              }
              System.out.println("BFS executada com sucesso");
              System.out.println("\n############ BFS #############################");
              System.out.println(myBfs.getStates()+": Estados visitados;");
              for(NodeAig element: myBfs.getNodesBfs())
              {
                  if(element.isOutput())
                      System.out.print("\n");
                  System.out.print(element.getName()+"-");                 
              }
              System.out.println("\n#########################################"); 
              return;
         }
         //--------------------EQN DESCRIÇÃO------------------------------------
         if(args[1].equals("-EQN")&&(args.length >= 2))
         {
              Aig myAig = new Aig(args[0]);
              String myEqn = Logs.AigtoEqn(myAig);
              System.out.println(myEqn);
              if(args.length > 2)
                  Logs.LogsWriteEqn(myEqn, args[2]);
              return;
         }
      }
      warning(); //caso de nenhuma opção erro
      return;
      //------------------------------------------------------------------------
    }
        
    /** Informa o procedimento correto para executar a ferramenta*/
    private static void warning() 
    {
      System.out.println("############################# FLEXMAP ########################################");
      System.out.println("-- Parametros Invalidos. O programa deve ser inicializado da seguinte maneira:");
      System.out.println("--ÁRVORES--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -T [arquivoSaida.txt(opcional)]");
      System.out.println("--ÁRVORES e SUBÁRVORES--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -TM [arquivoSaida.txt(opcional)]");
      System.out.println("--KCUTS--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -K TamanhoDoCorte [Nodo(opcional)] [arquivoSaida.txt(opcional)]");
      System.out.println("--KCUTS C/LIMITE DE TREENODES--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -KT TamanhoDoCorte [Nodo(opcional)] [arquivoSaida.txt(opcional)]");
      System.out.println("--KCUTS C/BIBLIOTECA--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -KB TamanhoDoCorte Biblioteca.genlib [Nodo(opcional)] [arquivoSaida.txt(opcional)]");
      System.out.println("--MAPEAMENTO COM AREAFLOW--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -A TamanhoDoCorte Custo-Cut [arquivoSaida.eqn (opcional)]");
      System.out.println("--MAPEAMENTO COM AREAFLOW COM BIBLIOTECA--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -AL TamanhoDoCorte arquivoBiblioteca [arquivoSaida.eqn (opcional)]");
      System.out.println("--MAPEAMENTO COM AREAFLOW E SIMULATED ANNELING--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -ASA TamanhoDoCorte [arquivoSaida.eqn (opcional)]");
      System.out.println("--MAPEAMENTO COM ELIS--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -E s p [arquivoSaida.eqn (opcional)]");
      System.out.println("--SWITCHING--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -S [arquivoSaida (opcional)]");
      System.out.println("--BFS--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -BFS [Nodo (opcional)]");
      System.out.println("--DFS--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -DFS [Nodo (opcional)]");
      System.out.println("--EQN--");
      System.out.println("--    ~$ java -jar FlexMap.jar arquivoEntrada.aag -EQN [arquivoSaida.eqn(opcional)]");
      System.out.println("-------------------------------------------------------");
    }
}
