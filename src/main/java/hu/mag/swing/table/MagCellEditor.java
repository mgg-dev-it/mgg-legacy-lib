package hu.mag.swing.table;

import hu.mag.lang.LookupInteger;
import hu.mag.swing.MagTextAreaField;
import hu.mag.swing.MagComboBoxField;
import hu.mag.swing.MagLookupTextField;
import hu.mag.swing.MagTextField;
import hu.mgx.app.swing.SwingAppInterface;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class MagCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private MagTextField magTextField = null;
    private MagTextAreaField magTextAreaField = null;
    private MagComboBoxField magLookupField = null;
    private MagLookupTextField magLookupTextField = null;
    private JComboBox jCombobox;
    private LookupInteger li;
    private JCheckBox jCheckBox;
    private MagTableModel magTableModel;
    private int iColumn;
    private Class c;
    private boolean bLookup = false;
    private boolean bText = false;

    public MagCellEditor(MagTableModel magTableModel, int iColumn, SwingAppInterface swingAppInterface) {
        super();
        this.magTableModel = magTableModel;
        this.iColumn = iColumn;
        magTextField = new MagTextField(swingAppInterface);
        magTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
        magTextAreaField = new MagTextAreaField(swingAppInterface);
        magTextAreaField.setBorder(new EmptyBorder(0, 0, 0, 0));
        magLookupField = new MagComboBoxField(swingAppInterface);
        magLookupField.setNoBorder();
    }

    @Override
    public Object getCellEditorValue() {
        if (bLookup) {
            //return (magLookupField.getValue());
            return (magLookupTextField.getValue());
        }
        if (bText) {
            return (magTextAreaField.getValue());
        }
        if (c.equals(String.class)) {
            return (magTextField.getValue());
        }
        if (c.equals(java.lang.Short.class) || c.equals(java.lang.Integer.class) || c.equals(java.lang.Long.class) || c.equals(java.lang.Double.class) || c.equals(java.lang.Float.class)) {
            return (magTextField.getValue());
        }
        if (c.equals(java.math.BigDecimal.class)) {
            return (magTextField.getValue());
        }
        if (c.equals(LookupInteger.class)) {
            li.setIndex(jCombobox.getSelectedIndex());
            return (li);
        }
        if (c.equals(Boolean.class)) {
            return (Boolean.valueOf(jCheckBox.isSelected()));
        }
        if (c.equals(java.util.Date.class)) {
            return (magTextField.getValue());
        }
        if (c.equals(java.sql.Date.class)) {
            return (magTextField.getValue());
        }
        if (c.equals(java.sql.Time.class)) {
            return (magTextField.getValue());
        }
        if (c.equals(java.sql.Timestamp.class)) {
            return (magTextField.getValue());
        }
        return (magTextField.getText());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        int iModelRow = table.convertRowIndexToModel(row);
        int iModelColumn = table.convertColumnIndexToModel(column);
        c = magTableModel.getColumnClass(iModelColumn);
        if (magTableModel.isLookupColumn(iModelColumn)) {
            bLookup = true;
//            magLookupField = magTableModel.getColumnLookupField(iModelColumn);
//            magLookupField.setNoBorder();
//            magLookupField.setClass(c);
//            magLookupField.setValue(value);
//            return magLookupField;
            magLookupTextField = magTableModel.getColumnLookupTableTextField(iModelColumn);
            magLookupTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
            magLookupTextField.setClass(c);
            magLookupTextField.setValue(value);
            return magLookupTextField;
        }
        if (c.equals(String.class)) {
            magTextField.setClass(c);
            magTextField.setMaxLength(magTableModel.getMaxLength(iModelColumn));
            magTextField.setUpperCase(magTableModel.isUpperCaseColumn(iModelColumn));
            magTextField.setValue(value);
            return magTextField;
        }
        if (c.equals(java.lang.Short.class) || c.equals(java.lang.Integer.class) || c.equals(java.lang.Long.class) || c.equals(java.lang.Double.class) || c.equals(java.lang.Float.class)) {
            magTextField.setClass(c);
            magTextField.setMaxLength(magTableModel.getMaxLength(iModelColumn));
            magTextField.setDecimals(magTableModel.getDecimals(iModelColumn));
            if (magTableModel.getColumnSpecType(iModelColumn).length() != 0) {
                magTextField.setSpecType(magTableModel.getColumnSpecType(iModelColumn));
            }
            magTextField.setValue(value);
            return magTextField;
        }
        if (c.equals(java.math.BigDecimal.class)) {
            magTextField.setSpecType(magTableModel.getColumnSpecType(column));
            magTextField.setClass(c);
            magTextField.setMaxLength(magTableModel.getMaxLength(iModelColumn));
            magTextField.setDecimals(magTableModel.getDecimals(iModelColumn));
            if (magTableModel.getColumnSpecType(iModelColumn).length() != 0) {
                magTextField.setSpecType(magTableModel.getColumnSpecType(iModelColumn));
            }
            magTextField.setValue(value);
            return magTextField;
        }
        if (c.equals(LookupInteger.class)) {
            li = new LookupInteger(((LookupInteger) value).getHashMap());
            jCombobox = new JComboBox(((LookupInteger) value).getDisplayVector());
            jCombobox.setBorder(new EmptyBorder(0, 0, 0, 0));
            if (value == null) {
                jCombobox.setSelectedIndex(-1);

            } else {
                jCombobox.setSelectedIndex(li.getIndex(((LookupInteger) value).getValue()));
            }
            return (jCombobox);
        }
        if (c.equals(Boolean.class)) {
            jCheckBox = new JCheckBox();
            jCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
            boolean selected = false;
            if (value instanceof Boolean) {
                selected = ((Boolean) value).booleanValue();
            } else if (value instanceof String) {
                selected = value.equals("true");
            }
            jCheckBox.setSelected(selected);
            jCheckBox.setRequestFocusEnabled(false);

            //TableCellRenderer renderer = table.getCellRenderer(iModelRow, iModelColumn);
            //Component c = renderer.getTableCellRendererComponent(table, value, isSelected, true, iModelRow, iModelColumn);
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component c = renderer.getTableCellRendererComponent(table, value, isSelected, true, row, column);
            if (c != null) {
                jCheckBox.setOpaque(true);
                jCheckBox.setBackground(c.getBackground());
                if (c instanceof JComponent) {
                    jCheckBox.setBorder(((JComponent) c).getBorder());
                }
            } else {
                jCheckBox.setOpaque(false);
            }

            return (jCheckBox);
        }
        if (c.equals(java.util.Date.class)) {
            magTextField.setClass(c);
            magTextField.setValue(value);
            return magTextField;
        }
        if (c.equals(java.sql.Date.class)) {
            magTextField.setClass(c);
            magTextField.setValue(value);
            return magTextField;
        }
        if (c.equals(java.sql.Time.class)) {
            magTextField.setClass(c);
            magTextField.setValue(value);
            return magTextField;
        }
        if (c.equals(java.sql.Timestamp.class)) {
            magTextField.setSpecType(magTableModel.getColumnSpecType(column));
            magTextField.setClass(c);
            magTextField.setValue(value);
            return magTextField;
        }
        magTextField.setText(value.toString());
        return magTextField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        fireEditingStopped();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
//            return ((MouseEvent) anEvent).getClickCount() >= 2;
            //if (magTableModel.getColumnClass(iColumn).equals(Boolean.class)) {
            if (magTableModel.getColumnClass(iColumn).equals(Boolean.class) || magTableModel.isTextColumn(iColumn)) {
                return ((MouseEvent) anEvent).getClickCount() >= 100;
            } else {
                return ((MouseEvent) anEvent).getClickCount() >= 2;
            }
        }
        return true;
    }

    public void formatChanged() {
        magTextField.formatChanged();
    }
}
