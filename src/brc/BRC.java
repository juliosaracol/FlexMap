package brc;

import java.util.ArrayList;

public class BRC {

    private ArrayList<Long> variable; //array para armazenar os inteiros que formarao o BRC
    private int numberBits;

    public BRC(int numberBits) {
        this.variable = new ArrayList<Long>();
        this.numberBits = numberBits;
    }
   
    public void addBRC(long value) {
        this.variable.add(value);
    }
    
    public void setBRC(int pos, long value) {
        this.variable.add(pos, value);
    }

    public long getBRC(int pos) {
        return variable.get(pos);
    }

    public ArrayList<Long> getBRC() {
        return variable;
    }

    public int getNumberBits() {
        return numberBits;
    }

    public void setNumberBits(int numberBits) {
        this.numberBits = numberBits;
    }        
    
    public int sizeBRC(){
        return variable.size();
    }
    
    @Override
    public BRC clone() {
        
        BRC clone = new BRC(this.numberBits);
        
        for(int i=0; i < this.sizeBRC(); i++) {
            clone.setBRC(i, this.getBRC(i));
        }
        
        return clone;
    }
    
}
