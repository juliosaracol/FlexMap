package FlexMap;

/**
 * Classe que define a função custo do método Area Flow
 * @author Julio Saraçol
 */
public class CostAreaFlow extends CostFunction
{
    public CostAreaFlow(String costFunction) {
        super(costFunction);
    }
    
    //**Método do calculo da função custo do areaflow, Utilizar 0 nos parametros não utlizados(area,delay,consumption)*/
    @Override
    public float eval(float area, float delay, float consumption, float input, float output, float other) 
    {
        if (handleExpression.HandleExpression.checkParentheses(fc)) {
            if (this.validator.validator(fc)){
                return this.interpretFunction.calcFunctionCost(area,delay,consumption,input,output,other); //float area, float delay, float power, float input, float output, float other
            }
            else{
                System.err.println("Error: Identifier or Value");
            }
        }
        else
            System.err.println("Error: Parentheses");
        return -1;
     }
}
