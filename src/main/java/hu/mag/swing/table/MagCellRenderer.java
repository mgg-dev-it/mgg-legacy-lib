package hu.mag.swing.table;

import hu.mag.lang.LookupInteger;
import hu.mag.swing.MagColorScheme;
import hu.mag.swing.MagLookupTextField;
import hu.mag.swing.text.DateTimeDocument;
import hu.mgx.app.swing.SwingAppInterface;
import hu.mgx.util.BigDecimalUtils;
import hu.mgx.util.IntegerUtils;
import hu.mgx.util.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

public class MagCellRenderer extends JLabel implements TableCellRenderer {

    private MagTableModel magTableModel;
    private JCheckBox jCheckBox;
    private JButton jButton;
    private SwingAppInterface swingAppInterface = null;
    private MagLookupTextField magLookupTextField;
    private boolean bDrawTriangle;
    private boolean bNoFocusBorder;
    private MagColorScheme magColorScheme;

    public MagCellRenderer(MagTableModel magTableModel, SwingAppInterface swingAppInterface) {
        this.magTableModel = magTableModel;
        this.swingAppInterface = swingAppInterface;
        this.setOpaque(true);
        jCheckBox = new JCheckBox();
        jCheckBox.setHorizontalAlignment(JLabel.CENTER);
        jButton = new JButton();
        this.bDrawTriangle = false;
        this.bNoFocusBorder = false;
        this.magColorScheme = new MagTableBasicColorScheme();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int iModelRow = table.convertRowIndexToModel(row);
        int iModelColumn = table.convertColumnIndexToModel(column);

//        System.out.println(column);
//        int iRowStatus = magTableModel.getRowStatus(row);
        int iModelRowStatus = magTableModel.getRowStatus(iModelRow);
        Color colorRowBackground = magTableModel.getRowColorBackground(iModelRow);
        Color colorCellBackground = magTableModel.getCellColorBackground(iModelRow, iModelColumn);
        Color colorCellForeground = magTableModel.getCellColorForeground(iModelRow, iModelColumn);

//        if (row == 0 && column == 0) {
//            System.out.println("0");
//        }
        boolean bIsCellEditable = magTableModel.isCellEditable(iModelRow, iModelColumn);
        boolean bIsCellPrimaryKey = magTableModel.isColumnPrimaryKey(iModelColumn);
        Color colorBackground = Color.WHITE;
//        String s = "";

//        if (column == 0 && !bIsCellEditable) {
//            System.out.println("not editable " + Integer.toString(row));
//        }
//        if (column == 0 && bIsCellEditable) {
//            System.out.println("editable " + Integer.toString(row));
//        }
        int iColumnType = java.sql.Types.OTHER;
        try {
            if (magTableModel.getResultSetMetaData() != null) {
                iColumnType = magTableModel.getResultSetMetaData().getColumnType(column + 1);
            }
        } catch (SQLException sqle) {
            //appInterface.handleError(sqle);
        }

        Color colorCellBackgroundEnabledBase = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledBase);
        Color colorCellBackgroundEnabledBaseAlternate = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledBaseAlternate);
        Color colorCellBackgroundEnabledSelected = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledSelected);

        Color colorCellBackgroundDisabledBase = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundDisabledBase);
        Color colorCellBackgroundDisabledBaseAlternate = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundDisabledBaseAlternate);
        Color colorCellBackgroundDisabledSelected = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundDisabledSelected);

        Color colorCellBackgroundEnabledNew = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledNew);
        Color colorCellBackgroundEnabledNewModified = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledNewModified);
        Color colorCellBackgroundEnabledModified = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledModified);

        Color colorCellBackgroundEnabledPrimaryKey = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledPrimaryKey);
        Color colorCellBackgroundEnabledPrimaryKeySelected = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledPrimaryKeySelected);
        Color colorCellBackgroundEnabledPrimaryKeyNew = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledPrimaryKeyNew);
        Color colorCellBackgroundEnabledPrimaryKeyNewModified = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledPrimaryKeyNewModified);
        Color colorCellBackgroundEnabledPrimaryKeyModified = magColorScheme.getColor(MagTableBasicColorScheme.CellBackgroundEnabledPrimaryKeyModified);

//        Color colorOrange = new Color(255, 224, 192);
//        Color colorChartreuseGreen = new Color(224, 255, 192);
//        Color colorRose = new Color(255, 192, 224);
//        Color colorViolet = new Color(224, 192, 255);
//        Color colorCyan = new Color(208, 255, 255);
//        Color colorCyanSelected = new Color(176, 255, 255);
//        Color colorAzure = new Color(192, 224, 255);
        Color colorLookup = new Color(255, 255, 192);
        Color colorLookupSelected = new Color(224, 224, 192);

        if (bIsCellEditable) {
            colorBackground = Color.WHITE;
            if (iModelRowStatus == MagTableModel.ROW_STATUS_NEW) {
                colorBackground = colorCellBackgroundEnabledNew;
                //colorBackground = colorViolet;
                //new and not selected - impossible
            }
            if (iModelRowStatus == MagTableModel.ROW_STATUS_NEW_MODIFIED) {
                colorBackground = colorCellBackgroundEnabledNewModified;
                //colorBackground = colorRose;
                //modified and not selected - impossible
            }
            if (iModelRowStatus == MagTableModel.ROW_STATUS_MODIFIED) {
                if (colorCellBackgroundEnabledModified != null) {
                    colorBackground = colorCellBackgroundEnabledModified;
                } else {
                    colorBackground = (isSelected ? colorCellBackgroundEnabledSelected : (row % 2 == 1 ? colorCellBackgroundEnabledBase : colorCellBackgroundEnabledBaseAlternate));
                }
                //colorBackground = colorOrange;
                //modified and not selected - impossible
            }
            if (iModelRowStatus == MagTableModel.ROW_STATUS_OK) {
                colorBackground = (isSelected ? colorCellBackgroundEnabledSelected : (row % 2 == 1 ? colorCellBackgroundEnabledBase : colorCellBackgroundEnabledBaseAlternate));
            }
            if (bIsCellPrimaryKey) {
                colorBackground = colorCellBackgroundEnabledPrimaryKey;
                //colorBackground = colorCyan;
                if (isSelected) {
                    colorBackground = colorCellBackgroundEnabledPrimaryKeySelected;
                    //colorBackground = colorCyanSelected;
                    if (iModelRowStatus == MagTableModel.ROW_STATUS_NEW) {
                        colorBackground = colorCellBackgroundEnabledPrimaryKeyNew;
                        //colorBackground = colorAzure;
                    }
                    if (iModelRowStatus == MagTableModel.ROW_STATUS_NEW_MODIFIED) {
                        colorBackground = colorCellBackgroundEnabledPrimaryKeyNewModified;
                        //colorBackground = colorViolet;
                    }
                    if (iModelRowStatus == MagTableModel.ROW_STATUS_MODIFIED) {
                        colorBackground = colorCellBackgroundEnabledPrimaryKeyModified;
                        //colorBackground = colorChartreuseGreen;
                    }
                }
            }
        } else {
            colorBackground = (isSelected ? colorCellBackgroundDisabledSelected : (row % 2 == 1 ? colorCellBackgroundDisabledBase : colorCellBackgroundDisabledBaseAlternate));
        }
        if (magTableModel.isLookupTable()) {
            colorBackground = (isSelected ? colorLookupSelected : colorLookup);
        }
        if (colorRowBackground != null) {
            colorBackground = colorRowBackground;
        }
        if (colorCellBackground != null) {
            colorBackground = colorCellBackground;
        }
        this.setBackground(colorBackground);
        jCheckBox.setBackground(colorBackground);
        if (colorCellForeground != null) {
            this.setForeground(colorCellForeground);
        } else {
            this.setForeground(Color.black);
        }

        //if (hasFocus) {
        if (hasFocus && !bNoFocusBorder) {
            this.setBorder(new LineBorder(Color.BLUE));
            jCheckBox.setBorder(new LineBorder(Color.BLUE));
        } else {
            this.setBorder(new EmptyBorder(1, 1, 1, 1));
            jCheckBox.setBorder(new EmptyBorder(1, 1, 1, 1));
        }

        if (magTableModel.getColumnSpecType(column).equals("button")) {
            //return (jCheckBox);
            jButton.setText(value != null ? value.toString() : "");
            return (jButton);
        }

        if (magTableModel.getColumnClass(column).equals(Boolean.class)) { //we should handle it correctly even if value is null
            jCheckBox.setSelected((value != null && ((Boolean) value).booleanValue()));
            return (jCheckBox);
        }

        if (value == null) {
            this.setText("");
            setToolTipText("");
        } else {
            this.setText(value.toString());
            if (magTableModel.isLookupColumn(column)) {
                //MagComboBoxField magLookupField = magTableModel.getColumnLookupField(column);
                //this.setText(magLookupField.getDisplay(value).toString());
                magLookupTextField = magTableModel.getColumnLookupTableTextField(column);
                //magLookupTextField.setValue(value);
                this.setText(magLookupTextField.getDisplay(value).toString());
                setToolTipText(iModelColumn);
                //return (magLookupTextField);
                this.bDrawTriangle = true;
                return (this);
            }
            if (value.getClass().equals(String.class)) {
                //this.setText(value.toString());
                this.setText(value.toString().replaceAll("\r\n", " ").replaceAll("\n\r", " ").replaceAll("\n", " ").replaceAll("\r", " "));
                //setToolTipText(magTableModel.getColumnName(iModelColumn) + ": " + this.getText() + (!magTableModel.getToolTipText(iModelColumn).equalsIgnoreCase("") ? " / " + magTableModel.getToolTipText(iModelColumn) : ""));
                setToolTipText(iModelColumn);
                if (magTableModel.isRightAlignedColumn(column)) {
                    this.setHorizontalAlignment(SwingConstants.RIGHT);
                }
                if (magTableModel.isCenteredColumn(column)) {
                    this.setHorizontalAlignment(SwingConstants.CENTER);
                }
                return (this);
            }
            if (magTableModel.getColumnSpecType(column).equals("timesheet_time_time")) {
                //System.out.println(value.toString());
                int iTimeSheetSeconds = IntegerUtils.convertToInt(value);
                String sTimeSheetValue = Integer.toString(iTimeSheetSeconds / 3600) + ":" + StringUtils.right("00" + Integer.toString((iTimeSheetSeconds % 3600) / 60), 2);
                if (sTimeSheetValue.endsWith(":00")) {
                    sTimeSheetValue = sTimeSheetValue.substring(0, sTimeSheetValue.length() - 3);
                }
                this.setText(sTimeSheetValue);
                this.setHorizontalAlignment(JLabel.CENTER);
                return (this);
            }
            if (magTableModel.getColumnSpecType(column).equals("timesheet_time_decimal")) {
                String sTimeSheetValue = BigDecimalUtils.convertToBigDecimal(value).divide(new BigDecimal(3600), 2, RoundingMode.HALF_UP).toPlainString();
                if (sTimeSheetValue.endsWith(".00")) {
                    sTimeSheetValue = sTimeSheetValue.substring(0, sTimeSheetValue.length() - 3);
                }
                this.setText(sTimeSheetValue);
                this.setHorizontalAlignment(JLabel.CENTER);
                return (this);
            }
            if (value.getClass().equals(java.lang.Short.class) || value.getClass().equals(java.lang.Integer.class) || value.getClass().equals(java.lang.Long.class) || value.getClass().equals(java.lang.Double.class) || value.getClass().equals(java.lang.Float.class)) {
                if (value != null) {
                    this.setText(swingAppInterface.getDecimalFormat().format(value));
                }
                this.setHorizontalAlignment(JLabel.RIGHT);
                setToolTipText(iModelColumn);
                return (this);
            }
            if (value.getClass().equals(java.math.BigDecimal.class)) {
                if (magTableModel.getColumnSpecType(column).equals("decimal_time")) {
                    this.setText(DateTimeDocument.convertDecimaltimeToString((java.math.BigDecimal) value, swingAppInterface));
                } else {
                    if (value != null) {
                        //this.setText(swingAppInterface.getDecimalFormat().format(value));
                        String sBDValue = swingAppInterface.getDecimalFormat().format(value);
                        java.math.BigDecimal bd = (java.math.BigDecimal) value;
                        int iScale = bd.scale();
                        String sDecimalSeparator = new StringBuffer().append(swingAppInterface.getDecimalSeparator()).toString();
                        //System.out.println(iScale);
                        //System.out.println(sDecimalSeparator);
                        //System.out.println(swingAppInterface.getDecimalSeparator());
                        if (iScale > 0) {
                            if (sBDValue.contains(sDecimalSeparator)) {
                                //sBDValue += StringUtils.repeat("0", sBDValue.length() - sBDValue.indexOf(sDecimalSeparator));
                                sBDValue += StringUtils.repeat("0", sBDValue.length() - sBDValue.indexOf(sDecimalSeparator) - iScale - 1);
                            } else {
                                sBDValue += sDecimalSeparator + StringUtils.repeat("0", iScale);
                            }
                        }
                        this.setText(sBDValue);
                    }
                }
                this.setHorizontalAlignment(JLabel.RIGHT);
                setToolTipText(iModelColumn);
                return (this);
            }
            if (value.getClass().equals(java.util.Date.class)) {
                if (magTableModel.getColumnSpecType(column).equals("date_only")) {
                    this.setText(swingAppInterface.getDateFormat().format(value));
                } else {
                    this.setText(swingAppInterface.getDateTimeFormat().format(value));
                }
                setToolTipText(iModelColumn);
                return (this);
            }
            if (value.getClass().equals(java.sql.Date.class)) {
                if (iColumnType == java.sql.Types.DATE) {
                    this.setText(swingAppInterface.getDateFormat().format(new java.util.Date(((java.sql.Date) value).getTime())));
                } else {
                    this.setText(swingAppInterface.getDateTimeFormat().format(new java.util.Date(((java.sql.Date) value).getTime())));
                }
                setToolTipText(iModelColumn);
                return (this);
            }
            if (value.getClass().equals(java.sql.Time.class)) {
                if (iColumnType == java.sql.Types.TIME) {
                    this.setText(swingAppInterface.getTimeFormat().format(new java.util.Date(((java.sql.Time) value).getTime())));
                } else {
                    //System.out.println(Integer.toString(iColumnType));
                    //this.setText(swingAppInterface.getDateTimeFormat().format(new java.util.Date(((java.sql.Date) value).getTime())));
                    this.setText(swingAppInterface.getDateTimeFormat().format(new java.util.Date(((java.sql.Time) value).getTime())));
                }
                setToolTipText(iModelColumn);
                return (this);
            }
            if (value.getClass().equals(java.sql.Timestamp.class)) {
                if (magTableModel.getColumnSpecType(column).equals("date_only")) {
                    this.setText(swingAppInterface.getDateFormat().format(new java.util.Date(((java.sql.Timestamp) value).getTime())));
                } else if (magTableModel.getColumnSpecType(column).equals("time_only")) {
                    this.setText(swingAppInterface.getTimeFormat().format(new java.util.Date(((java.sql.Timestamp) value).getTime())));
                } else {
                    this.setText(swingAppInterface.getDateTimeFormat().format(new java.util.Date(((java.sql.Timestamp) value).getTime())));
                }
                setToolTipText(iModelColumn);
                return (this);
            }
            if (value.getClass().equals(LookupInteger.class)) {
                this.setText(((LookupInteger) value).getDisplay());
                setToolTipText(iModelColumn);
                return (this);
            }
        }
        return (this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bDrawTriangle && magLookupTextField != null) {
            magLookupTextField.drawTriangle(this, g);
        }
    }

    private void setToolTipText(int iModelColumn) {
        //setToolTipText(magTableModel.getColumnName(iModelColumn) + ": " + this.getText() + (!magTableModel.getToolTipText(iModelColumn).equalsIgnoreCase("") ? " / " + magTableModel.getToolTipText(iModelColumn) : ""));
        //setToolTipText(StringUtils.filterHTML(magTableModel.getColumnName(iModelColumn)) + ": " + StringUtils.isNull(this.getText(), "").trim() + (!magTableModel.getToolTipText(iModelColumn).equalsIgnoreCase("") ? " / " + magTableModel.getToolTipText(iModelColumn) : "").trim());
        setToolTipText(StringUtils.filterHTML(magTableModel.getColumnName(iModelColumn) + ": " + StringUtils.isNull(this.getText(), "").trim()) + (!magTableModel.getToolTipText(iModelColumn).equalsIgnoreCase("") ? " / " + magTableModel.getToolTipText(iModelColumn) : "").trim());
        //swingAppInterface.logLine(sText);
    }

    public void setNoFocusBorder(boolean b) {
        this.bNoFocusBorder = b;
    }

    public void setMagColorScheme(MagColorScheme magColorScheme) {
        this.magColorScheme = magColorScheme;
    }

    @Override
    public String toString() {
        return (this.getText());
    }
}
