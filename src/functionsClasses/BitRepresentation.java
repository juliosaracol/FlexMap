package functionsClasses;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;

public class BitRepresentation extends BitSet {

    private int bitLength;
    private int nVariables;

    public BitRepresentation(int nBits, int nVariables) {
        super(nBits);
        this.bitLength = nBits;
        this.nVariables = nVariables;
    }

    @Override
    public BitRepresentation clone() {

        BitRepresentation clone = new BitRepresentation(this.bitLength, this.nVariables);
        for (int i = 0; i < this.bitLength; i++) {
            if (this.get(i)) {
                clone.flip(i);
            }
        }

        return clone;
    }

    /*
     * Converte o bitrepresentation para um arrayList de longs
     */
    public ArrayList<Long> toLong() {

        int nLongs, length = 64;
        long temp;
        ArrayList<Long> longs = new ArrayList<Long>();

        if (this.bitLength > 64) {
            nLongs = this.bitLength / 64;
            for (int i = 0; i < nLongs; i++) {
                temp = convert(length, i);
                longs.add(temp);
                length += 64;
            }
        } else {
            temp = convert(this.bitLength, 0);
            longs.add(temp);
        }

        Collections.reverse(longs);

        return longs;
    }

    /*
     * Coverte sequencias de 64 bits pra um long
     */
    private int convert(int length, int index) {

        int exp = 0;
        double number = 0;

        for (int i = index * 64; i < length; i++) {
            if (this.get(i)) {
                number += Math.pow(2, exp);
            }

            exp++;
        }

        return (int) number;
    }

    /*
     * Converte o bitRepresentation para um arrayList de String para representar
     * numeros hexadecimais
     */
    public ArrayList<String> toHexa() {

        ArrayList<Long> Longs = this.toLong();
        ArrayList<String> hexa = new ArrayList<String>();

        for (Long i : Longs) {
            hexa.add(Long.toHexString(i));
        }

        return hexa;
    }

    /*
     * Converte o bitRepresentation para um string com todos os inteiros
     * concatenados na base hexadecimal
     */
    public String toHexaString() {

        ArrayList<Long> longs = this.toLong();
        String hexa = "";

        for (Long i : longs) {
            hexa += Long.toHexString(i);
        }

        return hexa;
    }

    public String toBinaryString() {
        
        String binary = "";
        
        for(int i= 0; i < this.bitLength(); i++) {
            if(this.get(i)) {
                binary += "1";
            }
            else {
                binary += "0";
            }
        }
        
        return binary;
    }
    
    /*
     * Mostra a assimatura em numero(s)inteiro(s)
     */
    public void displayLongs() {
        for (Long i : this.toLong()) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    /*
     * Mostra a assimatura em numero(s)hexa(s)
     */
    public void displayHexa() {
        for (String h : this.toHexa()) {
            System.out.print(h + " ");
        }
        System.out.println();
    }

    /*
     * Mostra a assimatura em binario
     */
    public void displayBinary() {
        for (int i = this.bitLength() - 1; i >= 0; i--) {
            if (this.get(i)) {
                System.out.print("1");
            } else {
                System.out.print("0");
            }
        }
        System.out.println();
    }

    /*
     * Retorna uma String com a concatenacao dos valores hexa da assinatura.
     * Esse codigo pode ser usado para indexar a biblioteca de celulas para 
     * mais 5 variaveis
     */
    public String getHexaCode() {

        ArrayList<String> hexas = this.toHexa();
        String hexaCode = "";

        for (String h : hexas) {
            hexaCode += h;
        }

        return hexaCode;
    }

    public int bitLength() {
        return this.bitLength;
    }

    public int getnVariables() {
        return nVariables;
    }

    public void setnVariables(int nVariables) {
        this.nVariables = nVariables;
    }
}
