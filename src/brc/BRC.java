package brc;

import java.util.ArrayList;

public class BRC {

    protected ArrayList<Integer> variable; //array para armazenar os inteiros que formarao o BRC

    public BRC() {
        this.variable = new ArrayList<Integer>();
    }
   
    public void addBRC(int value) {
        this.variable.add(value);
    }
    
    public void setBRC(int pos, int value) {
        this.variable.add(pos, value);
    }

    public int getBRC(int pos) {
        return variable.get(pos);
    }

    public ArrayList<Integer> getBRC() {
        return variable;
    }
    
    public int sizeBRC(){
        return variable.size();
    }
    
    @Override
    public BRC clone() {
        
        BRC clone = new BRC();
        
        for(int i=0; i < this.sizeBRC(); i++) {
            clone.setBRC(i, this.getBRC(i));
        }
        
        return clone;
    }
}
