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
  
  
      //*Método que dada uma expressão encontra o melhor caracter para colocar como raiz de um arvore de expressão
    public static int findRoot(String s) {

        int contParentheses = 0;
        int signalRoot = -1;
        int posAuxSignal = 0;
        int pos = 0;
        boolean iteration;

        while (pos < s.length()) {

            if (s.charAt(pos) == '*' || s.charAt(pos) == '+' || s.charAt(pos) == '(' || (s.charAt(pos) == '!') && s.charAt(pos+1) == '(') {

                posAuxSignal = pos;
                contParentheses = 0;

                if (s.charAt(pos) == '(') {
                    contParentheses++;
                }

                iteration = true;

                while (iteration) {
                    pos++;

                    while (s.charAt(pos) == '!') {
                        pos++;
                    }

                    if (s.charAt(pos) == '(') {
                        contParentheses++;
                    } else {
                        if (s.charAt(pos) == ')') {
                            contParentheses--;
                        }
                    }

                    if (contParentheses == 0) {
                        iteration = false;
                    }
                }

                if (pos == s.length() - 1) {

                    if ((s.charAt(0) == '!' && s.charAt(1) == '(') && signalRoot == -1) {
                        return -1;
                    } else {
                        if (signalRoot == -1) {
                            return posAuxSignal;
                        } else {
                            return signalRoot;
                        }
                    }
                } else {
                    pos++;
                    if (((s.charAt(0) == '!' && s.charAt(1) == '(') || (s.charAt(0) == '!' && s.charAt(1) == '!')) && signalRoot == -1) {
                        signalRoot = pos;
                    } else {
                        if (s.charAt(posAuxSignal) == '+' && s.charAt(pos) == '+') {
                            signalRoot = pos;
                        } else {
                            if (s.charAt(posAuxSignal) == '*' && (s.charAt(pos) == '+' || s.charAt(pos) == '*')) {
                                signalRoot = pos;
                            } else {
                                if (s.charAt(pos) != '*' && s.charAt(pos) != '+' && s.charAt(pos) != '!') {
                                    signalRoot = posAuxSignal;
                                }
                            }

                        }
                    }
                }
            }

            pos++;

        }

        if (signalRoot != -1) {
            return signalRoot;
        } else {
            return 0;
        }

    }
}
