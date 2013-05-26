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
public class CutterKCutsLibrary extends CutterKCuts
{

    protected LibraryReader library;
    protected Map<NodeAig,Set<AigCutBrc>>    cutsBrc;
    /**Atributo que possui os cortes que foram encontrados Matchings*/
    protected Map<AigCutBrc,Set<FunctionData>> signature;
    protected Map<Set<FunctionData>,String>    signatureToHexa;
    protected Map<NodeAig,Set<AigCutBrc>>      cutsBrcMatchings;
    public String logs="";
    
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
        this.signature          = new HashMap<AigCutBrc, Set<FunctionData>>();
        this.signatureToHexa    = new HashMap<Set<FunctionData>, String>();
        this.cutsBrcMatchings   = new HashMap<NodeAig, Set<AigCutBrc>>();
        for(NodeAig node : aig.getAllNodesAig())
        {
            computeAllCuts(node);
        }
        System.out.println("KcutsLibrary efetuado com sucesso!");
    }
    
    public CutterKCutsLibrary(Aig aig,NodeAig nodeCurrent, int limit, String libraryName) throws Exception
    {
        super(aig, limit);
        this.library    = new LibraryReader(libraryName,limit); 
        if(library == null)
        {
            System.out.println("Impossivel carregar biblioteca.");
            System.exit(-1);
        }
        System.out.println("##Biblioteca carregada com sucesso");
        this.signature         = new HashMap<AigCutBrc, Set<FunctionData>>();
        this.cutsBrcMatchings  =  new HashMap<NodeAig, Set<AigCutBrc>>();
        if(nodeCurrent == null){
            System.out.println("NODO NÃO EXISTE NO CIRCUITO");
            System.exit(-1);
        }
        this.computeAllCuts(nodeCurrent);
        System.out.println("KcutsLibrary efetuado com sucesso!");
    }

    @Override
    protected void computeAllCuts(NodeAig node) throws CloneNotSupportedException 
    {
        computeKcuts(node);
        createBrc();
        Matching(node);
    }
    /**Método que inicializa os AigCutBrc de todos os cortes dos nodos do AIG*/
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
                cutBrc.getBrc(node);
                setBrcs.add(cutBrc);
            }
            this.cutsBrc.put(node,setBrcs);
        }
    }
    
    private void Matching(NodeAig nodeCurrent) {
            //System.out.println("Buscando Matching com nodo: "+nodeCurrent.getName());
            Set<AigCutBrc> allCuts = this.cutsBrc.get(nodeCurrent);
            for(AigCutBrc cut: allCuts)
            {
               //cut.showCut(nodeCurrent);
               //BRCHandler.displayBinary(cut.getBrc(nodeCurrent));
               
               BitRepresentation bitRep      = BitHandler.brcToBitRepresentation(cut.getBrc(nodeCurrent), this.limit);
               BitRepresentation signP       = LowestFunctionFinder.run_P(bitRep, limit);
               //cadastra a assinaturaP em HEXA do corte
               cut.setSignaturePHexa(signP.toHexaString());
               this.checkingSignP(cut, signP,nodeCurrent);
               //System.out.print("assinatura HEXA->"+ signP.toHexaString()+" "); 
               
               // Clona e Nega o BRC
               BRC cloneBRC = cut.getBrc(nodeCurrent).clone();
               cloneBRC = BRCHandler.not(cloneBRC);
               BitRepresentation notBitRep   = BitHandler.brcToBitRepresentation(cloneBRC, this.limit);
               BitRepresentation notSignP    = LowestFunctionFinder.run_P(notBitRep, limit);
               //System.out.print("Inversão de ");
               //System.out.print("assinatura HEXA->"+ notSignP.toHexaString()+" "); 
               this.checkingSignP(cut, notSignP,nodeCurrent);
               //System.out.print("-----------------\n");
            }
    }
    
    protected void checkingSignP(AigCutBrc cut, BitRepresentation sign,NodeAig nodeCurrent)
    {
        System.out.print("Nodo: "+nodeCurrent.getName()+"assinatura HEXA->"+ sign.toHexaString()+" ");                         
         try{
              FunctionData dataFunc = library.getLib().getBySign(sign.toHexaString()); //consulta custo na biblioteca
              System.out.println("######Matching com a biblioteca célula: "+dataFunc.getGateName()+" função: "+dataFunc.getFunction()+" custo: "+dataFunc.getCost());
              if(this.signature.containsKey(cut))
                  this.signature.get(cut).add(dataFunc);
              else{
                  Set<FunctionData> sig = new HashSet<FunctionData>();
                  sig.add(dataFunc);
                  this.signature.put(cut,sig);
              }
              if(!this.cutsBrcMatchings.containsKey(nodeCurrent)){
                 Set<AigCutBrc> cuts = new HashSet<AigCutBrc>();
                 cuts.add(cut);
                 this.cutsBrcMatchings.put(nodeCurrent,cuts);
              }
              else
                 this.cutsBrcMatchings.get(nodeCurrent).add(cut);                     
         }catch(Exception e)
         {
            System.out.println("%%%%SEM MATCHING: erro ao pesquisar função lógica na biblioteca ou assinatura%%%%");
         }
    }
    
    public void showAllcutsLibrary()
    {
       System.out.println("############CUTS LIBRARY#######################");
       logs +=("\n############CUTS LIBRARY#######################");
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
                logs+="\n[";
                for(NodeAig nodeCut: singleCut.getCut())
                    logs+=nodeCut.getName()+",";
                logs = logs.substring(0,logs.length()-1)+"] ";                         
                for(FunctionData cell :this.signature.get(singleCut))
                {
                    logs+=("------------ Matching com a biblioteca célula: "+cell.getGateName()+" função: "
                    +cell.getFunction()+" custo: "+cell.getCost());                    
                    System.out.println("------------\nMatching com a biblioteca célula: "+cell.getGateName()+" função: "
                    +cell.getFunction()+" custo: "+cell.getCost()+"\n------------");
                }
             }
           }   
       }
       logs += ("\n"+signature.size()+" Cortes com Matching\n");
       System.out.println(signature.size()+" Cortes com Matching");
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

    /** Método de acesso aos Kcuts que estão com matching na biblioteca*/ 
    public Map<NodeAig, Set<AigCutBrc>> getCutsBrc() {
        return Collections.unmodifiableMap(cutsBrcMatchings);
    }
    
    /** Método de acesso ao conjunto cort/matchings da biblioteca*/ 
    public Map<AigCutBrc, Set<FunctionData>> getMatchings() {
        return Collections.unmodifiableMap(signature);
    }

    /** Método de acesso a biblioteca em que os Kcuts foram aplicados os matchings*/ 
    public LibraryReader getLibrary() {
        return library;
    }

    public void setLibrary(LibraryReader library) {
        this.library = library;
    }
    
    
    
    
    
    
}
