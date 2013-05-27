package FlexMap;

import interpretfunction.*;
import java.util.ArrayList;

/**
 * Classe que da forma a função custo utilizada nos algoritmos de mapeamento
 * @author Julio Saraçol
 */
public abstract class CostFunction 
{
    protected InterpretFunction interpretFunction ;          
    protected String fc;
    protected ValidatorCostFunction validator;
    
    public CostFunction(String costFunction)
    {
       this.fc                  = costFunction;
       this.interpretFunction   = new InterpretFunction(fc);
       this.validator           = new ValidatorCostFunction();
   }
    
    //**Método que aplica a avaliação da função custo definida na classe filha (weight)X(cost)*/
    public abstract float eval(float area, float delay,float consumption,float input,float output,float other);
}
