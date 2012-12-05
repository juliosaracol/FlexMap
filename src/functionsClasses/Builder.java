package functionsClasses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class Builder {

    public Builder(int nVariables, int tableSize) {
    }

    public static ArrayList<BitRepresentation> buildBasicRepresentationCodes(int nVariables) {

        int nBits = (int) Math.pow(2, nVariables);
        int pos;
        int exp = 0;
        int iteration = nVariables - 1;

        ArrayList<BitRepresentation> arrayBitRepresentation = new ArrayList<BitRepresentation>();

        for (int variable = 0; variable < nVariables; variable++) {
            BitRepresentation bitrepresentation = new BitRepresentation(nBits, nVariables);
            arrayBitRepresentation.add(bitrepresentation);
            pos = 0;

            for (int i = 0; i < Math.pow(2, exp); i++) {

                
                // Coloca zero no bitrepresentation
                for (int j = 0; j < Math.pow(2, iteration); j++) {
                    pos++;
                }

                // Coloca um no bitrepresentation
                for (int j = 0; j < Math.pow(2, iteration); j++) {
                    bitrepresentation.flip(pos);
                    pos++;
                }
            }

            exp++;
            iteration--;
        }

        return arrayBitRepresentation;
    }

    /*
     * Constroi uma assinatura para cada linha da tabela verdade
     */
    public static int[] buildEntrys(ArrayList<BitRepresentation> signatures, int nVariables, int nBits) {

        int rows[] = new int[nBits];
        BitRepresentation row;

        // 2^n linhas
        for (int i = 0; i < nBits; i++) {
            row = new BitRepresentation(nVariables, nVariables);
            // n variaveis
            for (int j = 0; j < nVariables; j++) {
                if (signatures.get(j).get(i)) {
                    row.flip(j);
                }
            }

            rows[i] = row.toInteger().get(0);
        }

        return rows;
    }

    public static TreeMap<Integer, Boolean> buildTruthTable(BitRepresentation function, int nVariables) {

        int nBits = (int) Math.pow(2, nVariables);
        TreeMap<Integer, Boolean> truthTable = new TreeMap<Integer, Boolean>();

        for (int i = 0; i < nBits; i++) {
            truthTable.put(i, function.get(i));
        }

        return truthTable;
    }

    /**
     * ********************** BUILD TRANSFORMATION TABLES *********************
     */
    public static int[][] buildTransformationTable_NP(int nVariables, int tableSize) {

        int nBits = (int) Math.pow(2, nVariables);
        int table[][] = new int[tableSize][nBits];

        int p = 0; // Combinações p a p
        ArrayList<ArrayList<BitRepresentation>> permutations = new ArrayList<ArrayList<BitRepresentation>>();
        ArrayList<BitRepresentation> currentPermut;
        ArrayList<ArrayList<Integer>> combinations;
        int element[];
        int index = 0;
        Iterator permut = PermutationGenerator.getPermutations(buildBasicRepresentationCodes(nVariables));

        // Grava as permutacoes em um array
        while (permut.hasNext()) {
            permutations.add((ArrayList<BitRepresentation>) permut.next());
        }

        while (p <= nVariables) {

            combinations = CombinationGenerator.getCombinations(nVariables, p);

            for (ArrayList<Integer> comb : combinations) {

                for (int i = 0; i < permutations.size(); i++) {

                    currentPermut = BitHandler.cloneBitRepresentationArray(permutations.get(i));

                    for (Integer idx : comb) {
                        BitHandler.not(currentPermut.get(idx));
                    }

                    element = buildEntrys(currentPermut, nVariables, nBits);
                    table[index] = element;
                    index++;

                    if (index == tableSize) {
                        return table;
                    }
                }
            }
            p++;
        }

        return table;
    }

    public static int[][] buildTransformationTable_P(int nVariables, int tableSize) {

        int nBits = (int) Math.pow(2, nVariables);
        int table[][] = new int[tableSize][nBits];

        ArrayList<ArrayList<BitRepresentation>> permutations = new ArrayList<ArrayList<BitRepresentation>>();
        ArrayList<BitRepresentation> currentPermut;
        int element[];
        int index = 0;
        Iterator permut = PermutationGenerator.getPermutations(buildBasicRepresentationCodes(nVariables));

        // Grava as permutacoes em um array
        while (permut.hasNext()) {
            permutations.add((ArrayList<BitRepresentation>) permut.next());
        }

        for (int i = 0; i < permutations.size(); i++) {

            currentPermut = BitHandler.cloneBitRepresentationArray(permutations.get(i));

            element = buildEntrys(currentPermut, nVariables, nBits);
            table[index] = element;
            index++;

            if (index == tableSize) {
                return table;
            }
        }

        return table;
    }

    public static void printTable(int table[][]) {
        System.out.println();
        for (int[] element : table) {
            for (int num : element) {
                System.out.print(num + " ");
            }
            System.out.print("\n");
        }
    }

    public static void printTableReverse(int table[][]) {

        for (int[] element : table) {
            for (int i = element.length - 1; i >= 0; i--) {
                System.out.print(element[i] + " ");
            }
            System.out.print("\n");
        }
    }
}
