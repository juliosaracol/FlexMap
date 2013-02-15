package FlexMap;

/**
 * Classe que define a função custo do método Area Flow
 * @author Julio Saraçol
 */
public class CostAreaFlow extends CostFunction
{

    public CostAreaFlow(float pArea, float pDelay, float pConsumption, float pInput, float pOutput, float pOther) 
    {
        super(pArea, pDelay, pConsumption, pInput, pOutput, pOther);
    }
    
    //**Método do calculo da função custo do areaflow, Utilizar 0 nos parametros não utlizados(area,delay,consumption)*/
    @Override
    public float eval(float area, float delay, float consumption, float input, float output, float other) 
    {
        float costFinal=0;
        costFinal = ((area*pArea)+(delay*pDelay)+(input*pInput)+(other*pOther))/(output*pOutput);
            return costFinal;
    }
    
}
