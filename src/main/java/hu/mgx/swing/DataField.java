package hu.mgx.swing;

public interface DataField
{

    public Object getValue();

    public Object getOldValue();

    public void setValue(Object o);

    public void setEnabled(boolean enabled);

    public void setFocus();

    public boolean changed();

    public boolean check();

    public void setSize(java.awt.Dimension d);

    public javax.swing.JLabel getFieldLabel();

    public void setActualToOrigin();

    public void setOriginToActual();
}
