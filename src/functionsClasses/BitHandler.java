package functionsClasses;

import brc.BRC;
import brc.BRCHandler;
import java.util.ArrayList;

public class BitHandler {
    
    public BitHandler() {
        
    }

    /*
     * Nega a funcao implementada pelo BitRepresentation
     */
    public static BitRepresentation not(BitRepresentation a) {

        for (int i = 0; i < a.bitLength(); i++) {
            a.flip(i);
        }

        return a;
    }

    /*
     * Realiza uma operaco AND entre os dois BitRepresentations
     */
    public static BitRepresentation and(BitRepresentation a, BitRepresentation b) {

        BitRepresentation c = new BitRepresentation(a.bitLength(), a.getnVariables());

        for (int i = 0; i < a.bitLength(); i++) {
            if (a.get(i) && b.get(i)) {
                c.flip(i);
            }
        }

        return c;

    }

    /*
     * Realiza uma operaco OR entre os dois BitRepresentations
     */
    public static BitRepresentation or(BitRepresentation a, BitRepresentation b) {

        BitRepresentation c = new BitRepresentation(a.bitLength(), a.getnVariables());

        for (int i = 0; i < a.bitLength(); i++) {
            if (a.get(i) || b.get(i)) {
                c.flip(i);
            }
        }

        return c;

    }

    /*
     * Retorna a menor valor entre as duas sequencias de bits
     */
    public static BitRepresentation getLowest(BitRepresentation a, BitRepresentation b) {

        for (int i = a.bitLength(); i > 0; i--) {
            if (a.get(i) != b.get(i)) {
                if (a.get(i)) {
                    return b;
                } else {
                    return a;
                }
            }
        }

        // Caso em que sao iguais
        return a;
    }

    /*
     * Clona um ArrayList de BitRepresentaion
     */
    public static ArrayList<BitRepresentation> cloneBitRepresentationArray(ArrayList<BitRepresentation> bitSetList) {

        ArrayList<BitRepresentation> clone = new ArrayList<BitRepresentation>();

        for (BitRepresentation bitSet : bitSetList) {
            clone.add((BitRepresentation) bitSet.clone());
        }

        return clone;
    }

    /*
     * Converte um BRC para um BitRepresentation
     */
    public static BitRepresentation brcToBitRepresentation(BRC cutBRC, int nVariables) {
        
        int nBits = (int) Math.pow(2, nVariables);
        
        // Converte o BRC para String Binaria
        String BinaryString = BRCHandler.toBinaryString(cutBRC);
        
        BitRepresentation function = new BitRepresentation(nBits, nVariables);
        int index = 0;
        
        for (int i = (BinaryString.length()-nBits); i < BinaryString.length(); i++) {
            if (BinaryString.charAt(i) == '1') {
                function.flip(index);
            }
            index++;
        }

        return function;
    }


    public static BitRepresentation setBitRepresentation(BitRepresentation bitRepresentation, int nVariable) {
        
        int nBits = (int) Math.pow(2, nVariable);
        BitRepresentation newBitRepresentation = new BitRepresentation(nBits, nVariable);
        
        for(int i=0; i < nBits; i++) {
            if(bitRepresentation.get(i)) {
                newBitRepresentation.flip(i);
            }
        }
        
        return newBitRepresentation;
    }
    
    /*
     * Calcula o fatorial de n
     */
    public static int fact(int n) {

        if (n <= 1) {
            return 1;
        } else {
            return n * fact(n - 1);
        }
    }

    /*
     * Mostra a assimatura em numero(s)inteiro(s)
     */
    public static void displayLongs(BitRepresentation a) {
        for (Long i : a.toLong()) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    /*
     * Mostra a assimatura em numero(s)hexa(s)
     */
    public static void displayHexa(BitRepresentation a) {
        for (String h : a.toHexa()) {
            System.out.print(h + " ");
        }
        System.out.println();
    }

    /*
     * Mostra a assimatura em binario
     */
    public static void displayBinary(BitRepresentation a) {
        for (int i = 0; i < a.bitLength(); i++) {
            if (a.get(i)) {
                System.out.print("1");
            } else {
                System.out.print("0");
            }
        }
        System.out.println();
    }
    
}