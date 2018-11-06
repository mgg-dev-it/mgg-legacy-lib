package hu.mag.swing;

import hu.mgx.swing.CommonPanel;
import hu.mgx.util.StringUtils;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author MaG
 */
public class MagEditorPanePanel extends CommonPanel {

    private JEditorPane jEditorPaneLog;
    private JScrollPane jScrollPaneLog;
    private SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");

    public MagEditorPanePanel() {
        super();
    }

    public void addText(String sText) {
        jEditorPaneLog.setText(jEditorPaneLog.getText() + sText);
    }

    public void addTextLine(String sText) {
        jEditorPaneLog.setText(jEditorPaneLog.getText() + sText + StringUtils.sCrLf);
    }

    public void addTextLineWithTimeStamp(String sText) {
        jEditorPaneLog.setText(jEditorPaneLog.getText() + logDateFormat.format(new java.util.Date()) + " " + sText + StringUtils.sCrLf);
    }

    public static MagEditorPanePanel createLogPanel() {
        return (createLogPanel(null));
    }

    public static MagEditorPanePanel createLogPanel(String sTitledBorder) {
        MagEditorPanePanel mepp = new MagEditorPanePanel();

        mepp.jEditorPaneLog = new JEditorPane();
        mepp.jEditorPaneLog.setEditable(false);
        mepp.jEditorPaneLog.setContentType("text/plain");
        //jEditorPaneLog.setBackground(Color.cyan);
        mepp.jEditorPaneLog.setText("");
        mepp.jEditorPaneLog.setFocusable(false);

        mepp.jScrollPaneLog = new JScrollPane(mepp.jEditorPaneLog);
        mepp.jScrollPaneLog.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mepp.jScrollPaneLog.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        mepp.setBorder(new EmptyBorder(0, 0, 0, 0));
        mepp.setInsets(0, 0, 0, 0);
        if (sTitledBorder != null) {
            mepp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Log"));
        }
        mepp.addToCurrentRow(mepp.jScrollPaneLog, 1, 1, 1, 1);
        return (mepp);
    }
}
