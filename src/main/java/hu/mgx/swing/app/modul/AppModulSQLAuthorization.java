package hu.mgx.swing.app.modul;

import java.sql.*;

import hu.mgx.util.*;

public class AppModulSQLAuthorization implements AppModulAuthorizationInterface
{

    private Connection connection = null;
    private String sSQLLogin = "select username, password, userid from usertable where username=?";
    private String sSQLSetPassword = "update usertable set password=? where userid=?";
    private String sUserName = "";
    private String sPassword = "";
    private int iUserID = 0;
    private boolean bLoggedIn = false;
    private int iMaximumAttempt = 1;

    public AppModulSQLAuthorization(Connection connection)
    {
        if (connection == null)
        {
            throw new NullPointerException();
        }
        this.connection = connection;
    }

    public void setSQLLogin(String sSQLLogin)
    {
        this.sSQLLogin = sSQLLogin;
    }

    public void setSQLSetPassword(String sSQLSetPassword)
    {
        this.sSQLSetPassword = sSQLSetPassword;
    }
    //--- interface mothods ---
    public boolean loginNeeded()
    {
        return (true);
    }

    public void setMaximumAttempt(int iMaximumAttempt)
    {
        this.iMaximumAttempt = iMaximumAttempt;
    }

    public int getRemainingAttempt()
    {
        return (iMaximumAttempt);
    }

    public boolean login(String sUserName, String sPassword)
    {
        if (connection == null)
        {
            return (false);
        }
        if (sUserName == null)
        {
            return (false);
        }
        if (sPassword == null)
        {
            return (false);
        }
        if (sUserName.length() < 1)
        {
            return (false);
        }
        if (sPassword.length() < 1)
        {
            return (false);
        }
        String sStoredPassword = "";
        String sStoredUserName = "";
        PreparedStatement preparedStatement;
        ResultSet rs;
        try
        {
            preparedStatement = connection.prepareStatement(sSQLLogin);
            preparedStatement.setString(1, sUserName);
            rs = preparedStatement.executeQuery();
            if (rs.next())
            {
                sStoredUserName = rs.getString(1);
                if (rs.wasNull())
                {
                    sStoredUserName = "";
                }
                sStoredUserName = sStoredUserName.trim();
                sStoredPassword = rs.getString(2);
                if (rs.wasNull())
                {
                    sStoredPassword = "";
                }
                sStoredPassword = sStoredPassword.trim();
                iUserID = rs.getInt(3);
                if (rs.wasNull())
                {
                    iUserID = 0;
                }
                if (sStoredUserName.equals(sUserName))
                { //az SQL nem különbözteti meg a kis- és nagybetûket, itt viszont csak a pontosan egyezõt engedjük át
                    if (sStoredPassword.equals(""))
                    {
                        this.sUserName = sUserName;
                        if (setPassword(sPassword))
                        {
                            this.sPassword = sPassword;
                            this.bLoggedIn = true;
                            return (true);
                        }
                    }
                    else
                    {
                        if (PwUtils.codePw(sPassword).equalsIgnoreCase(sStoredPassword))
                        {
                            this.sUserName = sUserName;
                            this.sPassword = sPassword;
                            this.bLoggedIn = true;
                            return (true);
                        }
                    }
                }
            }
            preparedStatement.close();
            preparedStatement = null;
        }
        catch (SQLException sqle)
        {
        }
        return (false);
    }

    public boolean loggedIn()
    {
        return (bLoggedIn);
    }

    public int getUserID()
    {
        return (iUserID);
    }

    public boolean hasPermission(int iPermission)
    {
        return (false);
    }

    public boolean changePassword(String sNewPassword)
    {
        if (bLoggedIn)
        {
            return (setPassword(sNewPassword));
        }
        return (false);
    }

    public void logout()
    {
        this.sUserName = "";
        this.sPassword = "";
        this.iUserID = 0;
        this.bLoggedIn = false;
    }
    //--- private methods ---
    private boolean setPassword(String sPassword)
    {
        PreparedStatement preparedStatement;
        int iUpdateCount = 0;
        try
        {
            preparedStatement = connection.prepareStatement(sSQLSetPassword);
            preparedStatement.setString(1, PwUtils.codePw(sPassword));
            preparedStatement.setInt(2, iUserID);
            iUpdateCount = preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = null;
            if (iUpdateCount == 1)
            {
                this.sPassword = sPassword;
                return (true);
            }
        }
        catch (SQLException sqle)
        {
        }
        return (false);
    }
}
