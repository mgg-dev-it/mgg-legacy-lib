package hu.mgx.swing.app.modul;

public class AppModulNoAuthorization implements AppModulAuthorizationInterface
{

    public AppModulNoAuthorization()
    {
    }

    public boolean loginNeeded()
    {
        return (false);
    }

    public void setMaximumAttempt(int iMaximumAttempt)
    {
    }

    public int getRemainingAttempt()
    {
        return (1);
    }

    public boolean login(String sUserName, String sPassword)
    {
        return (true);
    }

    public boolean loggedIn()
    {
        return (true);
    }

    public int getUserID()
    {
        return (0);
    }

    public boolean hasPermission(int iPermission)
    {
        return (true);
    }

    public boolean changePassword(String sNewPassword)
    {
        return (false);
    }

    public void logout()
    {
    }
}
