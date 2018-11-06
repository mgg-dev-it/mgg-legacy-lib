package hu.mgx.app.common;

public interface VersionInterface
{

    public abstract int getMajor();

    public abstract int getMinor();

    public abstract int getRevision();

    //public abstract String getInfo(int iMajor, int iMinor, int iRevision);
    
    public abstract VersionHistory getVersionHistory();
}
