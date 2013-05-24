package kcutter;

import brc.*;
import aig.*;
import java.util.*;

/**
 * Classe que re-implementa o SET de nodos para Kcuts com Brc
 * @author Julio Saraçol
 */
public class AigCutBrc extends AigCut 
{
    protected int k;
    protected Boolean flagBrc;
    protected BRC brc = null;
    protected TreeMap<String, BRC> brcVariables =null;
    protected Boolean notMatching = false;
 
    /**
     * Construtor
     */
    public AigCutBrc(int k) {
        super();
        this.k = k;
        this.flagBrc = false;
        //*adaptação para repreentar matchings invertidos
        this.notMatching = false;
    }

    /**
     * Construtor para um Cut
     * @param node The node for the cut
     */
    public AigCutBrc(NodeAig node, int k) {
        super(node);
        this.k=k;
        this.flagBrc = false;
    }

    @Override
    public boolean add(NodeAig e) {
        this.flagBrc = false;        
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends NodeAig> c) {
        this.flagBrc = false;        
        return super.addAll(c);
    }
    
    public void addCuts(Set<NodeAig> cuts)
    {
        for(NodeAig node : cuts)
            add(node);
    }

    @Override
    public boolean remove(Object o) {
        this.flagBrc = false;
        return super.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        this.flagBrc = false;
        return super.removeAll(c);
    }

    public void showCut(NodeAig nodeCurrent) {
       if(cut.size() == 0)
           return;
       System.out.print("[");
       int i=0;
       for(NodeAig node: cut)
       {
           if(i < cut.size()-1)
                System.out.print(node.getName()+",");
           else{
               System.out.println(node.getName()+"]");
           }
           getBrc(nodeCurrent);
           i++;
       }
    } 

    public void showCutBrc(NodeAig nodeCurrent) {
       if(cut.size() == 0){
           System.out.print("VAZIO");          
           return;
       }
       System.out.print("[");
       int i=0;
       for(NodeAig node: cut)
       {
           if(i < cut.size()-1)
                System.out.print(node.getName()+",");
           else{
               System.out.println(node.getName()+"]=>"+BRCHandler.toIntegerString(getBrc(nodeCurrent)));
           }
           i++;
       }
    } 
    
    @Override
    public String getBytesForLog() 
    {
       String cutString="";
       cutString +="[";
       int i=0;
       for(NodeAig node: this.cut)
       {
           if(i < this.cut.size()-1)
               cutString +=node.getName()+",";
           else
               cutString +=node.getName()+"]=>"+BRCHandler.toIntegerString(getBrc(node))+"\n";
           i++;
       }
       return cutString;
    }
    
    /**
     * Acesso a BRC do K-cut
     * @return the cut's signature in BRC
     */
    public BRC getBrc(NodeAig root) {
        if(this.flagBrc == false)
            updateBrc(root);
        return this.brc;
    }
    
    public BRC getBrc() {
       throw new UnsupportedOperationException("Not supported yet.");
    }
  
    /**Cria o BRC referente ao corte atual*/ 
    public void updateBrc(NodeAig root)
    {   //aplica os brc nos cut 
        this.flagBrc            = true;
        this.brc                = new BRC((int)Math.pow(2,this.k));
        ArrayList<String> var   = getVariables(); //lista as variaveis de entrada do corte
        this.brcVariables       = BRCBuilder.getBasicRepresentationCodes(k,var);
        //System.out.println("raiz: "+root.getName());
        //showCut();
        bfsNodeAigVisitorKCutsBrc createBrc = new bfsNodeAigVisitorKCutsBrc(this);       
        root.accept(createBrc);
        this.brc = this.getBrcVariables().get(root.getName());
    }
    /**Método necessário para interface com o BRCBuilder*/
    protected ArrayList<String> getVariables()
    {
        ArrayList<String> names = new ArrayList<String>();
        for(NodeAig nodeCurrent: this.cut)
            names.add(nodeCurrent.getName());
        return names;
    }


    public TreeMap<String, BRC> getBrcVariables() {
        return brcVariables;
    }

    public void setBrcVariables(TreeMap<String, BRC> brcVariables) {
        this.flagBrc = false;
        this.brcVariables = brcVariables;
    }

    public int getK() {
        return this.k;
    }
    
    public void setK(int k) {
        this.k = k;
    }

    /*métdodo para tratar matchings invertidos
     * @param notMatching 
     */
    public void setNotMatching() {
        if(this.notMatching == true)
            this.notMatching = false;
        else
            this.notMatching = true;
        this.notMatching = notMatching;
    }

    public Boolean getNotMatching() {
        return notMatching;
    }
    
    
}