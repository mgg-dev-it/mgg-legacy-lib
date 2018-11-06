package hu.mag.swing.tree;

import hu.mag.swing.MagTextField;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.swing.CommonPanel;
import hu.mgx.swing.table.MemoryTable;
import java.awt.GridBagConstraints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author MaG
 */
public class MagTreePanel extends CommonPanel implements KeyListener, TreeSelectionListener {

    private SwingAppInterface swingAppInterface;
    //
    private CommonPanel cpTree = null;
    private JScrollPane jspTree = null;
    //private JViewport jvTree = null;
    private JTree jTree;
    private DefaultMutableTreeNode top;
    private Vector<DefaultMutableTreeNode> vNodes;
    private HashMap<DefaultMutableTreeNode, String> hashmap = new HashMap<DefaultMutableTreeNode, String>();
    //
    private CommonPanel cpFilter = null;
    private JLabel lblFilter = null;
    private MagTextField mtfFilter = null;
    //
    private String sTopText;
    private boolean bRootVisible;
    private MemoryTable mtTreeData;
    //
    private Vector<MagTreeEventListener> vMagTreeEventListeners = new Vector<MagTreeEventListener>();

    public MagTreePanel(SwingAppInterface swingAppInterface) {
        super();
        this.swingAppInterface = swingAppInterface;
        init();
    }

    private void init() {
        this.setBorder(new EmptyBorder(2, 2, 2, 2));
        this.setInsets(0, 0, 0, 0);

        String sXMLConfig = "<?xml version='1.0' encoding='ISO-8859-2'?>";
        sXMLConfig += "<!-- Comment -->";
        sXMLConfig += "<app name='app' major='0' minor='0' revision='0' width='800' height='600'>";
        sXMLConfig += "    <language>";
        sXMLConfig += "        <languageitem key='MagTreePanel Filter'>";
        sXMLConfig += "            <translation lang='hu'>Szûrés</translation>";
        sXMLConfig += "            <translation lang='en'>Filter</translation>";
        sXMLConfig += "            <translation lang='de'>Filter</translation>";
        sXMLConfig += "        </languageitem>";
        sXMLConfig += "    </language>";
        sXMLConfig += "</app>";
        swingAppInterface.addLanguageXML(sXMLConfig, "ISO-8859-2");

        cpFilter = new CommonPanel();
        cpFilter.setBorder(new EmptyBorder(0, 0, 0, 0));
        cpFilter.setInsets(0, 0, 0, 0);

        lblFilter = new JLabel(swingAppInterface.getLanguageString("MagTreePanel Filter") + ": ");
        cpFilter.addToCurrentRow(lblFilter, 1, 1, 0, 0);

        mtfFilter = new MagTextField(swingAppInterface);
        mtfFilter.setMaxLength(999);
        mtfFilter.setValue(new String(""));
        mtfFilter.addKeyListener(this);
        cpFilter.addToCurrentRow(mtfFilter, 1, 1, 0, 1.0);

        this.addToGrid(cpFilter, 0, 0, 1, 1, 0, 0, GridBagConstraints.BOTH);

        cpTree = new CommonPanel();
        cpTree.setBorder(new EmptyBorder(0, 0, 0, 0));
        cpTree.setInsets(0, 0, 0, 0);
        sTopText = "";
        top = new DefaultMutableTreeNode(sTopText);
        jTree = new JTree(top);
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTree.addTreeSelectionListener(this);
        bRootVisible = jTree.isRootVisible();
        jspTree = new JScrollPane(jTree);
        cpTree.addToCurrentRow(jspTree, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        this.addToGrid(cpTree, 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        vNodes = new Vector<DefaultMutableTreeNode>();
        vNodes.add(top);
        hashmap = new HashMap<DefaultMutableTreeNode, String>();
        hashmap.put(top, "top");
        mtTreeData = new MemoryTable(new String[]{"text", "identifier", "level", "haschild", "isvisible", "rowinview"});
    }

    public void createNewTree(String sText) {
        createNewTree(sText, true);
    }

    private void createNewTree(String sText, boolean bDeleteData) {
        sTopText = sText;
        top = new DefaultMutableTreeNode(sTopText);
        jTree = new JTree(top);
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTree.addTreeSelectionListener(this);
        //jTree.setRootVisible(bRootVisible);
        jspTree.setViewportView(jTree);
        vNodes = new Vector<DefaultMutableTreeNode>();
        vNodes.add(top);
        hashmap = new HashMap<DefaultMutableTreeNode, String>();
        hashmap.put(top, "top");
        if (bDeleteData) {
            mtTreeData = new MemoryTable(new String[]{"text", "identifier", "level", "haschild", "isvisible", "rowinview"});
        }
    }

    public void addTreeElement(String sText, String sItemIdentifier, int iLevel, boolean bHasChild) {
        addTreeElement(sText, sItemIdentifier, iLevel, bHasChild, true);
    }

    private void addTreeElement(String sText, String sItemIdentifier, int iLevel, boolean bHasChild, boolean bAddData) {
        if (vNodes.size() < (iLevel + 1)) {
            return;
        }
        DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(sText);
        vNodes.elementAt(iLevel).add(dmtn);
        if (vNodes.size() == (iLevel + 1)) {
            vNodes.add(dmtn);
        } else {
            vNodes.setElementAt(dmtn, (iLevel + 1));
        }
        hashmap.put(dmtn, sItemIdentifier);
        if (bAddData) {
            mtTreeData.addRow(sText, sItemIdentifier, new Integer(iLevel), new Integer(bHasChild ? 1 : 0), new Integer(1), new Integer(mtTreeData.getRowCount()));
        }
    }

    public void openAll() {
        for (int iRow = 0; iRow < jTree.getRowCount(); iRow++) {
            jTree.expandRow(iRow);
        }
    }

    public void setRootVisible(boolean rootVisible) {
        jTree.setRootVisible(rootVisible);
        bRootVisible = rootVisible;
    }

    public static MagTreePanel createEmptyMagTreePanel(SwingAppInterface swingAppInterface) {
        MagTreePanel magTreePanel = new MagTreePanel(swingAppInterface);
        return (magTreePanel);
    }

    private void doFilter() {
        for (int iRow = 0; iRow < mtTreeData.getRowCount(); iRow++) {
            if (mtTreeData.getIntValueAt(iRow, "haschild") == 0 && mtTreeData.getStringValueAt(iRow, "text").toLowerCase().contains(mtfFilter.getStringValue().toLowerCase())) {
                mtTreeData.setValueAt(new Integer(1), iRow, "isvisible");
            } else {
                mtTreeData.setValueAt(new Integer(0), iRow, "isvisible");
            }
        }
        for (int iRow = 0; iRow < mtTreeData.getRowCount(); iRow++) {
            if (mtTreeData.getIntValueAt(iRow, "isvisible") == 1) {
                int iLevel = mtTreeData.getIntValueAt(iRow, "level");
                boolean bOK = false;
                for (int i = iRow - 1; !bOK && i >= 0; i--) {
                    if (mtTreeData.getIntValueAt(i, "level") < iLevel) {
                        if (mtTreeData.getIntValueAt(i, "isvisible") == 1) {
                            bOK = true;
                        } else {
                            mtTreeData.setValueAt(new Integer(1), i, "isvisible");
                            --iLevel;
                        }
                    }
                }
            }
        }
        doRebuild();
        openAll();
        jTree.setRootVisible(bRootVisible);
    }

    private void doRebuild() {
        int iVisibleRow = 0;
        createNewTree(sTopText, false);
        for (int iRow = 0; iRow < mtTreeData.getRowCount(); iRow++) {
            if (mtTreeData.getIntValueAt(iRow, "isvisible") == 1) {
                addTreeElement(mtTreeData.getStringValueAt(iRow, "text"), mtTreeData.getStringValueAt(iRow, "identifier"), mtTreeData.getIntValueAt(iRow, "level"), mtTreeData.getIntValueAt(iRow, "haschild") == 1, false);
                mtTreeData.setValueAt(++iVisibleRow, iRow, "rowinview");
            } else {
                mtTreeData.setValueAt(-1, iRow, "rowinview");
            }
        }
    }

    public void addMagTreeEventListener(MagTreeEventListener mtel) {
        for (int i = 0; i < vMagTreeEventListeners.size(); i++) {
            if (vMagTreeEventListeners.elementAt(i).equals(mtel)) {
                return;
            }
        }
        vMagTreeEventListeners.add(mtel);
    }

    private void fireMagTreeEventSelected(String sItemIdentifier) {
        for (int i = 0; i < vMagTreeEventListeners.size(); i++) {
            MagTreeEvent mtre = new MagTreeEvent(this, MagTreeEvent.ITEM_SELECTED, sItemIdentifier);
            vMagTreeEventListeners.elementAt(i).treeEventPerformed(mtre);
        }
    }

    //KeyListener
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == mtfFilter) {
            //doFilter();
        }
    }

    //KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == mtfFilter) {
            //doFilter();
        }
    }

    //KeyListener
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == mtfFilter) {
            doFilter();
        }
    }

    //TreeSelectionListener
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
        if (dmtn == null) {
            return;
        }
        String sItemIdentifier = hashmap.get(dmtn);
        fireMagTreeEventSelected(sItemIdentifier);
        //System.out.println("TreeSelectionEvent " + dmtn.toString() + " ID=" + StringUtils.isNull(sIdentifier, "NULL"));
    }

    public void setReadOnly(boolean bReadOnly) {
        //this.bReadOnly = bReadOnly;
        jTree.setEditable(!bReadOnly);
        jTree.setFocusable(!bReadOnly);
//        if (bReadOnly) {
//            setBackground(ColorManager.inputBackgroundDisabled());
//        } else {
//            setBackground(ColorManager.inputBackgroundFocusLost());
//        }
    }

}
