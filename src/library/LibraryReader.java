package library;
import java.util.*;

//executa o parsing da lib e monta a lib na memoria
public class LibraryReader extends Loader{
    
	protected Histogram lib;
        protected int nVars;
	
	public LibraryReader(String filename, int nvars) throws Exception{
		super(filename);
                if (nvars > 7){
                    System.out.println("Numero máximo de variáveis: 7");
                    throw new Exception();
                }
                nVars = nvars;
		lib = new Histogram();
                lib.setNVars(nvars);
		load_to_lib();
	}
        
        public Histogram getLib(){
            return lib;
        }
	
        public static String removeInvalidChars(String str){
            int i = 0;
            while ((str.charAt(i) == ' ') || (str.charAt(i) == '\t')|| (str.charAt(i) == '\n')|| (str.charAt(i) == '\r')|| (str.charAt(i) == '=')) i++;
            return str.substring(i);
        }   
        
	public void load_to_lib(){
            
            StringTokenizer parser = new StringTokenizer(input);
            String line = parser.nextToken("\r\n");
            StringTokenizer lineParser = new StringTokenizer(line);
            String gateName;
            int cost;
            try{
                while (line != null){
                    if (lineParser.nextToken().equals("GATE")){ //se nao for um comentario nem um pino
                        gateName = lineParser.nextToken(" \t\n\r");  //pega o nome da porta logica
                        cost = Integer.parseInt(lineParser.nextToken());    //pega o custo da celula
                        String function = lineParser.nextToken(";");  //pega a função logica da porta
                        function = function.substring(function.indexOf("=")+1);
                        function = removeInvalidChars(function);
                        //System.out.println(function);
                        if (function.equals("CONST0")) lib.add(FunctionData.getSignature("0",nVars), new FunctionData(gateName, "0", cost, FunctionData.getSignature("0",nVars)));
                        else if (function.equals("CONST1")) lib.add(FunctionData.getSignature("1",nVars), new FunctionData(gateName, "1", cost, FunctionData.getSignature("1",nVars)));
                        else lib.add(FunctionData.getSignature(function,nVars), new FunctionData(gateName, function, cost, FunctionData.getSignature(function,nVars)));
                    }
                    
                    try{
                        line = parser.nextToken("\r\n");
                        lineParser = new StringTokenizer(line);
                    }
                    catch(Exception e){
                        line = null;
                    }
                }
            }catch(Exception e){
                System.out.println("aaaaaaaaaaaaaaa"+e.toString());
            }
	}
	
}
