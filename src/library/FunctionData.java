package library;
import expanderExpression.*;
import functionsClasses.*;

public class FunctionData{
	private String gateName;
	private String function;
	private int cost;
	private String signature;
	private int ocorrences;
	
	public FunctionData(String gn, String f, int c, String s){
		gateName = gn;
		function = f;
		cost = c;
		signature = s;
		ocorrences = 0;
	}

        public FunctionData(FunctionData f){
		gateName = f.gateName;
		function = f.function;
		cost = f.cost;
		signature = f.signature;
		ocorrences = f.ocorrences;
	}

        public FunctionData clone(){
            return new FunctionData(this);
        }

	// GETER
	public String getGateName(){
		return gateName;
	}
	
	public String getFunction(){
		return function;
	}
	
	public int getCost(){
		return cost;
	}
	
	public String getSignature(){
		return signature;
	}
	
	public int getOcorrences(){
		return ocorrences;
	}
	
	//SETER
	
	public void setGateName(String s){
		gateName = s;
	}
	
	public void setFunction(String s){
		function = s;
	}
	
	public void setCost(int s){
		cost = s;
	}
	
	public void setSignature(String s){
		signature = s;
	}
	
	public void setOcorrences(int s){
		ocorrences = s;
	}
	
	//
	
	public void addOcorrences(){
		ocorrences++;
	}

        public void print(){
            System.out.println("Gate Name: "+gateName+"\tOcorrences: "+ocorrences+"\tCost: "+cost+"\tSignature: "+signature+"\tFunction: \""+function+"\"");
        }

        public static String getSignature(String f, int nvars)throws Exception{
            if (f.equals("0")) return "0";
            if (f.equals("1")) return "255";
            BitRepresentation br = LowestFunctionFinder.run_P(ExpanderExpression.runExpanderExpression(f),nvars);
            return Integer.toString(br.toInteger().get(0));
	}
}
