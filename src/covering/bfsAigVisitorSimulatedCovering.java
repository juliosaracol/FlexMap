//package covering;
//
//import aig.*;
//import java.util.*;
//
///**
// *
// * @author Julio Saraçol
// */
//public class bfsAigVisitorSimulatedCovering extends bfsNodeAigVisitor
//{
//
//    protected SimulatedAnneling simulated;
//    protected Map<NodeAig, Set<NodeAig>> selectedCuts;
//    public bfsAigVisitorSimulatedCovering(SimulatedAnneling sa, Map<NodeAig, Set<NodeAig>> selectedCuts) 
//    {
//        super();
//        this.simulated      = sa;
//        this.selectedCuts   = selectedCuts;
//    }
//    
//    @Override
//    public void function(NodeAig nodeAigActual) 
//    {
//        boolean flag =false;
//        if(!selectedCuts.containsKey(nodeAigActual))
//        {                     
//            for(Map.Entry<NodeAig,Set<NodeAig>> entry: selectedCuts.entrySet()) 
//            {
//                if(entry.getValue().contains(nodeAigActual))
//                    flag = true;
//            }
//            if(flag == true)
//              selectedCuts.put(nodeAigActual,simulated.bestCut.get(nodeAigActual));   
//        }
//    }
//}
