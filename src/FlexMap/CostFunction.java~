package FlexMap;

import java.util.ArrayList;

/**
 * Classe que da forma a função custo utilizada nos algoritmos de mapeamento
 * @author Julio Saraçol
 */
public abstract class CostFunction 
{
        protected  float pArea;
        protected  float pDelay;
        protected  float pConsumption;
        protected  float pInput;
        protected  float pOutput;
        protected  float pOther;

    /**Construtor da função custo, necessário parametros de configuração e imnplementação do método eval();
     * @param pArea
     * @param pDelay
     * @param pConsumption
     * @param pInput
     * @param pOutput
     * @param pOther 
     */
    public CostFunction(float pArea, float pDelay,float pConsumption,float pInput,float pOutput,float pOther)
    {
        this.pArea          =pArea;
        this.pDelay         =pDelay;
        this.pConsumption   =pConsumption;
        this.pInput         =pInput;
        this.pOutput        =pOutput;
        this.pOther         =pOther;
    }
    
    //**Método que aplica a avaliação da função custo definida na classe filha (weight)X(cost)*/
    public abstract float eval(float area, float delay,float consumption,float input,float output,float other);
}
