package functionsClasses;

import expanderExpression.ExpressionTokenizer;
import java.util.ArrayList;
import java.util.TreeMap;

public class SetFunction {

    private ExpressionTokenizer parser;
    private int nVariables;
    private TreeMap<String, BitRepresentation> basicRepresentationCodesMap;
    private ArrayList<BitRepresentation> basicRepresentarionCodes;
    private ArrayList<ArrayList<String>> cubes;
    private ArrayList<BitRepresentation> cubesRepresentationCodes;
    private BitRepresentation function;

    public SetFunction(String function, int nVariables) {

        this.parser = new ExpressionTokenizer(function);
        this.cubes = parser.tokenizer();
        this.nVariables = nVariables;
        this.basicRepresentationCodesMap = new TreeMap<String, BitRepresentation>();
        this.basicRepresentarionCodes = Builder.buildBasicRepresentationCodes(nVariables);
        this.cubesRepresentationCodes = new ArrayList<BitRepresentation>();
        this.function = builFunctionRepresentationCode();
    }

    /*
     * Calcula o codigo de representaco da funcao
     */
    private BitRepresentation builFunctionRepresentationCode() {

        BitRepresentation first_code;
        BitRepresentation second_code;

        String variable;
        BitRepresentation tempCode;
        for (int i = 0; i < parser.getVariablesSet().size(); i++) {
            variable = parser.getVariablesSet().get(i);
            tempCode = basicRepresentarionCodes.get(i);
            basicRepresentationCodesMap.put(variable, tempCode);
        }

        // Cria a assinatura de cada produto
        for (ArrayList<String> cube : cubes) {
            for (String literal : cube) {
                if (!basicRepresentationCodesMap.containsKey(literal)) {
                    if (literal.charAt(0) == '!') {
                        BitRepresentation directCode = basicRepresentationCodesMap.get(literal.substring(1));
                        BitRepresentation invCode = directCode.clone();
                        BitHandler.not(invCode);
                        basicRepresentationCodesMap.put(literal, invCode);
                    }
                }
            }
            this.buildCubesRepresentationCode(cube);
        }

        // Calcula a assinatura da funcao
        first_code = cubesRepresentationCodes.get(0);
        for (int i = 1; i < cubesRepresentationCodes.size(); i++) {
            second_code = cubesRepresentationCodes.get(i);
            first_code = BitHandler.or(first_code, second_code);
        }

        return first_code;
    }

    /*
     * Cria o codigo de representacao de um cubo e salva em um array
     */
    private void buildCubesRepresentationCode(ArrayList<String> cube) {

        BitRepresentation first_code = basicRepresentationCodesMap.get(cube.get(0));
        BitRepresentation second_code;
        for (int i = 1; i < cube.size(); i++) {
            second_code = basicRepresentationCodesMap.get(cube.get(i));
            first_code = BitHandler.and(first_code, second_code);
        }

        cubesRepresentationCodes.add(first_code);

    }

    //////////////////////// GETTERS AND SETTERS /////////////////////////////
    public ArrayList<BitRepresentation> getBasicSignatures() {
        return basicRepresentarionCodes;
    }

    public void setBasicSignatures(ArrayList<BitRepresentation> basicSignatures) {
        this.basicRepresentarionCodes = basicSignatures;
    }

    public TreeMap<String, BitRepresentation> getBasicSignaturesMap() {
        return basicRepresentationCodesMap;
    }

    public void setBasicSignaturesMap(TreeMap<String, BitRepresentation> basicSignaturesMap) {
        this.basicRepresentationCodesMap = basicSignaturesMap;
    }

    public BitRepresentation getFunctionSignature() {
        return function;
    }

    public void setFunctionSignature(BitRepresentation functionSignature) {
        this.function = functionSignature;
    }

    public int getnVariables() {
        return nVariables;
    }

    public void setnVariables(int nVariables) {
        this.nVariables = nVariables;
    }

    public ExpressionTokenizer getParser() {
        return parser;
    }

    public void setParser(ExpressionTokenizer parser) {
        this.parser = parser;
    }

    public ArrayList<ArrayList<String>> getProducts() {
        return cubes;
    }

    public void setProducts(ArrayList<ArrayList<String>> products) {
        this.cubes = products;
    }

    public ArrayList<BitRepresentation> getProductsSignatures() {
        return cubesRepresentationCodes;
    }

    public void setProductsSignatures(ArrayList<BitRepresentation> productsSignatures) {
        this.cubesRepresentationCodes = productsSignatures;
    }
}
