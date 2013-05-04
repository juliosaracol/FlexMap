package FlexMap;
import aig.*;
import java.util.ArrayList;
import tree.*;

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
  /**Método que retorna a aresta entre os nodos indicados nos parametros
   * @param nodeCurrent
   * @param father
   * @return EdgeAig
   */
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
  /**Método que identifica subárvores que possuem somente nodo-inversor, caso aconteça coloca a saída-invertida na árvore original como raíz*/
  public static boolean deletedTreeInverter(Trees trees)
  {
      ArrayList<Tree>    setRootTree    = new ArrayList<Tree>();
      ArrayList<Tree>    removeTree     = new ArrayList<Tree>();
      ArrayList<NodeAig> rootsTree      = new ArrayList<NodeAig>();
      for(Tree treeActual : trees.getRoots())
      {
        if(treeActual.getTree().size()==2)
        {
          System.out.println("ARVORE @ NODOS "+treeActual.getRoot().getName());
          for(Tree trueTree : trees.getRoots())
          {  
            if(trueTree.getRoot().getId() == treeActual.getRoot().getParents().get(0).getId()) // acha a árvore do pai da saida invertida
            {
                System.out.println("SET DE NOVA RAIZ de: "+treeActual.getRoot().getName()+" PARA "+trueTree.getRoot().getName());
                setRootTree.add(trueTree);
                removeTree.add(treeActual);
                rootsTree.add(treeActual.getRoot());
            }
          }
        }
      }
      if(setRootTree.isEmpty())
          return false;
      else
      {
          for(int i=0;i<setRootTree.size();i++)
          {
              removeTree.get(i).getRoot().getAdjacencies().clear();
              setRootTree.get(i).addEdge(removeTree.get(i).getRoot(),setRootTree.get(i).getRoot(), true);
              setRootTree.get(i).setRoot(removeTree.get(i).getRoot());
          }
          trees.getRoots().removeAll(removeTree);
          return true;
      }
  }
}
