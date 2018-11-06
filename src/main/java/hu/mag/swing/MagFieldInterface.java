package hu.mag.swing;

public interface MagFieldInterface {

    public void setValue(Object o);

    public Object getValue();

    public void setReadOnly(boolean bReadOnly);

    public boolean isChanged();

//    public Object getOldValue();
//
//    public void setEnabled(boolean enabled);
//
//    public void setFocus();
//
//    public boolean changed();
//
//    public boolean check();
//
//    public void setSize(java.awt.Dimension d);
//
//    public javax.swing.JLabel getFieldLabel();
//
//    public void setActualToOrigin();
//
//    public void setOriginToActual();
    //@todo idea : createMag...Field (Class c, String sName, ... other (XML?) parameters)
}
