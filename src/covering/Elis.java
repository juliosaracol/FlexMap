package covering;

import aig.*;
import tree.*;
import FlexMap.Algorithms;

import java.util.*;
/**
 * Classe que aplica a cobertura de acordo com  algoritmo da Elis (Correia,2005)
 * @author Julio Saraçol
 */
public class Elis 
{

    protected Trees trees;
    protected Integer s;
    protected Integer p;
    
    public Elis(TreesElis trees, Integer s, Integer p)
    {
        this.s = s;
        this.p = p;
        this.trees      = trees;
        Set<Tree> roots = trees.getRoots();
        for(Tree root : roots)
        {
            dfsNodeAigVisitorOrTree dfsOrGate = new dfsNodeAigVisitorOrTree(trees,root);
            root.getRoot().accept(dfsOrGate);
        }
        for(Tree root: this.trees.getRoots())
        {
            System.out.println("**************TREE root:"+root.getRoot().getName());
            deMorgan(root,root.getRoot(),false);           
        }
        
    }
    /**Método que aplica deMorgan na árvore até as entradas
     * @param tree (árvore do nodo)
     * @param root (nodo atual) 
     * @param type (boolean indica se ja encontrou o inversor antes)
     */
    private void deMorgan(Tree tree, NodeAig root, boolean type)
    {
      if(root.isInput())
          return;
      else
      {
        if((root.getParents().size()==1))//caso se tiver saida negada elimina o nodo
        {
            NodeAig newRoot = createGate(tree,root.getParents().get(0));
            tree.setRoot(newRoot);
            tree.removeEdge(Algorithms.getEdge(root, root.getParents().get(0)).getId());
            tree.removeVertex(root.getId());
            for(NodeAig father: tree.getRoot().getParents())
                deMorgan(tree, father,true);
            return;
        }  
        
        if(type==true) //caso de ja ter encontrado um inversor antes
        {
          String porta="";
          if(root.isOR())
              porta= "AND";
          else
              porta = "OR";
          System.out.println("Set Nodo deMorgan: "+root.getName()+" "+porta);
          ArrayList<NodeAig> fathers = root.getParents();
          for(int i=0;i<fathers.size();i++)
          {
              if(!Algorithms.isInverter(root,fathers.get(i)))
                deMorgan(tree,fathers.get(i), false);          
              else
                deMorgan(tree,fathers.get(i), true);          
          }
          createGate(tree, root);  
       }        
       else
       {
          ArrayList<NodeAig> fathers = root.getParents();
          for(int i=0;i<fathers.size();i++)
          {
              if(!Algorithms.isInverter(root,fathers.get(i)))
                deMorgan(tree,fathers.get(i), false);          
              else
                deMorgan(tree,fathers.get(i), true);          
          }
       }
     }            
        
    }

    public Integer getP() {
        return p;
    }

    public Integer getS() {
        return s;
    }

    public Trees getTrees() {
        return trees;
    }
    
    /**Método que instancia um nodo cópia trocando a operação lógica do nodo and->or || or->and invertendo as arestas dos pais e mantendo a aresta do filho */
    public NodeAig createGate(Tree tree, NodeAig gate)
    {
        if(gate.isOR())
        {
            NodeAigGate newNode = new NodeAigGate(gate.getId(), gate.getName());
            //tree.removeVertex(gate.getId());
            for(NodeAig father: gate.getParents())
            {
                System.out.println("set aresta de "+gate.getName()+" para "+father.getName()+" :"+!(Algorithms.isInverter(gate, father)));
                tree.addEdge(newNode,father, !(Algorithms.isInverter(gate, father)));
            }
            tree.addEdge(gate.getChildren().get(0),newNode, (Algorithms.isInverter(gate.getChildren().get(0),gate)));
            tree.add(newNode);
            return newNode;
        }
        if(gate.isAnd()||gate.isOutput())
        {
            
            NodeAigGateOr newNode = new NodeAigGateOr(gate.getId(), gate.getName());
            for(NodeAig father: gate.getParents())
            {
                System.out.println("set aresta de "+gate.getName()+" para "+father.getName()+" :"+!(Algorithms.isInverter(gate, father)));
                tree.addEdge(newNode,father, !(Algorithms.isInverter(gate, father)));
            }
            //tree.removeVertex(gate.getId());
            tree.addEdge(gate.getChildren().get(0),newNode, (Algorithms.isInverter(gate.getChildren().get(0),gate)));
            tree.add(newNode);
            return newNode;
        }
        return null;
    }
}
