package aig;
import graph.*;
import io.*;
import java.util.*;

/**Classe que define a estrutura de Aig a partir de Graph 
 * @author Julio Saraçol
 * */

public class Aig extends Graph 
{
    protected TreeMap<String, Integer> indexNames;                      /** Atributo associa o indice 'id' do nodo ao 'name'*/ 
    protected String inputsAig[][];                                     //definição das arestasAig
    protected String outputsAig[][];                                    //definição das arestasAig
    protected String gatesAig[][];                                      //definição das arestasAig
    protected String latchAig[][]; 										
    protected ArrayList<ArrayList<String>> definitionAig;               //definição de todo o Aig 
    protected int m,i,l,o,a;
    protected Set<NodeAig>  allNodesAig;
    protected Set<NodeAig>  nodeOutputsAig;
    protected Set<NodeAig>  nodeInputsAig;
    protected Set<NodeAig>  nodeGatesAig;
   /**Construtor 
   *@param fileName (omeArquivo de Aig em ascii '.aag')
   * */
   public Aig(String fileName)
   {
      super();
      definitionAig = new ArrayList<ArrayList<String>>();
      indexNames    = new TreeMap<String,Integer>();
      allNodesAig   = new HashSet<NodeAig>(); 
      nodeGatesAig  = new HashSet<NodeAig>();
      nodeInputsAig = new HashSet<NodeAig>();
      nodeOutputsAig= new HashSet<NodeAig>();
      parserAig(fileName);
      createNodesAig();
      System.out.println("#AIG carregado com sucesso");
   }    
   /** Método que realiza o parser do arquivo baseado no padrão AIGER carrega os vetores de inputs|outputs|latches|gates
    * @param file (nomeArquivoEntrada.aag)*/
   public void parserAig(String file)
   {            
    StringTokenizer lines,line;
    String subline;
    String fileInput = Readfile.LoadFileToString(file);  
    this.m=0;this.i=0;this.l=0;this.a=0;this.o=0;
    int iCount=0,lCount=0,oCount=0,aCount=0,index=0;

    if(fileInput == null)
    {
        System.out.println("O arquivo não foi carregado com sucesso");
        System.exit(1);
    }

    lines = new StringTokenizer(fileInput, "\n");
    while(lines.hasMoreTokens())                        
    {              
        subline = lines.nextToken();                    
        line = new StringTokenizer(subline," ");        
        String token = line.nextToken();
        if(token.equals("aag"))
        {
           this.m = Integer.parseInt(line.nextToken());
           this.i = Integer.parseInt(line.nextToken());
           this.l = Integer.parseInt(line.nextToken());
           this.o = Integer.parseInt(line.nextToken());
           this.a = Integer.parseInt(line.nextToken());
           this.inputsAig   = new String[this.i][1];
           this.latchAig    = new String[this.l][2];
           this.outputsAig  = new String[this.o][1];
           this.gatesAig    = new String[this.a][3];
        }
        else
        {
            this.definitionAig.add(new ArrayList<String>()); 
            if(this.i > iCount) 
            {
                     this.inputsAig[iCount][0] = token;
                     this.definitionAig.get(definitionAig.size()-1).add(token);
                     iCount++;
            }               
            else
            {
                if(this.l > lCount) //latches
                {
                   this.latchAig[lCount][0] = token;
                   this.definitionAig.get(definitionAig.size()-1).add(token);
                   while(line.hasMoreTokens())
                   {
                     token = line.nextToken();
                     this.latchAig[lCount][1] = token;
                     this.definitionAig.get(definitionAig.size()-1).add(token);                        
                   }
                   lCount++;
                }
                else
                {
                    if(this.o > oCount) //outputs
                    { 
                        this.outputsAig[oCount][0]= token;
                        this.definitionAig.get(definitionAig.size()-1).add(token);
                        oCount++;
                    }
                    else
                    {
                        if(this.a > aCount) //gates
                        {
                           index=0;
                           this.gatesAig[aCount][index] = token;
                           index++;
                           this.definitionAig.get(definitionAig.size()-1).add(token);
                           while(line.hasMoreTokens())
                           {
                               token = line.nextToken();
                               this.gatesAig[aCount][index] = token;
                               index++;
                               this.definitionAig.get(definitionAig.size()-1).add(token);
                           }
                           aCount++;
                        }
                        else  //symbol
                        {
                           if((token.length() == 2)&&(token.substring(0,1).equals("i")))
                           {
                              token = line.nextToken();
                              this.definitionAig.get(definitionAig.size()-1).add(token);
                           }
                           else
                           {
                               if((token.length() == 2)&&(token.substring(0,1).equals("o")))
                                   this.definitionAig.get(definitionAig.size()-1).add(token);
                               else //comments
                                   this.definitionAig.get(definitionAig.size()-1).add(token);
                           }                               
                        }
                    }        
                }
            }
        }
    }
   }    
    //Método que instância o NodesAig através das inputs|outputs|latchs|gates
    public void createNodesAig()
    {
        int j,w;
        /*instância nodos inputs*/
        for(j=0;j<this.i;j++)
        {      
           if(this.getVertexName(this.inputsAig[j][0]) == null) //se não existe o nodo
               if((Integer.parseInt(this.inputsAig[j][0]) %2) == 0)
                   this.createInput(this.inputsAig[j][0]);
           else
               nodeInputsAig.add(this.getVertexName(this.inputsAig[j][0]));
        }

        /*instância gates e as arestas no aig se necessário*/
        for(j=0;j<this.a;j++)
        {      
            /*instância os nodosAig*/
            for(w=0;w<3;w++)
            {      
              if(this.getVertexName(this.gatesAig[j][w]) == null)
                if(Integer.parseInt(this.gatesAig[j][w])%2==0)
                    this.createAnd(this.gatesAig[j][w]);   
              else
                  nodeGatesAig.add(this.getVertexName(this.gatesAig[j][w]));
            }
        }
        
        /*instância as arestas entre os nodos*/
        for(j=0;j<this.a;j++)
        {
            if(Integer.parseInt(this.gatesAig[j][1])%2==0) //se não inversor cria direto
                this.addEdge(this.gatesAig[j][0],this.gatesAig[j][1],false);               
            else
                this.addEdge(this.gatesAig[j][0],String.valueOf(Integer.parseInt(this.gatesAig[j][1])-1),true);                             
            
            if(Integer.parseInt(this.gatesAig[j][2])%2==0) //se não inversor cria direto
                this.addEdge(this.gatesAig[j][0],this.gatesAig[j][2],false);
            else
                this.addEdge(this.gatesAig[j][0],String.valueOf(Integer.parseInt(this.gatesAig[j][2])-1),true);                             
        }        
        
        /*instância nodos outputs*/
        for(j=0;j<this.o;j++)
        {      
           if(this.getVertexName(this.outputsAig[j][0]) == null)//se não existe o nodo
           {
               if(Integer.parseInt(this.outputsAig[j][0])%2==0)
                this.createOutput(this.outputsAig[j][0]);
               else
               {
                   this.createOutput(this.outputsAig[j][0]); //cria a aresta inversora
                   this.addEdge(this.outputsAig[j][0],String.valueOf(Integer.parseInt(this.outputsAig[j][0])-1),true);
               }
           }
           else
               nodeOutputsAig.add(this.getVertexName(this.outputsAig[j][0]));
        }
        /*instância arestas referentes aos latches*/
        int nameLatch = -1;
        for(j=0;j<this.l;j++)
        {
          String nameNodeAig = String.valueOf(nameLatch);
          this.createLatch(nameNodeAig);
          this.addEdge(this.latchAig[j][0],nameNodeAig,false);
          this.addEdge(nameNodeAig,this.latchAig[j][1],false);
          nameLatch--;
        }       
    }        
    public NodeAigInput createInput(String name)
    {
        try 
        {
            NodeAigInput temp = new NodeAigInput(this.verticesCount,name);
            this.vertices.put(this.verticesCount, temp);
            this.indexNames.put(name,this.verticesCount);
            allNodesAig.add(temp);
            nodeInputsAig.add(temp);
            this.verticesCount++;
            return temp;
        }
        catch(Exception e) {
            System.out.print(e.toString());
        }
        return null;   
    }
    public NodeAigOutput createOutput(String name)
    {
        try 
        {
            NodeAigOutput temp = new NodeAigOutput(this.verticesCount,name);
            this.vertices.put(this.verticesCount, temp);
            this.indexNames.put(name,this.verticesCount);
            allNodesAig.add(temp);
            nodeOutputsAig.add(temp);
            this.verticesCount++;
            return temp;
        }
        catch(Exception e) {
            System.out.print(e.toString());
        }
        return null;   
    }    
    public NodeAigGate createAnd(String name)
    {
        try 
        {
            NodeAigGate temp = new NodeAigGate(this.verticesCount,name);
            this.vertices.put(this.verticesCount, temp);
            this.indexNames.put(name,this.verticesCount);
            allNodesAig.add(temp);
            nodeGatesAig.add(temp);            
            this.verticesCount++;
            return temp;
        }
        catch(Exception e) {
            System.out.print(e.toString());
        }
        return null;   
    }
    public NodeAigLatch createLatch(String name)
    {
        try 
        {
            NodeAigLatch temp = new NodeAigLatch(this.verticesCount,name);
            this.vertices.put(this.verticesCount, temp);
            this.indexNames.put(name,this.verticesCount);
            allNodesAig.add(temp);
            this.verticesCount++;
            return temp;
        }
        catch(Exception e) {
            System.out.print(e.toString());
        }
        return null;   
    }    
    /**override do Método addEdge para trabalhar com EdgesAig*/
    public int addEdge(String vertex1, String vertex2,boolean type) throws NullPointerException
    {        
        try {
            Vertex v1 = getVertexName(vertex1);
            Vertex v2 = getVertexName(vertex2);
            
            if(v1 == null || v2 == null) {
                throw new NullPointerException("Vertices não instanciados");// dispara a Exceção throws
            }

            EdgeAig auxEdge = new EdgeAig(this.edgesCount,v1,v2);
            if(type == true) //caso seja um inversor faz o set da aresta
                auxEdge.createInverter();
            this.edges.put(auxEdge.getId(), auxEdge);
            v1.getAdjacencies().add(auxEdge);
            v2.getAdjacencies().add(auxEdge);
            this.edgesCount++;
            return auxEdge.getId();
        }
        catch(Exception e) {
            System.out.print("não foi possivel inserir a aresta" + e.toString());
        }
        return 0;
    }       
    /** Métodos para Visualizar a estrutura do AIG */
    public void viewTableAig ()
    {
        System.out.println("------TABELA inputAIG-------");
        for(int w=0;w<this.i;w++)
            System.out.print(w+"["+inputsAig[w][0]+"]\n");
        System.out.println("----------------------------");
        System.out.println("------TABELA outputAIG------");
        for(int w=0;w<this.o;w++)
            System.out.print(w+"["+outputsAig[w][0]+"]\n");
        System.out.println("----------------------------");
        System.out.println("------TABELA gatesAIG------");
        for(int w=0;w<this.a;w++)
        {
          System.out.print("[");
          for(int q=0;q<3;q++)
              System.out.print(gatesAig[w][q]+"-");
          System.out.print("]\n");
        }
        System.out.println("----------------------------");
    }
    /**Método que apresenta definição do aig a partir do ascii carregado*/
    public void viewDefintionAig ()
    {
        System.out.println("---------AIG---------");
        for(int w=0;w<this.definitionAig.size();w++)
        {
           for(int j=0;j<this.definitionAig.get(w).size();j++)
               System.out.print(this.definitionAig.get(w).get(j)+"-");
           System.out.print("\n");
        }
        System.out.println("----------------------------");
    }
    /**Método que apresenta todo Aig com sua hierarquia de nodos*/
    public void showAig()
    {
        for(int i=1;i<this.getVerticesCount();i++)
        {
            /*procura todas as adjacencias do nodo nameNode*/
            NodeAig nodo = (NodeAig)this.getVertex(i);
            System.out.print("\nnodoAig: "+nodo.getName()+"\n");
            for(int z=0;z<nodo.getParents().size();z++)
                System.out.println("É FILHO De:"+nodo.getParents().get(z).getName());
            for(int j=0;j<nodo.getChildren().size();j++)
                System.out.println("É PAI De:"+nodo.getChildren().get(j).getName());                
        }
    }
    /** Método que faz interface com ID <-> Name do NodeAig*/
    public NodeAig getVertexName(String name) 
    {
        if(this.indexNames.get(name) == null)
            return null;
        else
            return (NodeAig)this.vertices.get(this.indexNames.get(name));
    }    
    //Método que possibilita acesso as definições do AIG-------------------------------------
    public ArrayList<ArrayList<String>> getDefinitionAig() {
        return definitionAig;
    }
    public String[][] getGatesAig() {
        return gatesAig;
    }    
    public TreeMap<String, Integer> getIndexNames() {
        return indexNames;
    }
    public String[][] getInputsAig() {
        return inputsAig;
    }
    public String[][] getLatchAig() {
        return latchAig;
    }
    public String[][] getOutputsAig() {
        return outputsAig;
    }
    public int getM() {
        return m;
    }
    public int getI() {
        return i;
    }
    public int getL() {
        return l;
    }
    public int getO() {
        return o;
    }
    public int getA() {
        return a;
    }
    //Métodos que possibilitam acesso a referencias para os nodos do Aig---------------------
    public Set<NodeAig> getAllNodesAig() {
        return Collections.unmodifiableSet(allNodesAig);
    }
    public void setAllNodesAig(Set<NodeAig> allNodesAig) {
        this.allNodesAig = allNodesAig;
    }
    public Set<NodeAig> getNodeGatesAig() {
        return Collections.unmodifiableSet(nodeGatesAig);
    }
    public void setNodeGatesAig(Set<NodeAig> nodeGatesAig) {
        this.nodeGatesAig = nodeGatesAig;
    }
    public Set<NodeAig> getNodeInputsAig() {
        return Collections.unmodifiableSet(nodeInputsAig);
    }
    public void setNodeInputsAig(Set<NodeAig> nodeInputsAig) {
        this.nodeInputsAig = nodeInputsAig;
    }
    public Set<NodeAig> getNodeOutputsAig() {
        return Collections.unmodifiableSet(nodeOutputsAig);
    }
    public void setNodeOutputsAig(Set<NodeAig> nodeOutputsAig) {
        this.nodeOutputsAig = nodeOutputsAig;
    }    
    public Iterator<NodeAig> getIteratorNodesAig(){
        return this.allNodesAig.iterator();
    }
 }