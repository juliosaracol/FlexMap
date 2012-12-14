package covering;

import aig.*;
import io.*;
import java.io.FileNotFoundException;
import kcutter.*;

import java.util.*;
/**
 *@author Julio Saraçol
 * Classe que aplica a cobertura de acordo com  algoritmo de AreaFlow (Valavan,2006)
 */
public class AreaFlow 
{
    protected Aig                   myAig;
    protected Integer               sizeCut;
    protected CutterK               kcuts;
    protected Float                 CostCut;
    protected Map<NodeAig, AigCut>  covering;
    protected Map<NodeAig, AigCut>  bestCut;
    protected Map<NodeAig,Float>    tableArea;
    protected Map<NodeAig,Integer>  levelNode;

    public AreaFlow(Aig myAig, int size, CutterK cutterK, Float Cost) 
    {
        this.myAig      = myAig;
        this.sizeCut    = size;
        this.kcuts      = cutterK;
        this.CostCut    = Cost;
        this.covering   = new HashMap<NodeAig, AigCut>();
        this.bestCut    = new HashMap<NodeAig, AigCut>();
        this.tableArea  = new HashMap<NodeAig, Float>();
        this.levelNode  = new HashMap<NodeAig, Integer>();
        mapAreaFlow();
        covering();
    }
    //** Método que aplica a cobertura baseado no AreaFlow
    private void mapAreaFlow() 
    {
        dfsNodeAigVisitorGetLevel dfs = new dfsNodeAigVisitorGetLevel(this);
        for(NodeAig node: myAig.getNodeInputsAig())
            this.levelNode.put(node,1);
        for(NodeAig node: myAig.getNodeOutputsAig())
        {
           if((node.isOutput())&&(node.getParents().isEmpty())) //constant
               this.levelNode.put(node,1);
           else
               node.accept(dfs);
        }
        for(NodeAig node: myAig.getAllNodesAig())
          getBestArea(node);
    }
    //**Método que dado um Nodo gera os cortes, calcula áreas e seleciona melhor área
    protected  void getBestArea(NodeAig nodeActual)
    {
        if(this.bestCut.containsKey(nodeActual))
          return;
        Map<AigCut,Float> tableCost     = new HashMap<AigCut, Float>();
        Set<AigCut> cuts                = kcuts.getCuts().get(nodeActual);
        Iterator<AigCut> iterator       = cuts.iterator();
        do
        {          
           AigCut cut = iterator.next(); 
           if(nodeActual.isInput())
           {
              bestCut.put(nodeActual, cut);
              tableArea.put(nodeActual,(float)0);              
              break;
           }
           else
           {
              float cost  = sumCost(cut,nodeActual);
              tableCost.put(cut, (Float)cost);              
           }
           
        }while(iterator.hasNext()); //contabiliza areas
        if(!nodeActual.isInput())
            choiceBestArea(nodeActual,tableCost);
        System.out.print(" BestArea Nodo: "+nodeActual.getName()+
         " Custo: "+tableArea.get(nodeActual)+
         " Profundidade: "+levelNode.get(nodeActual)+
         " Corte:");
        bestCut.get(nodeActual).showCut();
    }
    //**Método contabiliza a área do Cut
    private float sumCost(AigCut cut, NodeAig nodeActual) 
    {
        float Cost=0;
        int outEdge=0;
        if(nodeActual.getChildren().isEmpty())
            outEdge =1;//fanouts
        else
            outEdge = nodeActual.getChildren().size(); //fanouts
        if(cut.size()==1) //corte unitário sempre 1+entradas
        {
          for(NodeAig node: nodeActual.getParents())
          {
            if(!tableArea.containsKey(node))
                getBestArea(node);
            Cost+=tableArea.get(node);
          }
          return (this.CostCut+Cost)/outEdge; 
        }
        for(NodeAig node:cut)
        {
            if(!tableArea.containsKey(node))
                getBestArea(node);
            Cost+=tableArea.get(node);
        }
        return (Cost+this.CostCut)/outEdge;
    }
    //**Método faz a melhor escolha entre os cortes do Nodo
    private void choiceBestArea(NodeAig nodeActual, Map<AigCut, Float> tableCost) 
    {
        AigCut cut      = null;
        AigCut cutBest  = bestCost(tableCost);
        Set<AigCut> cuts = kcuts.getCuts().get(nodeActual); 
        Iterator<AigCut> iterator = cuts.iterator();
        do
        {    
            cut = iterator.next();
            System.out.println("trabalhando com o nodo :"+nodeActual.getName()+" e corte :");
            cut.showCut();
            if(((tableCost.get(cut)) <= (tableCost.get(cutBest))))
              if(cut.size() >= cutBest.size())
                if(sumLevel(cut,nodeActual) >= sumLevel(cutBest,nodeActual)) //compara a profundidade em relação ao circuito
                  cutBest = cut; 
                
        }while(iterator.hasNext());
        bestCut.put(nodeActual,cutBest);      
        tableArea.put(nodeActual, (tableCost.get(cutBest)));
    }
    //**Método contabiliza a profundidade utilizando bfs
    private Integer sumLevel(AigCut cut, NodeAig nodeActual)
    {
        if((nodeActual.isOutput())&&(nodeActual.getParents().isEmpty())) //constant
            return 1;
        bfsNodeAigVisitorSumLevel bfs = new bfsNodeAigVisitorSumLevel(levelNode, cut);
        nodeActual.accept(bfs);
        return bfs.getLevel();
    }
    //**Método que aplica a cobertura baseado em bfs utilizando as areas calculadas
    protected void covering()
    {
        bfsNodeAigVisitorAreaCovering bfs = new bfsNodeAigVisitorAreaCovering(this);
        for(NodeAig nodeActual: myAig.getNodeOutputsAig())
        {
            System.out.println("saida: "+nodeActual.getName());
            if(!this.covering.containsKey(nodeActual))
            {
                this.covering.put(nodeActual, this.bestCut.get(nodeActual));
                nodeActual.accept(bfs);     
            }
        }
        boolean signalOk = true;
        while(signalOk == true)
        {
          Map<NodeAig,AigCut> list = new HashMap<NodeAig, AigCut>();
          signalOk = false;  
          for(Map.Entry<NodeAig,AigCut> element: this.covering.entrySet())
              for(NodeAig node: element.getValue().getCut())
                if((!node.isInput())&&(!this.covering.containsKey(node)))
                {
                    list.put(node, this.bestCut.get(node));
                    signalOk = true;
                }
          this.covering.putAll(list);
        }
    }
    //**Método para visualização da cobetura final
    public void showCovering()
    {
        Iterator<Map.Entry<NodeAig,AigCut>> iterator =  this.covering.entrySet().iterator();
        System.out.println("################# AREA FLOW ################################");
        do
        {
            Map.Entry<NodeAig,AigCut> element = iterator.next();
            System.out.print("Covering Nodo: "+element.getKey().getName()+
                           " Custo: "+tableArea.get(element.getKey())+
                           " Profundidade: "+levelNode.get(element.getKey())+
                           " Corte:");
            element.getValue().showCut();
        }while(iterator.hasNext());
        System.out.println("############################################################");
    }
    
    //** Método de acesso a cobetura gerada
    public Map<NodeAig, AigCut> getCovering() {
        return covering;
    }

    //**Método que seleciona o menor custo possivel melhor Cut*/
    private AigCut bestCost(Map<AigCut, Float> tableCost) 
    {
        AigCut best = null;
        for(Map.Entry<AigCut,Float> nodes: tableCost.entrySet())
        {
            if(best == null)
                best = nodes.getKey();
            else
                if(tableCost.get(best) > nodes.getValue())
                    best = nodes.getKey();
        }
        return best;            
    }
    
    public String getEqn() throws FileNotFoundException
    {
       String eqn = Logs.coveringToEqn(myAig, getCovering());
       return eqn;
    }
}
