package hu.mgx.swing.table;

import java.awt.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import hu.mgx.app.common.*;
import hu.mgx.db.*;

public class LookupRenderer implements TableCellRenderer
{

    private FieldDefinition fieldDefinition;
    private JTextField jTextField;
    private Color cSelectedColor;
    private JComboBox valueComboBox;
    private JComboBox displayComboBox;
    private SimpleDateFormat simpleDateFormat;
    private FormatInterface mgxFormat;

    public LookupRenderer(Color cSelectedColor, FieldDefinition fieldDefinition, String sLookup, FormatInterface mgxFormat)
    {
        super();
        this.cSelectedColor = cSelectedColor;
        this.fieldDefinition = fieldDefinition;
        this.mgxFormat = mgxFormat;
        simpleDateFormat = mgxFormat.getDateFormat();
        valueComboBox = new JComboBox();
        displayComboBox = new JComboBox();
        jTextField = new JTextField();
        jTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
        if (fieldDefinition.getType() == FieldType.INT)
        {
            //jTextField.setHorizontalAlignment(JTextField.RIGHT);
        }
        setLookup(sLookup);
    }

    public void setLookup(String sLookup)
    {
        String sNext = "";
        String sValue = "";
        String sDisplay = "";

        StringTokenizer st = new StringTokenizer(sLookup, "@", false);
        while (st.hasMoreTokens())
        {
            sNext = st.nextToken();//.trim();
            StringTokenizer st1 = new StringTokenizer(sNext, "|", false);
            sValue = "";
            sDisplay = "";
            if (st1.hasMoreTokens())
            {
                sValue = st1.nextToken().trim();
            }
            if (st1.hasMoreTokens())
            {
                sDisplay = st1.nextToken().trim();
            }
            addItem(sValue, sDisplay);
        }
        return;
    }

    private Object convertValue(String sValue)
    {
        int i;
        double d;
        java.util.Date utilDate;
        if (fieldDefinition.getType() == FieldType.INT)
        {
            if (sValue.equals(""))
            {
                return (null);
            }
            try
            {
                i = Integer.parseInt(sValue);
            }
            catch (NumberFormatException e)
            {
                i = 0;
            }
            return (new Integer(i));
        }
        if (fieldDefinition.getType() == FieldType.DECIMAL)
        {
            if (sValue.equals(""))
            {
                return (null);
            }
            try
            {
                d = Double.parseDouble(sValue);
            }
            catch (NumberFormatException e)
            {
                d = 0.0;
            }
            return (new Double(d));
        }
        if (fieldDefinition.getType() == FieldType.DATE)
        {
            if (sValue.equals(""))
            {
                return (null);
            }
            try
            {
                utilDate = simpleDateFormat.parse(sValue);
            }
            catch (ParseException e)
            {
                utilDate = new java.util.Date();
            }
            return (new java.sql.Date(utilDate.getTime()));
        }
        return (sValue);
    }

    public void addItem(String sValue, String sDisplay)
    {
        displayComboBox.addItem(new String(sDisplay));
        valueComboBox.addItem(convertValue(sValue));
    }

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value == null)
        {
            jTextField.setText("");
        }
        else
        {
            boolean bFound = false;
            for (int i = 0; i < valueComboBox.getItemCount(); i++)
            {
                if (valueComboBox.getItemAt(i).toString().equals(value.toString()))
                {
                    jTextField.setText(displayComboBox.getItemAt(i).toString());
                    bFound = true;
                }
            }
            if (!bFound)
            {
                jTextField.setText("<" + value.toString() + ">");
            }
        }
        if (isSelected)
        {
            jTextField.setBackground(cSelectedColor);
        }
        else
        {
            jTextField.setBackground(ColorManager.inputBackgroundFocusLost());
        }
        return (jTextField);
    }
}
