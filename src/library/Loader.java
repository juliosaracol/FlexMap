package library;
import java.io.*;

public abstract class Loader {
    protected String input;
    protected File f;
    protected BufferedReader stream;

    public Loader(String fname) throws Exception{
        f = new File(fname);
        stream = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        input = new String();
        load_to_mem();
    }

    //carrega o arquivo para a memoria
    public void load_to_mem(){
        String s;
        try{
            while((s = stream.readLine()) != null){
                input = input + s+ "\n";
            }
        }
        catch(Exception e){
            System.out.println("Erro ao ler do arquivo: "+e.toString());
        }
    }
}
