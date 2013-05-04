package aig;

/**
 * Interface que da forma ao Patterns Visitor
 * @author Osvaldo
 */
public interface AigNodeVisitor {

    /**
     * Visitor for an AigNodeAnd
     * @param nodeAigGate The node
     */
    public void visit(NodeAigGate nodeAigGate);

    /**
     * Visitor for an AigNodeOR
     * @param nodeAigGateOr The node
     */
    public void visit(NodeAigGateOr nodeAigGateOr);

    /**
     * Visitor for an AigNodeInput
     * @param nodeAigInput  The Node
     */
    public void visit(NodeAigInput nodeAigInput);

    /**
     * Visitor for an AigNodeOutput
     * @param nodeAigOutput The Node
     */
    public void visit(NodeAigOutput nodeAigOutput);

    /**
     * Visitor for an AigNodeLatch
     * @param nodeAigLatch The Node
     */
    public void visit(NodeAigLatch nodeAigLatch);

    /**
     * Visitor for an AigNodeInverter
     * @param nodeAigInverter The Node
     */
    public void visit(NodeAigInverter nodeAigInverter);
}
