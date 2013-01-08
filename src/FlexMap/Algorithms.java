package FlexMap;
import aig.*;

/**
 * Classe que concentra diversos algoritmos utilizados nas diversas estruturas
 * @author Julio Saraçol
 */

public class Algorithms 
{      
  
  /**Método que faz avaliação se a aresta de dois nodos é negada
     * @param nodeCurrent 
     * @param nodeFather
     * @return is inverter edge? Yes/No
     */
  public static boolean isInverter(NodeAig nodeCurrent,NodeAig nodeFather)
  {
       EdgeAig edge = null;
       for(int i=0;i<nodeCurrent.getAdjacencies().size();i++)
       {
           if(nodeCurrent.getAdjacencies().get(i).getVertex2().equals(nodeFather))
            edge = (EdgeAig) nodeCurrent.getAdjacencies().get(i);
       }
       return edge.isInverter();
   }
  public static EdgeAig getEdge(NodeAig nodeCurrent,NodeAig father)
  {
       EdgeAig edge = null;
       for(int i=0;i<nodeCurrent.getAdjacencies().size();i++)
       {
           if(nodeCurrent.getAdjacencies().get(i).getVertex2().equals(father))
            edge = (EdgeAig) nodeCurrent.getAdjacencies().get(i);
       }
       return edge;
   }
}
