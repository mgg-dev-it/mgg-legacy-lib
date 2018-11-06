package hu.mgx.app.swing;

public class ErrorSeverity
{

    private int iLevel = 0;

    public ErrorSeverity(int iLevel)
    {
        this.iLevel = iLevel;
    }

    public int getLevel()
    {
        return (iLevel);
    }
}
