package hu.mgx.swing.table;

import java.awt.event.*;
import javax.swing.*;

import hu.mgx.swing.maint.*;

public class MgxTable extends JTable implements MouseListener
{

    private MaintPanel maintPanel = null;

    public MgxTable(TableSorterNewStyle tableSorterNewStyle)
    {
        this(tableSorterNewStyle, null);
    }

    public MgxTable(TableSorterNewStyle tableSorterNewStyle, MaintPanel maintPanel)
    {
        super(tableSorterNewStyle);
        this.maintPanel = maintPanel;
        this.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        this.addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {
            if (maintPanel != null)
            {
                maintPanel.cellClicked(this.rowAtPoint(e.getPoint()), this.columnAtPoint(e.getPoint()), e.getClickCount());
            }
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
}
