package hu.mgx.report;

import hu.mgx.swing.CommonPanel;
import java.awt.*;
import java.awt.event.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.swing.*;
import hu.mgx.app.swing.*;

public class PrinterDialog_notused extends JDialog implements ActionListener, KeyListener {
    
    private CommonPanel mainPane;
    private JButton jButtonYes;
    private JButton jButtonNo;
    private JComboBox jComboBox;
    public static final int YES = 0;
    public static final int NO = 1;
    public static final int ESCAPE = -1;
    private int iAction = ESCAPE;
    
    private PrintService[] services;
    
    public PrinterDialog_notused() {
        super();
        init();
    }
    
    private void init(){
        setModal(true);
        mainPane = new CommonPanel();
        //mainPane.setInsets(10, 10, 10, 10);
        mainPane.setInsets(5, 7, 5, 7);
        setContentPane(mainPane);
        mainPane.addToGrid(jComboBox = new JComboBox(), 0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        //jComboBox.setPreferredSize(new Dimension(200, jComboBox.getHeight()));
        //jLabel.setVerticalAlignment(SwingConstants.CENTER);
        //jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPane.addToGrid(jButtonYes = AppUtils.createButton("Igen","Yes", this), 1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.EAST);
        jButtonYes.setMnemonic(KeyEvent.VK_I);
        jButtonYes.setFocusable(true);
        mainPane.addToGrid(jButtonNo = AppUtils.createButton("Nem","No", this), 1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        jButtonNo.setMnemonic(KeyEvent.VK_N);
        jButtonNo.setFocusable(true);
        //this.getRootPane().setDefaultButton(jButtonNo);
        jButtonYes.addKeyListener(this);
        jButtonNo.addKeyListener(this);
        
        
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        //aset.add(OrientationRequested.LANDSCAPE);
        aset.add(new Copies(1));
        aset.add(new JobName("My job", null));
        //aset.add(MediaSizeName.ISO_A4);
        services = PrintServiceLookup.lookupPrintServices(flavor, aset);
        for (int i=0; i<services.length; i++) {
            jComboBox.addItem(services[i].getName());
        }
        
        pack();
    }
    
    public int showDialog(Component c, String sTitle, String sMsg){
        setTitle(sTitle);
        //jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //jLabel.setText(sMsg);
        //jComboBox.addItem("111");
        setLocationRelativeTo(c);
        iAction = ESCAPE;
        pack();
        jButtonNo.requestFocus();
        //show();
        setVisible(true);
        return(iAction);
    }
    
    public PrintService getSelectedPrintService(){
        if ((jComboBox.getSelectedIndex() > -1) && (jComboBox.getSelectedIndex() < jComboBox.getItemCount())){
            return(services[jComboBox.getSelectedIndex()]);
        }
        return(null);
    }
    
    private void actionYES(){
        iAction = YES;
        //hide();
        setVisible(false);
    }
    
    private void actionNO(){
        iAction = NO;
        //hide();
        setVisible(false);
    }
    
    private void actionESCAPE(){
        iAction = ESCAPE;
        //hide();
        setVisible(false);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Yes")) actionYES();
        if (e.getActionCommand().equals("No")) actionNO();
    }
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER) {
            if (jButtonYes.isFocusOwner()) actionYES();
            if (jButtonNo.isFocusOwner()) actionNO();
        }
        if (e.getKeyCode()==KeyEvent.VK_LEFT) jButtonYes.requestFocus();
        if (e.getKeyCode()==KeyEvent.VK_RIGHT) jButtonNo.requestFocus();
        if (e.getKeyCode()==KeyEvent.VK_I) actionYES();
        if (e.getKeyCode()==KeyEvent.VK_N) actionNO();
        if (e.getKeyCode()==KeyEvent.VK_ESCAPE) actionESCAPE();
    }
    
    public void keyReleased(KeyEvent e) {
    }
    
    public void keyTyped(KeyEvent e) {
    }
    
}
