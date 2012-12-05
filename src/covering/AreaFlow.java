package covering;

import aig.*;
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
    protected Float                 custCut;
    protected Map<NodeAig, AigCut>  covering;
    protected Map<NodeAig, AigCut>  bestCut;
    protected Map<NodeAig,Float>    tableArea;
    protected Map<NodeAig,Integer>  levelNode;

    public AreaFlow(Aig myAig, int size, CutterK cutterK, Float cust) 
    {
        this.myAig      = myAig;
        this.sizeCut    = size;
        this.kcuts      = cutterK;
        this.custCut    = cust;
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
        Map<AigCut,Float> tableCust = new HashMap<AigCut,Float>();
        Set<AigCut> cuts            = kcuts.getCuts().get(nodeActual);
        Iterator<AigCut> iterator   = cuts.iterator();
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
              float cust  = sumCust(cut,nodeActual);
              tableCust.put(cut, cust);              
           }
           
        }while(iterator.hasNext()); //contabiliza areas
        if(!nodeActual.isInput())
            choiceBestArea(nodeActual,tableCust);
        System.out.print(" BestArea Nodo: "+nodeActual.getName()+
         " Custo: "+tableArea.get(nodeActual)+
         " Profundidade: "+levelNode.get(nodeActual)+
         " Corte:");
        bestCut.get(nodeActual).showCut();
    }
    //**Método contabiliza a área do Cut
    private float sumCust(AigCut cut, NodeAig nodeActual) 
    {
        float cust=0;
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
            cust+=tableArea.get(node);
          }
          return (this.custCut+cust)/outEdge; 
        }
        for(NodeAig node:cut)
        {
            if(!tableArea.containsKey(node))
                getBestArea(node);
            cust+=tableArea.get(node);
        }
        return (cust+this.custCut)/outEdge;
    }
    //**Método faz a melhor escolha entre os cortes do Nodo
    private void choiceBestArea(NodeAig nodeActual, Map<AigCut, Float> tableCust) 
    {
        AigCut cut=null,cutBest = null;
        Set<AigCut> cuts = kcuts.getCuts().get(nodeActual); 
        Iterator<AigCut> iterator = cuts.iterator();
        do
        {    
            if(cut==null)
            {
                cut = iterator.next();
                cutBest  = cut;
            }
            else
                cut = iterator.next();
            if(((tableCust.get(cut)) <= (tableCust.get(cutBest))))
              if(cut.size() >= cutBest.size())
                if(sumLevel(cut,nodeActual) >= sumLevel(cutBest,nodeActual)) //compara a profundidade em relação ao circuito
                  cutBest = cut; 
                
        }while(iterator.hasNext());
        bestCut.put(nodeActual,cutBest);      
        tableArea.put(nodeActual, (tableCust.get(cutBest)));
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
            this.covering.put(nodeActual, this.bestCut.get(nodeActual));
            nodeActual.accept(bfs);     
        }
        Map<NodeAig,AigCut> list = new HashMap<NodeAig, AigCut>();
        for(Map.Entry<NodeAig,AigCut> element: this.covering.entrySet())
           for(NodeAig node: element.getValue().getCut())
                if((!node.isInput())&&(!this.covering.containsKey(node)))
                    list.put(node, this.bestCut.get(node));
        this.covering.putAll(list);
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
}
