package functionsClasses;

import expanderExpression.ExpanderExpression;
import java.util.TreeMap;

public class LowestFunctionFinder {

    /*
     * Recebe uma funcao atraves de uma expressao Booleana e o numero de
     * variaveis da funcao e retorna um BitRepresentation com uma assinatura NP
     */
    public static BitRepresentation run_NP(String expression, int nVariables) {

        expression = ExpanderExpression.runExpanderExpression(expression);
        BitRepresentation function = SetFunction.builFunctionRepresentationCode(expression, nVariables);
        
        int tableSize = (BitHandler.fact(nVariables) * (function.bitLength()));
        long table[][] = Builder.buildTransformationTable_NP(nVariables, tableSize);

        return getLowestFunction(table, tableSize, function, nVariables);
    }

    /*
     * Recebe uma funcao atraves de um BitRepresentation e o numero de variaveis
     * da funcao e retorna um BitRepresentation com uma assinatura NP
     */
    public static BitRepresentation run_NP(BitRepresentation function, int nVariables) {

        int tableSize = (BitHandler.fact(nVariables) * (function.bitLength()));
        long table[][] = Builder.buildTransformationTable_NP(nVariables, tableSize);
        BitRepresentation newFunction = BitHandler.setBitRepresentation(function, nVariables);
        
        return getLowestFunction(table, tableSize, newFunction, nVariables);
    }

    /*
     * Recebe uma funcao atraves de uma expressao Booleana e o numero de
     * variaveis da funcao e retorna um BitRepresentation com uma assinatura P
     */
    public static BitRepresentation run_P(String expression, int nVariables) {

        expression = ExpanderExpression.runExpanderExpression(expression);
        BitRepresentation function = SetFunction.builFunctionRepresentationCode(expression, nVariables);
        
        int tableSize = (BitHandler.fact(nVariables));
        long table[][] = Builder.buildTransformationTable_P(nVariables, tableSize);

        return getLowestFunction(table, tableSize, function, nVariables);
    }

    /*
     * Recebe uma funcao atraves de um BitRepresentation e o numero de variaveis
     * da funcao e retorna um BitRepresentation com uma assinatura P
     */
    public static BitRepresentation run_P(BitRepresentation function, int nVariables) {

        int tableSize = (BitHandler.fact(nVariables));
        long table[][] = Builder.buildTransformationTable_P(nVariables, tableSize);
        BitRepresentation newFunction = BitHandler.setBitRepresentation(function, nVariables);
        
        return getLowestFunction(table, tableSize, newFunction, nVariables);
    }

    /*
     * Realiza a busca pela menor representacao da funcao na tabela de
     * transformacao NP ou P e retorna um BitRepresentation com a menor
     * representacao
     */
    private static BitRepresentation getLowestFunction(long table[][], int tableSize, BitRepresentation function, int nVariables) {

        int nBits = function.bitLength();
        int col, level;

        BitRepresentation lowest = new BitRepresentation(nBits, nVariables);
        BitRepresentation temp;
        TreeMap<Long, Boolean> truthTable = Builder.buildTruthTable(function, nVariables);

        for (level = 0; level < nBits; level++) {
            if (truthTable.get(table[0][level])) {
                lowest.flip(level);
            }
        }

        // inicializa lista
        for (col = 1; col < tableSize; col++) {

            temp = new BitRepresentation(nBits, nVariables);

            for (level = 0; level < nBits; level++) {
                if (truthTable.get(table[col][level])) {
                    temp.flip(level);
                }
            }

            lowest = BitHandler.getLowest(lowest, temp);
        }

        return lowest;
    }
    
}
