package library;
import java.util.*;
import java.io.*;

//lista de funcoes e seus respectivos dados
public class Histogram{
	protected TreeMap<String, FunctionData> fmap;
        protected int cost;   //custo total do circuito
        protected int invs;
        protected int maj;
        protected int nVars;
	
	public Histogram(){
            fmap = new TreeMap<String, FunctionData>();
            cost = 0;
            invs = 0;
            maj = 0;
            nVars = 3;
	}
	
        public Histogram(int nvars){
            fmap = new TreeMap<String, FunctionData>();
            cost = 0;
            invs = 0;
            maj = 0;
            nVars = nvars;
	}
        
	//adiciona uma nova função no histograma
	public void addFunction(FunctionData f){
            if (f != null){
                f.addOcorrences();
                fmap.put(f.getSignature(),f);
                cost = cost + f.getCost();
            }
	}
        
        public void setNVars(int n){
            nVars = n;
        }
	
	/*//adiciona uma ocorrencia na iesima função do histograma
	public void addOcorrence(int i){
            if ((i < fmap.size()) && (i >= 0)){
                fmap.get(i).addOcorrences();
                cost = cost + fmap.get(i).getCost();
            }
	}*/

        //adiciona uma ocorrencia na funcção f do histograma
	public void addOcorrence(FunctionData f){
            if (f != null){
                f.addOcorrences();
                cost = cost + f.getCost();
            }
	}

        public Iterator<FunctionData> getFunctionsList(){
            return fmap.values().iterator();
        }

        public int getCost(){
            return cost;
        }

        public int size(){
            return fmap.size();
        }

        /*
         * //escreve o histograma em um arquivo
        public void write(String arq) throws Exception{
            File f = new File(arq);
            f.delete();
            String out = new String();
            for (int i = 0; i < fmap.size(); i++){
                FunctionData function = fmap.get(i);
                out = out + "."+function.getGateName()+"."+function.getSignature()+"."+function.getCost()+"."+function.getOcorrences()+"."+function.getFunction()+"\n";
            }
            FileWriter fw = new FileWriter(f);
            fw.write(out);
        }*/

        public int getSize(){
            return fmap.size();
        }

        public FunctionData getBySign(String sign){
            return fmap.get(sign);
        }

        public FunctionData getBySign(int sign){
            return fmap.get(Integer.toString(sign));
        }
        
        public void add(String sign, FunctionData f){
            fmap.put(sign, f);
        }

        public void print(){
            System.out.println("Cost: "+cost);
            Iterator<FunctionData> i = fmap.values().iterator();
            while (i.hasNext()){
                i.next().print();
            }
        }
}
