package aig;

/**
 * Interface que da forma ao Patterns Visitor
 * @author Osvaldo
 */
public interface AigNodeVisitor {

    /**
     * Visitor for an AigNodeAnd
     * @param aigNodeAnd    The node
     */
    public void visit(NodeAigGate nodeAigGate);

    /**
     * Visitor for an AigNodeOR
     * @param aigNodeAnd    The node
     */
    public void visit(NodeAigGateOr nodeAigGateOr);

    /**
     * Visitor for an AigNodeInput
     * @param aigNodeInput  The Node
     */
    public void visit(NodeAigInput nodeAigInput);

    /**
     * Visitor for an AigNodeOutput
     * @param aigNodeOutput The Node
     */
    public void visit(NodeAigOutput nodeAigOutput);

    /**
     * Visitor for an AigNodeLatch
     * @param aigNodeLatch The Node
     */
    public void visit(NodeAigLatch nodeAigLatch);
}
