package functionsClasses;

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
    public static BitRepresentation brcToBitRepresentation(ArrayList<Integer> brc) {

        int nBits = brc.size() * 32;
        int nVariables = getVariablesNumber(nBits);

        String bitArray = getBitArray(brc);

        BitRepresentation function = new BitRepresentation(nBits, nVariables);

        for (int i = 0; i < bitArray.length(); i++) {
            if (bitArray.charAt(i) == '1') {
                function.flip(i);
            }
        }

        return function;
    }

    /*
     * Encontra o numero de variaveis da funcao atraves do numero de bits
     */
    private static int getVariablesNumber(int nBits) {

        int count = 1;

        while (nBits != 2) {
            nBits = nBits / 2;
            count++;
        }

        return count;
    }

    /*
     * Para cada Integer do ArrayList do BRC Gera uma String de 32 Bits e as
     * concatena
     */
    private static String getBitArray(ArrayList<Integer> brc) {

        String bitArray;
        String leftZeros;
        String function = "";
        String reverse;
        int length;

        for (int i = brc.size() - 1; i >= 0; i--) {

            leftZeros = "";

            bitArray = Integer.toBinaryString(brc.get(i));
            length = 32 - bitArray.length();

            for (int j = 0; j < length; j++) {
                leftZeros += "0";
            }

            reverse = new StringBuffer(bitArray).reverse().toString();

            function += reverse + leftZeros;

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
    public static void displayIntegers(BitRepresentation a) {
        for (Integer i : a.toInteger()) {
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
        for (int i = a.bitLength() - 1; i >= 0; i--) {
            if (a.get(i)) {
                System.out.print("1");
            } else {
                System.out.print("0");
            }
        }
        System.out.println();
    }
}