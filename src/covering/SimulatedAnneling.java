package covering;

import FlexMap.*;
import aig.*;
import io.LogsCoveringToEqn;
import java.io.FileNotFoundException;
import kcutter.*;
import java.util.*;

/**
 * Classe aplica SimulatedAnneling 
 * @author Matheus Nachtigall
 */

/*
 * temperature = 5000.0;
 * cooling_rate = 0.9999 //Taxa de decaimento da temperatura
 * epsilon = 0.001;  //Condição de parada do algoritmo
 *
 * best = Hash.new
 * best[:route] = randomSolution
 * best[:cost] = best[:route].cost
 * 
 * current = best
 * while temperature > epsilon
 *  candidate = current[:route].generateNeighbor
 *  delta = current[:cost] - candidate[:cost]
 *  if delta > 0                                        //Se o candidato é melhor, troca automaticamente
 *    if best[:cost] > candidate[:cost]
 *      best = candidate
 *    end
 *    current = candidate
 *  else                                                //Caso contrario, troca com prob
 *    if (Math.exp(delta/temperature) > rand()) #Simulated annealing  //Rand: Valor entre 0 e 1
 *      current = candidate
 *    end
 *  end
 * 
 *  temperature *= cooling_rate
 * end
 
 */

public class SimulatedAnneling
{
    protected float temp;
    protected Aig myAig;
    protected CutterK   kcuts;
    protected CostFunction function;
    protected Map<NodeAig,Integer> levelNode;
    protected float coolingRate;
    // protected Map<NodeAig,Float> currentCostTable
    protected Map<NodeAig,AigCut> bestCut;
    protected Map<NodeAig,AigCut> bestCoverCut;
    protected Map<NodeAig,Float> bestCostTable;
    
    protected Map<NodeAig,AigCut> currentCut;
    protected Map<NodeAig,AigCut> currentCoverCut;
    protected Map<NodeAig,Float> currentCostTable;

    
    public SimulatedAnneling(Aig myAig, CutterK kcuts, CostFunction function, int tempInitial, float coolingRate)
    {
        this.myAig              = myAig;  
        this.kcuts              = kcuts;
        this.function           = function;
        this.levelNode          = new HashMap<NodeAig, Integer>();
        this.bestCut            = new HashMap<NodeAig, AigCut>();
        this.bestCoverCut       = new HashMap<NodeAig, AigCut>();
        this.bestCostTable      = new HashMap<NodeAig, Float>();
                
        this.temp               = tempInitial;
        this.coolingRate        = coolingRate;
        this.currentCut         = new HashMap<NodeAig, AigCut>();
        currentCoverCut         = new HashMap<NodeAig, AigCut>();
        currentCostTable        = new HashMap<NodeAig, Float>();

        //===========================================
        randomInitialSolution();
        //===========================================
        //showCovering(bestCoverCut);
        
        // System.out.println("#Solução Inicial com custo:"+currentCover.getCost(function));

         // //**Profundidade de todos os nodos do grafo****************************
         // levelNode = new HashMap<NodeAig, Integer>();
         // dfsAigVisitorAreaGetLevel dfs = new dfsAigVisitorAreaGetLevel(this.levelNode);
         // for(NodeAig node: myAig.getNodeInputsAig())
         //    this.levelNode.put(node,1);
         // for(NodeAig node: myAig.getNodeOutputsAig())
         // {
         //   if((node.isOutput())&&(node.getParents().isEmpty())) //constant
         //       this.levelNode.put(node,1);
         //   else
         //       node.accept(dfs);
         // }
         // //*********************************************************************
         
        run();
        System.out.println("Custo Aig:"+getCostCovering(bestCoverCut, bestCostTable));   
        
     }
        
     //**Motor da engine simullatedAnelling*/
     protected void run()
     {      

            // applyCovering(); //Faz a cobertura com a cover inicial aleatória

            NodeAig randomNode = null;
            AigCut candidateCut = null;
            float candidateCutCost;
            
            AigCut singleCut = null;
            float singleCutCost;
            
            float delta;
            float epsilon = (float) 0.001;

            this.currentCoverCut.clear();
             //Copia a cobertura inicial e os valores para começar o SA
            for(Map.Entry<NodeAig,AigCut> element: bestCoverCut.entrySet())
                currentCoverCut.put(element.getKey(), element.getValue());
            currentCostTable.clear();
            for(Map.Entry<NodeAig,Float> element: bestCostTable.entrySet())
                currentCostTable.put(element.getKey(), element.getValue());
            
            System.out.println("Custo Inicial:"+getCostCovering(currentCoverCut, currentCostTable));
            
            while (this.temp > epsilon)
            {                       
              randomNode = generateRandomNode(); //Escolhe um nodo aleatório do AIG
              //Procura o corte atual e o custo do nodo escolhido
              singleCut = currentCoverCut.get(randomNode);
              singleCutCost = currentCostTable.get(randomNode);

              //Escolhe um corte aleatório no nodo selecionado e calcula o custo
              candidateCut = generateNeighbour(randomNode);
              candidateCutCost =  sumCost(candidateCut, randomNode);

              delta       = singleCutCost - candidateCutCost;

              if (delta > 0){ //novo corte tem custo menor que o corte atual
                      AigCut old = currentCoverCut.get(randomNode);
                      if(old != null ){
                         /**acerta pra tira o overlap caso seja possivel*/
                          for(NodeAig cutIn: old.getCut()){
                              boolean inputCut = false;
                              for(Map.Entry<NodeAig,AigCut> cuts: currentCoverCut.entrySet()){
                                  if(cuts.getValue().contains(cutIn)){
                                      inputCut = true;
                                      break;
                                  }
                              }
                              if((!cutIn.isInput())&&(inputCut == false)){
                                  currentCoverCut.remove(cutIn);
                              }
                          }
                          /***-------------------------------------------*/
                      }
                      this.currentCut.put(randomNode, candidateCut);
                      currentCoverCut.put(randomNode,candidateCut);      
                      currentCostTable.put(randomNode, candidateCutCost);
                      bfsAigVisitorCoveringSimulated mybfs = new bfsAigVisitorCoveringSimulated(currentCoverCut,currentCut);
                      for(NodeAig father: candidateCut.getCut()){
                         if(!father.isInput()){
                              father.accept(mybfs);
                         }
                  }                     
                  if (getCostCovering(currentCoverCut,currentCostTable) < getCostCovering(bestCoverCut, bestCostTable)){
                     //Copia a cobertura inicial e os valores para começar o SA
                    this.bestCoverCut.clear();
                    for(Map.Entry<NodeAig,AigCut> element: currentCoverCut.entrySet())
                            bestCoverCut.put(element.getKey(), element.getValue());
                    this.bestCut.clear();
                    for(Map.Entry<NodeAig,AigCut> element: currentCut.entrySet())
                            bestCut.put(element.getKey(), element.getValue());
                    bestCostTable.clear();
                    for(Map.Entry<NodeAig,Float> element: currentCostTable.entrySet())
                            bestCostTable.put(element.getKey(), element.getValue());
//                      showCovering(bestCoverCut);
                      
                   //System.out.println("Custo Aig:"+getCostCovering(bestCoverCut, bestCostTable));   
                      
                  }
                                     
              }
              else{ //Escolha probabilistica do SA
                  if (Math.exp(delta/this.temp) > Math.random()){
                      AigCut old = currentCoverCut.get(randomNode);
                      if(old != null ){
                         /**acerta pra tira o overlap caso seja possivel*/
                          for(NodeAig cutIn: old.getCut()){
                              boolean inputCut = false;
                              for(Map.Entry<NodeAig,AigCut> cuts: currentCoverCut.entrySet()){
                                  if(cuts.getValue().contains(cutIn)){
                                      inputCut = true;
                                      break;
                                  }
                              }
                              if((!cutIn.isInput())&&(inputCut == false)){
                                  currentCoverCut.remove(cutIn);
                              }
                          }
                          /***-------------------------------------------*/
                      }
                      this.currentCut.put(randomNode, candidateCut);
                      currentCoverCut.put(randomNode,candidateCut);      
                      currentCostTable.put(randomNode, candidateCutCost);
                      bfsAigVisitorCoveringSimulated mybfs = new bfsAigVisitorCoveringSimulated(currentCoverCut,currentCut);
                      for(NodeAig father: candidateCut.getCut()){
                         if(!father.isInput()){
                              father.accept(mybfs);
                         }                  
                      }
                  }
              }
                
                
              this.temp = this.temp * coolingRate;
            }
    }
    
    protected NodeAig generateRandomNode() 
    {
        NodeAig newNode             = null;
        Set<NodeAig> allNodes       = myAig.getAllNodesAig();
        Iterator<NodeAig> iterator  = allNodes.iterator();
        int aigSize                 = allNodes.size();
        int position                = new Random().nextInt(aigSize); 
        
        for(int i = 0 ; i<=position ; i=i+1){
           newNode = iterator.next();
        }
//        System.out.print(" Tamanho do AIG: "+aigSize+
//         " Nodo aleatório: "+newNode.getId()+
//         " Nodo aleatório: "+newNode.getName()+"\n\n");
//        System.out.print("Tamanho:"+aigSize+"Nodo aleatório: "+newNode.getId()+"\n");
        return newNode;
    }

    protected AigCut generateNeighbour(NodeAig nodeActual) 
    {
        AigCut newCut      = null;
        Set<AigCut> cuts = kcuts.getCuts().get(nodeActual); 
        Iterator<AigCut> iterator = cuts.iterator();

        //gerar tabela de custos de todos os cortes do nodo
        int cutSize = cuts.size();
        int position = new Random().nextInt(cutSize); 
        for(int i = 0 ; i<=position ; i=i+1){
           newCut = iterator.next();
        }
            
        return newCut;
    }

protected void randomInitialSolution()
    {
        dfsAigVisitorAreaGetLevel dfs = new dfsAigVisitorAreaGetLevel(this.levelNode);
        for(NodeAig node: myAig.getNodeInputsAig()){
            System.out.println(node.getName());
            this.levelNode.put(node,1);
        }
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

    protected  void getBestArea(NodeAig nodeActual)
    {
        if(this.bestCoverCut.containsKey(nodeActual))
          return;
        Map<AigCut,Float> tableCost     = new HashMap<AigCut, Float>();
        Set<AigCut> cuts                = kcuts.getCuts().get(nodeActual);
        Iterator<AigCut> iterator       = cuts.iterator();
        do
        {          
           AigCut cut = iterator.next(); 
           if(nodeActual.isInput())
           {
              bestCoverCut.put(nodeActual, cut);
              bestCut.put(nodeActual, cut);
              bestCostTable.put(nodeActual,(float)0);              
              break;
           }
           else
           {
              float cost  = sumCost(cut,nodeActual);
              tableCost.put(cut, (Float)cost);              
           }           
        }while(iterator.hasNext()); //contabiliza areas
        if(!nodeActual.isInput())
            selectRandomCut(nodeActual,tableCost);
        System.out.print(" BestArea Nodo: "+nodeActual.getName()+
         " Custo: "+bestCostTable.get(nodeActual)+
         " Profundidade: "+levelNode.get(nodeActual)+
         " Corte:");
        bestCoverCut.get(nodeActual).showCut();
    }

    protected float sumCost(AigCut cut, NodeAig nodeActual) 
    {
        float input     =0;
        int output      =0;
        if(nodeActual.getChildren().isEmpty())
            output =1;//fanouts
        else
            output = nodeActual.getChildren().size(); //fanouts
        if(cut.size()==1) //corte unitário sempre 1+entradas
        {
          for(NodeAig node: nodeActual.getParents())
          {
            if(!bestCostTable.containsKey(node))
                getBestArea(node);
            input+=bestCostTable.get(node);
          }
          return this.function.eval(1,this.levelNode.get(nodeActual),0,input, output,0);  //área do corte 1
        }
        for(NodeAig node:cut)
        {
            if(!bestCostTable.containsKey(node))
                getBestArea(node);
            input+=bestCostTable.get(node);
        }
        return this.function.eval(1,this.levelNode.get(nodeActual),0,input, output, 0);
    }

    protected void selectRandomCut(NodeAig nodeActual, Map<AigCut, Float> tableCost) 
    {
        /*
        criar uma solução inicial aleatória 
        */

        AigCut cut      = null;
        AigCut cutBest  = bestCost(tableCost);
        Set<AigCut> cuts = kcuts.getCuts().get(nodeActual); 
        Iterator<AigCut> iterator = cuts.iterator();

        int cutSize = cuts.size();
        int position = new Random().nextInt(cutSize); 
       
        // System.out.print("Nodo: "+nodeActual.getName()+
        //     " tamanho: "+cuts.size()+
        //     " Rand: "+position+"\n");
        
        for(int i = 0 ; i<=position ; i=i+1){
           cut = iterator.next();
        }
        cutBest = cut;   
        bestCut.put(nodeActual, cut);
        bestCoverCut.put(nodeActual,cutBest);      
        bestCostTable.put(nodeActual, (tableCost.get(cutBest)));
    }
    //**Método contabiliza a profundidade utilizando bfs
    protected Integer sumLevel(AigCut cut, NodeAig nodeActual)
    {
        if((nodeActual.isOutput())&&(nodeActual.getParents().isEmpty())) //constant
            return 1;
        bfsAigVisitorAreaSumLevel bfs = new bfsAigVisitorAreaSumLevel(levelNode, cut);
        nodeActual.accept(bfs);
        return bfs.getLevel();
    }
    protected AigCut bestCost(Map<AigCut, Float> tableCost) 
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
    
    void showCovering(Map<NodeAig,AigCut> current){
        System.out.println("========COBERTURA========");
        for(Map.Entry<NodeAig,AigCut> elem: current.entrySet()){
            System.out.print(elem.getKey().getName()+" :");
            elem.getValue().showCut();
       }
    } 
    public float getCostCovering(Map<NodeAig,AigCut> covering, Map<NodeAig,Float> tableCost){
        float costFinal = 0;
        for(Map.Entry<NodeAig,AigCut> covered: covering.entrySet())
            costFinal+=tableCost.get(covered.getKey());
        return costFinal;
    }
    
    
    public String getEqn() throws FileNotFoundException
    {
       String eqn = LogsCoveringToEqn.coveringToEqn(myAig, bestCoverCut);
       return eqn;
    }
            
}










