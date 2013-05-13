//package aigEditor;
//
//import FlexMap.*;
//import aig.Aig.*;
//import graph.*;
//
////import edu.uci.ics.jung.algorithms.layout.DAGLayout;
////import edu.uci.ics.jung.algorithms.layout.Layout;
////import edu.uci.ics.jung.algorithms.layout.StaticLayout;
////import edu.uci.ics.jung.graph.DirectedSparseGraph;
////import edu.uci.ics.jung.graph.Graph;
////import edu.uci.ics.jung.graph.util.EdgeType;
////import edu.uci.ics.jung.visualization.VisualizationViewer;
////import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
////import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
////import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
////import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
////import edu.uci.ics.jung.visualization.renderers.Renderer;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.EventQueue;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.*;
//import javax.swing.GroupLayout;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JMenu;
//import javax.swing.JMenuBar;
//import javax.swing.JMenuItem;
//import javax.swing.event.AncestorEvent;
//import javax.swing.event.AncestorListener;
////import org.apache.commons.collections15.Factory;
//
//public class AigEditor extends JFrame
//{
//  private Aig myAig;
//  private Graph<NodeAig, EdgeAig> graph;
//  private VisualizationViewer<NodeAig, EdgeAig> vv;
//  private Layout<NodeAig, EdgeAig> layout;
//  private DefaultModalGraphMouse gm;
//  private EditingModalGraphMouse gmE;
//  private JMenu jMenu1;
//  private JMenu jMenu2;
//  private JMenu jMenu3;
//  private JMenu jMenu4;
//  private JMenuBar jMenuBar1;
//  private JMenuItem jMenuItem1;
//  private JMenuItem jMenuItem2;
//  private JMenuItem jMenuItem3;
//  private JMenuItem jMenuItem4;
//  private JMenuItem jMenuItem5;
//  private JMenuItem jMenuItem6;
//  private JMenuItem jMenuItem7;
//  private JMenuItem jMenuItem8;
//
//  public AigEditor()
//  {
//    super("AigEditor");
//
//    initComponents();
//
//    setDefaultCloseOperation(3);
//    setVisible(true);
//  }
//
//  private void loadGraph()
//  {
//    TreeMap listEdge = this.myAig.getEdges();
//    TreeMap listVertex = this.myAig.getVertices();
//
//    this.graph = new DirectedSparseGraph();
//
//    for (int i = 1; i <= listVertex.size(); i++)
//    {
//      this.graph.addVertex((NodeAig)listVertex.get(Integer.valueOf(i)));
//    }
//
//    for (int i = 1; i <= listEdge.size(); i++)
//    {
//      this.graph.addEdge((EdgeAig)listEdge.get(Integer.valueOf(i)), (NodeAig)((Edge)listEdge.get(Integer.valueOf(i))).getVertex1(), (NodeAig)((Edge)listEdge.get(Integer.valueOf(i))).getVertex2(), EdgeType.DIRECTED);
//    }
//  }
//
//  private void plotCanvas()
//  {
//    this.layout = new DAGLayout(this.graph);
//    this.layout.setSize(new Dimension(500, 400));
//
//    this.vv = new VisualizationViewer(this.layout);
//    this.vv.setPreferredSize(new Dimension(500, 400));
//
//    this.vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
//    this.vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
//
//    this.gm = new DefaultModalGraphMouse();
//    this.gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
//
//    this.vv.setGraphMouse(this.gm);
//    this.vv.setBackground(Color.white);
//    this.vv.setBounds(0, 0, 800, 600);
//    getContentPane().add(this.vv);
//  }
//
//  private void newCanvas()
//  {
//    this.graph = new DirectedSparseGraph();
//
//    this.layout = new StaticLayout(this.graph);
//    this.layout.setSize(new Dimension(800, 600));
//
//    this.vv = new VisualizationViewer(this.layout);
//    this.vv.setPreferredSize(new Dimension(800, 600));
//
//    Factory vertexFactory = new Factory()
//    {
//            @Override
//      public Vertex create()
//      {
//        return new NodeAigInput(1, "2");
//      }
//    };
//    Factory edgeFactory = new Factory()
//    {
//            @Override
//      public Edge create()
//      {
//        Vertex aux = null;
//        return new Edge(1, aux, aux);
//      }
//    };
//    this.gmE = new EditingModalGraphMouse(this.vv.getRenderContext(), vertexFactory, edgeFactory);
//
//    this.vv.setGraphMouse(this.gmE);
//    this.vv.setBackground(Color.white);
//    this.vv.setBounds(0, 0, 800, 600);
//    getContentPane().add(this.vv);
//  }
//
//  private void initComponents()
//  {
//    this.jMenuBar1 = new JMenuBar();
//    this.jMenu1 = new JMenu();
//    this.jMenuItem1 = new JMenuItem();
//    this.jMenuItem2 = new JMenuItem();
//    this.jMenuItem3 = new JMenuItem();
//    this.jMenuItem4 = new JMenuItem();
//    this.jMenu2 = new JMenu();
//    this.jMenuItem5 = new JMenuItem();
//    this.jMenu4 = new JMenu();
//    this.jMenuItem6 = new JMenuItem();
//    this.jMenuItem7 = new JMenuItem();
//    this.jMenuItem8 = new JMenuItem();
//    this.jMenu3 = new JMenu();
//
//    setDefaultCloseOperation(3);
//    setTitle("AigEditor");
//    setResizable(false);
//
//    this.jMenu1.setText("File");
//
//    this.jMenuItem1.setText("New AIG");
//    this.jMenuItem1.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent evt) {
//        AigEditor.this.jMenuItem1ActionPerformed(evt);
//      }
//    });
//    this.jMenuItem1.addAncestorListener(new AncestorListener() {
//      public void ancestorMoved(AncestorEvent evt) {
//      }
//      public void ancestorAdded(AncestorEvent evt) {
//        AigEditor.this.jMenuItem1AncestorAdded(evt);
//      }
//
//      public void ancestorRemoved(AncestorEvent evt)
//      {
//      }
//    });
//    this.jMenu1.add(this.jMenuItem1);
//
//    this.jMenuItem2.setText("Open AIG");
//    this.jMenuItem2.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent evt) {
//        AigEditor.this.jMenuItem2ActionPerformed(evt);
//      }
//    });
//    this.jMenu1.add(this.jMenuItem2);
//
//    this.jMenuItem3.setText("Save AIG");
//    this.jMenu1.add(this.jMenuItem3);
//
//    this.jMenuItem4.setText("Exit");
//    this.jMenuItem4.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent evt) {
//        AigEditor.this.jMenuItem4ActionPerformed(evt);
//      }
//    });
//    this.jMenu1.add(this.jMenuItem4);
//
//    this.jMenuBar1.add(this.jMenu1);
//
//    this.jMenu2.setText("Edit");
//
//    this.jMenuItem5.setText("Compile Graph");
//    this.jMenu2.add(this.jMenuItem5);
//
//    this.jMenu4.setText("Mode");
//
//    this.jMenuItem6.setText("Transforming");
//    this.jMenuItem6.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent evt) {
//        AigEditor.this.jMenuItem6ActionPerformed(evt);
//      }
//    });
//    this.jMenu4.add(this.jMenuItem6);
//
//    this.jMenuItem7.setText("Picking");
//    this.jMenuItem7.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent evt) {
//        AigEditor.this.jMenuItem7ActionPerformed(evt);
//      }
//    });
//    this.jMenu4.add(this.jMenuItem7);
//
//    this.jMenuItem8.setText("Editing");
//    this.jMenuItem8.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent evt) {
//        AigEditor.this.jMenuItem8ActionPerformed(evt);
//      }
//    });
//    this.jMenu4.add(this.jMenuItem8);
//
//    this.jMenu2.add(this.jMenu4);
//
//    this.jMenuBar1.add(this.jMenu2);
//
//    this.jMenu3.setText("About");
//    this.jMenuBar1.add(this.jMenu3);
//
//    setJMenuBar(this.jMenuBar1);
//
//    GroupLayout layout = new GroupLayout(getContentPane());
//    getContentPane().setLayout(layout);
//    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 800, 32767));
//
//    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 579, 32767));
//
//    pack();
//  }
//
//  private void jMenuItem4ActionPerformed(ActionEvent evt) {
//    System.exit(1);
//  }
//
//  private void jMenuItem2ActionPerformed(ActionEvent evt)
//  {
//    if (this.vv != null)
//    {
//      remove(this.vv);
//      this.myAig = null;
//      this.graph = null;
//      this.vv = null;
//      this.gm = null;
//    }
//
//    JFileChooser fileC = new JFileChooser();
//    fileC.showOpenDialog(this);
//    String path = fileC.getSelectedFile().getName();
//    this.myAig = new Aig(path);
//    loadGraph();
//    plotCanvas();
//  }
//
//  private void jMenuItem6ActionPerformed(ActionEvent evt)
//  {
//    if (this.gm != null)
//    {
//      this.gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
//    }
//    else
//    {
//      remove(this.vv);
//      this.gm = new DefaultModalGraphMouse();
//      this.gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
//
//      this.vv.setGraphMouse(this.gm);
//      this.vv.setBackground(Color.white);
//      this.vv.setBounds(0, 0, 800, 600);
//      getContentPane().add(this.vv);
//    }
//  }
//
//  private void jMenuItem7ActionPerformed(ActionEvent evt)
//  {
//    if (this.gm != null)
//    {
//      this.gm.setMode(ModalGraphMouse.Mode.PICKING);
//    }
//    else {
//      remove(this.vv);
//      this.gm = new DefaultModalGraphMouse();
//      this.gm.setMode(ModalGraphMouse.Mode.PICKING);
//
//      this.vv.setGraphMouse(this.gm);
//      this.vv.setBackground(Color.white);
//      this.vv.setBounds(0, 0, 800, 600);
//      getContentPane().add(this.vv);
//    }
//  }
//
//  private void jMenuItem1ActionPerformed(ActionEvent evt) {
//    if (this.gmE == null)
//    {
//      newCanvas();
//    }
//    else {
//      remove(this.vv);
//      this.myAig = null;
//      this.graph = null;
//      this.vv = null;
//      this.gm = null;
//      newCanvas();
//    }
//  }
//
//  private void jMenuItem1AncestorAdded(AncestorEvent evt)
//  {
//  }
//
//  private void jMenuItem8ActionPerformed(ActionEvent evt)
//  {
//    remove(this.vv);
//    this.gm = null;
//
//    Factory vertexFactory = new Factory()
//    {
//            @Override
//      public Vertex create()
//      {
//        return new Vertex(1);
//      }
//    };
//    Factory edgeFactory = new Factory()
//    {
//            @Override
//      public Edge create()
//      {
//        Vertex aux = null;
//        return new Edge(1, aux, aux);
//      }
//    };
//    this.gmE = new EditingModalGraphMouse(this.vv.getRenderContext(), vertexFactory, edgeFactory);
//
//    this.vv.setGraphMouse(this.gmE);
//    this.vv.setBackground(Color.white);
//    this.vv.setBounds(0, 0, 800, 600);
//    getContentPane().add(this.vv);
//  }
//
//  public static void main(String[] args)
//  {
//    EventQueue.invokeLater(new Runnable() {
//            @Override
//      public void run() {
//        new AigEditor().setVisible(true);
//      }
//    });
//  }
//}
