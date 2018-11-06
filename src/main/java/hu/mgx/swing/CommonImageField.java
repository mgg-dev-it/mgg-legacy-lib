package hu.mgx.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import hu.mgx.db.*;
import hu.mgx.image.*;
import hu.mgx.lang.*;
import hu.mgx.app.swing.*;

public class CommonImageField extends CommonPanel implements DataField, MouseListener
{

    private FieldDefinition fieldDefinition;
    private JLabel jLabel;
    private JScrollPane jScrollPane;
    private ImageIcon imageIcon;
    private ByteArray imageData = null;
    private ImageExportImport imageExportImport;
    private Object oOrigin = null;
    private Component cValidate;

    public CommonImageField(FieldDefinition fieldDefinition, int iWidth, int iHeight, Component cValidate)
    {
        super();
        this.fieldDefinition = fieldDefinition;
        this.cValidate = cValidate;
        setSize(new Dimension(iWidth, iHeight));
        init();
    }

    private void init()
    {
        imageData = new ByteArray();
        imageIcon = new ImageIcon();
        imageExportImport = new ImageExportImport();
        jLabel = new JLabel(imageIcon);
        jLabel.addMouseListener(this);
        jScrollPane = new JScrollPane(jLabel);
        addToGrid(jScrollPane, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BOTH);
        addMouseListener(this);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (imageIcon != null)
        {
            g.drawImage(imageIcon.getImage(), 1, 1, imageIcon.getIconWidth(), imageIcon.getIconHeight(), this);
        }
    }

    private void color()
    {
        if (changed())
        {
            //setForeground(colorChanged);
        }
        else
        {
            //setForeground(colorNormal);
        }
    }

    public boolean changed()
    {
        if (oOrigin != null)
        {
            if (imageData == null)
            {
                return (true);
            }
            if (!imageData.toString().equals(((ByteArray) oOrigin).toString()))
            {
                return (true);
            }
        }
        else
        {
            if (imageData != null)
            {
                return (true);
            }
        }
        return (false);
    }

    public boolean check()
    {
        return (true);
    }

    public javax.swing.JLabel getFieldLabel()
    {
        return (new JLabel(" " + fieldDefinition.getDisplayName() + ":"));
    }

    public Object getValue()
    {
        return (imageData);
    }

    public void setActualToOrigin()
    {
        setValue(oOrigin);
        color();
    }

    public void setFocus()
    {
    }

    public void setOriginToActual()
    {
        oOrigin = getValue();
        color();
    }

    public void setValue(Object o)
    {
        oOrigin = o;
        if (o == null)
        {
            imageData.setBytes(null);
            imageIcon = null;
        }
        else
        {
            imageData = new ByteArray(((ByteArray) o).getBytes());
            if (imageData.getBytes() == null)
            {
                imageIcon = null;
            }
            else
            {
                imageIcon = new ImageIcon(imageData.getBytes());
            }
        }
        jLabel = new JLabel(imageIcon);
        jLabel.addMouseListener(this);
        jScrollPane.setViewportView(jLabel);
        jScrollPane.validate();
    }

    private void imageImport()
    {
        byte[] b = imageExportImport.imageImport(this);
        if (b != null)
        {
            imageData.setBytes(b);
            imageIcon = new ImageIcon(imageData.getBytes());
            jLabel = new JLabel(imageIcon);
            jLabel.addMouseListener(this);
            jScrollPane.setViewportView(jLabel);
            jScrollPane.validate();
        }
    }

    public void setSize(java.awt.Dimension d)
    {
        setMinimumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getSource() != null)
        {
            imageImport();
        }
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public Object getOldValue()
    {
        return (oOrigin);
    }
}
