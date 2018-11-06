package hu.mgx.swing.table;

import java.awt.Component;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import hu.mgx.swing.*;

public class IntegerEditor extends AbstractCellEditor implements TableCellEditor, ActionListener, FocusListener
{

    private CommonTextField ctf = null;
    private boolean bSuppressZero = false;
    private boolean bEditing = false;

    public IntegerEditor(CommonTextField ctf, boolean bSuppressZero)
    {
        this.ctf = ctf;
        this.bSuppressZero = bSuppressZero;
        ctf.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        if (bSuppressZero && hu.mgx.util.StringUtils.isNull(value, "0").equals("0"))
        {
            value = "";
        }
        ctf.setValue(value);
        bEditing = true;
        return (ctf);
    }

    public boolean isEditing()
    {
        return (bEditing);
    }

    public void setEditing(boolean b)
    {
        bEditing = b;
    }

    public Object getCellEditorValue()
    {
        return ((Integer) ctf.getValue());
    }

    public boolean stopCellEditing()
    {
        fireEditingStopped();
        return true;
    }

    public void actionPerformed(ActionEvent e)
    {
        stopCellEditing();
    }

    public void focusGained(FocusEvent e)
    {
    }

    public void focusLost(FocusEvent e)
    {
        stopCellEditing();
    }
}
