package kcutter;

import aig.*;
import brc.*;
import functionsClasses.*;
import library.*;
import java.util.*; 

/**
 * Classe que aplica os K-cuts com Matching em Biblioteca de Células ".genlib"
 * @author Julio Saraçol
 */
public final class CutterKCutsLibrary extends CutterKCuts
{

    protected LibraryReader library;
    protected Map<NodeAig,Set<AigCutBrc>>    cutsBrc;
    protected Map<AigCutBrc,Set<FunctionData>> signature;
    public String logs;
    
    public CutterKCutsLibrary(Aig aig, int limit, String libraryName) throws Exception
    {
        super(aig, limit);
        this.library    = new LibraryReader(libraryName, limit); 
        if(library.equals(null))
        {
            System.out.println("Impossivel carregar biblioteca.");
            System.exit(-1);
        }            
        System.out.println("Biblioteca carregada com sucesso");
        this.signature  = new HashMap<AigCutBrc, Set<FunctionData>>();
        for(NodeAig node : aig.getAllNodesAig())
        {
            computeAllCuts(node);
        }
    }
    
    public CutterKCutsLibrary(Aig aig,NodeAig nodeCurrent, int limit, String libraryName) throws Exception
    {
        super(aig, limit);
        this.library    = new LibraryReader(libraryName,limit); 
        if(library.equals(null))
        {
            System.out.println("Impossivel carregar biblioteca.");
            System.exit(-1);
        }
        System.out.println("##Biblioteca carregada com sucesso");
        this.signature  = new HashMap<AigCutBrc, Set<FunctionData>>();
        if(nodeCurrent == null){
            System.out.println("NODO NÃO EXISTE NO CIRCUITO");
            System.exit(-1);
        }
        this.computeAllCuts(nodeCurrent);
    }

    @Override
    protected void computeAllCuts(NodeAig node) 
    {
        computeKcuts(node);
        createBrc();
        Matching(node);
    }

    private void createBrc() {
        this.cutsBrc  = new HashMap<NodeAig, Set<AigCutBrc>>();
        for(NodeAig node : aig.getAllNodesAig())
        {
            Set<AigCutBrc> setBrcs  = new HashSet<AigCutBrc>();
            Set<AigCut> elements    = cuts.get(node);
            for(Set<NodeAig> nodesCut : elements)
            {
                AigCutBrc cutBrc  = new AigCutBrc(this.limit);
                cutBrc.addCuts(nodesCut);
                setBrcs.add(cutBrc);
            }
            this.cutsBrc.put(node,setBrcs);
        }
    }
    
    private void Matching(NodeAig nodeCurrent) {
            System.out.println("Buscando Matching com nodo: "+nodeCurrent.getName());
            Set<AigCutBrc> allCuts = this.cutsBrc.get(nodeCurrent);
            for(AigCutBrc cut: allCuts)
            {
               cut.showCut(nodeCurrent);
               BitRepresentation brcBit      = BitHandler.brcToBitRepresentation(cut.getBrc().getBRC());              
               BitRepresentation notBrcBit   = BitHandler.brcToBitRepresentation(BRCHandler.not(cut.getBrc()).getBRC());              
               BitRepresentation signP       = LowestFunctionFinder.run_P(brcBit, limit);
               BitRepresentation notSignP    = LowestFunctionFinder.run_P(notBrcBit, limit);
               this.checkingSignP(cut, signP);
               System.out.print("Inversão de ");
               this.checkingSignP(cut, notSignP);
               System.out.print("-----------------\n");
            }
    }
    
    protected void checkingSignP(AigCutBrc cut, BitRepresentation sign)
    {
         System.out.print("assinatura ->"+ sign.toHexaString()+" ");                         
         try{
              FunctionData dataFunc = library.getLib().getBySign(sign.toHexaString()); //consulta custo na biblioteca
              System.out.println("Matching com a biblioteca célula: "+dataFunc.getGateName()+" função: "+dataFunc.getFunction()+" custo: "+dataFunc.getCost());
              if(this.signature.containsKey(cut))
                  this.signature.get(cut).add(dataFunc);
              else
              {
                  Set<FunctionData> sig = new HashSet<FunctionData>();
                  sig.add(dataFunc);
                  this.signature.put(cut,sig);
              }
         }catch(Exception e)
         {
            System.out.println("%%%%SEM MATCHING: erro ao pesquisar função lógica na biblioteca ou assinatura%%%%");
         }
    }
    
    public void showAllcutsLibrary()
    {
       System.out.println("############CUTS LIBRARY#######################");
       logs +=("############CUTS LIBRARY#######################");
       Iterator<Map.Entry<NodeAig,Set<AigCutBrc>>> node = cutsBrc.entrySet().iterator();
       while(node.hasNext())
       {
           Map.Entry<NodeAig,Set<AigCutBrc>> cut = node.next();
           System.out.println("Node:"+cut.getKey().getName());
           logs += "\nNode:"+cut.getKey().getName();
           for(AigCutBrc singleCut: cut.getValue())
           {
             singleCut.showCut(cut.getKey());
             if(this.signature.containsKey(singleCut))
             {
                for(FunctionData cell :this.signature.get(singleCut))
                {
                    logs+=("------------\nMatching com a biblioteca célula: "+cell.getGateName()+" função: "
                    +cell.getFunction()+" custo: "+cell.getCost()+"\n------------");                    
                    System.out.println("------------\nMatching com a biblioteca célula: "+cell.getGateName()+" função: "
                    +cell.getFunction()+" custo: "+cell.getCost()+"\n------------");
                }
             }
           }   
       }
       logs += (cuts.size()+" Cortes\n");
       System.out.println(cuts.size()+" Cortes");
       logs += ("########################################\n");
       System.out.println("########################################");
    }   
    
    public void showAllcutsLibrary(NodeAig node)
    {
       System.out.println("############CUTS LIBRARY#######################");
       System.out.println("Node:"+node.getName());
       for(AigCutBrc singleCut: this.cutsBrc.get(node))
       {
         singleCut.showCut(node);             
         if(this.signature.containsKey(singleCut))
         {
             for(FunctionData cell :this.signature.get(singleCut))
                System.out.println("------------\nMatching com a biblioteca célula: "+cell.getGateName()+" função: "
                +cell.getFunction()+" custo: "+cell.getCost()+"\n------------");
         }
       }   
       
       System.out.println(this.cuts.get(node).size()+" Cortes");
       System.out.println("########################################");
    }
    
    public Map<NodeAig, Set<AigCutBrc>> getCutsBrc() {
        return cutsBrc;
    }
}
