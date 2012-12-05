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
     * Converte o bitrepresentation para um arrayList de inteiros
     */
    public ArrayList<Integer> toInteger() {

        int nIntegers, temp, length = 32;
        ArrayList<Integer> ingeregers = new ArrayList<Integer>();

        if (this.bitLength > 32) {
            nIntegers = this.bitLength / 32;
            for (int i = 0; i < nIntegers; i++) {
                temp = convert(length, i);
                ingeregers.add(temp);
                length += 32;
            }
        } else {
            temp = convert(this.bitLength, 0);
            ingeregers.add(temp);
        }

        Collections.reverse(ingeregers);

        return ingeregers;
    }

    /*
     * Coverte sequencias de 32 bits pra um inteiro
     */
    private int convert(int length, int index) {

        int exp = 0;
        double number = 0;

        for (int i = index * 32; i < length; i++) {
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

        ArrayList<Integer> integers = this.toInteger();
        ArrayList<String> hexa = new ArrayList<String>();

        for (Integer i : integers) {
            hexa.add(Integer.toHexString(i));
        }

        return hexa;
    }

    /*
     * Converte o bitRepresentation para um string com todos os inteiros
     * concatenados na base hexadecimal
     */
    public String toHexaString() {

        ArrayList<Integer> integers = this.toInteger();
        String hexa = "";

        for (Integer i : integers) {
            hexa += Integer.toHexString(i);
        }

        return hexa;
    }

    /*
     * Mostra a assimatura em numero(s)inteiro(s)
     */
    public void displayIntegers() {
        for (Integer i : this.toInteger()) {
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
