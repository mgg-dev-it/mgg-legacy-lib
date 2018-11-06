package hu.mgx.app.common;

public interface ErrorHandlerInterface
{

    public abstract void handleError(Exception e);

    public abstract void handleError(Exception e, int iErrorLevel);

    public abstract void handleError(String sInfo);

    public abstract void handleError(String sInfo, int iErrorLevel);

    public abstract void handleError(Exception e, String sInfo);

    public abstract void handleError(Exception e, String sInfo, int iErrorLevel);
}
