package aig;

import FlexMap.Algorithms;
import javax.swing.text.html.HTMLEditorKit.Parser;

/**
 * Classe que implementa o caminhamento para explicitar inversores
 * @author Julio Saraçol
 */
public class dfsAigToAigInverter extends dfsNodeAigVisitor
{

    private Aig myaig;
    public dfsAigToAigInverter(Aig myAig) 
    {
        this.myaig = myAig;
    }
    
    @Override
    public void function(NodeAig nodeAigActual){
        if((nodeAigActual.isOutput())&&(nodeAigActual.getParents().size()==1)){
            Algorithms.getEdge(nodeAigActual, nodeAigActual.getParents().get(0)).edgeInverter();
            return;
        }
        if(nodeAigActual.isInput())
            return;
        NodeAig newInverterNode = null;
        for(NodeAig father: nodeAigActual.getParents())
        {
            if(Algorithms.isInverter(nodeAigActual, father)){
                this.myaig.removeEdge(Algorithms.getEdge(nodeAigActual, father).getId());
                String name = String.valueOf(Integer.valueOf(father.getName())+1);
                if(this.myaig.getVertexName(name) == null)
                {
                   newInverterNode = new NodeAigInverter(this.myaig.getVerticesCount(),name);
                   this.myaig.indexNames.put(name,this.myaig.getVerticesCount());
                   this.myaig.getAllNodesAig().add(newInverterNode);
                   this.myaig.getVertices().put(newInverterNode.getId(), newInverterNode);
                   this.myaig.inc_verticesCount();
                }
                else
                   newInverterNode = this.myaig.getVertexName(name);
                this.myaig.addEdge(newInverterNode.getName(),father.getName(),false);
                this.myaig.addEdge(nodeAigActual.getName(),newInverterNode.getName(),false);
            }               
         } 
    }
    
}
