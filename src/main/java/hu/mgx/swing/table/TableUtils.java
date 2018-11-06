package hu.mgx.swing.table;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;

public abstract class TableUtils {

    public static void setColumnWidth(JTable jTable, int iColumn, int iWidth) {
        jTable.getColumnModel().getColumn(iColumn).setMinWidth(iWidth);
        jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(iWidth);
        jTable.getColumnModel().getColumn(iColumn).setMaxWidth(iWidth);
    }

    public static void setColumnPreferredWidth(JTable jTable, int iColumn, int iWidth) {
        jTable.getColumnModel().getColumn(iColumn).setPreferredWidth(iWidth);
    }

    public static void setMinColumnWidthAll(JTable jTable, int iWidth) {
        for (int i = 0; i < jTable.getColumnCount(); i++) {
            jTable.getColumnModel().getColumn(i).setMinWidth(iWidth);
        }
    }

    public static void setAutoColumnWidth(JTable jTable) {
        setAutoColumnWidth(jTable, true, 0);
    }

    public static void setAutoColumnWidth(JTable jTable, boolean bWithHeader, int iMaxColumnWidth) {
        Font font = jTable.getFont();
        FontMetrics fm = new javax.swing.JPanel().getFontMetrics(font);
        Object oCellValue = null;
        String sCellValue = "";
        Vector<Integer> vColumnWidth = new Vector<Integer>();
        for (int i = 0; i < jTable.getColumnCount(); i++) {
            vColumnWidth.add(new Integer(0));
        }
        if (bWithHeader) {
            for (int i = 0; i < jTable.getColumnCount(); i++) {
                sCellValue = jTable.getColumnModel().getColumn(i).getHeaderValue().toString();
                sCellValue = "X" + sCellValue + "X"; // a fejlécben nagyobb a margó. ezt szimuláljuk a két betû hozzáadásával
                //System.out.println(sCellValue);
                if (fm.stringWidth(sCellValue) > vColumnWidth.elementAt(i).intValue()) {
                    vColumnWidth.setElementAt(new Integer(fm.stringWidth(sCellValue)), i);
                    //System.out.println(fm.stringWidth(sCellValue));
                }
            }
        }
        //System.out.println("");
        for (int iRow = 0; iRow < jTable.getRowCount(); iRow++) {
            for (int i = 0; i < jTable.getColumnCount(); i++) {
                //jTable.getCellRenderer(iRow, i).;
                sCellValue = "";
                oCellValue = jTable.getValueAt(iRow, i);
                if (oCellValue != null) {
                    sCellValue = oCellValue.toString();
                }
                if (fm.stringWidth(sCellValue) > vColumnWidth.elementAt(i).intValue()) {
                    vColumnWidth.setElementAt(new Integer(fm.stringWidth(sCellValue)), i);
                }
            }
        }
        for (int i = 0; i < jTable.getColumnCount(); i++) {
            if (iMaxColumnWidth > 0) {
                if (vColumnWidth.elementAt(i).intValue() > iMaxColumnWidth) {
                    vColumnWidth.setElementAt(new Integer(iMaxColumnWidth), i);
                }
            }
            setColumnPreferredWidth(jTable, i, vColumnWidth.elementAt(i).intValue() + 5);
            //System.out.println(vCW.elementAt(i).intValue());
        }
    }

    public static void setColumnToCheckBox(JTable jTable, int iColumn) {
        JCheckBox jCheckBox = new JCheckBox();
        jCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
        jTable.getColumnModel().getColumn(iColumn).setCellRenderer(new BitRenderer(jTable.getSelectionBackground()));
        jTable.getColumnModel().getColumn(iColumn).setCellEditor(new DefaultCellEditor(jCheckBox));
        //setColumnWidth(jTable, iColumn, 30);
    }

//    public static String saveTableContentToXML(JTable jTable) {
//        String sXML = "";
//        return (sXML);
//    }
}
