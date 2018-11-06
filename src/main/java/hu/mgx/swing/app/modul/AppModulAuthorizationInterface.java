package hu.mgx.swing.app.modul;

public interface AppModulAuthorizationInterface
{

    public boolean loginNeeded();

    public void setMaximumAttempt(int iMaximumAttempt);

    public int getRemainingAttempt();

    public boolean login(String sUserName, String sPassword);

    public boolean loggedIn();

    public int getUserID();

    public boolean hasPermission(int iPermission);

    public boolean changePassword(String sNewPassword);

    public void logout();
}
