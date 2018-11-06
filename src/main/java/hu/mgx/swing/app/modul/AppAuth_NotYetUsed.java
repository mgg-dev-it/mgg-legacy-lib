package hu.mgx.swing.app.modul;

import java.util.*;

import hu.mgx.swing.table.*;

public class AppAuth_NotYetUsed
{
    //private Vector vUsers = null;
    private MemoryTable mtUsers = null;
    private MemoryTable mtGroupMembers = null;
    private MemoryTable mtGroups = null;
    private MemoryTable mtGroupPermissions = null;
    private MemoryTable mtPermissions = null;
    private Properties pUsers = null;
    private Properties pGroupMembers = null;
    private Properties pGroups = null;
    private Properties pGroupPermissions = null;
    private Properties pPermissions = null;

    public AppAuth_NotYetUsed()
    {
        init();
    }

    private void init()
    {
        //vUsers = new Vector();
        mtUsers = new MemoryTable(new String[]
                                  {
                                      "id", "username", "password"
                                  });
        mtGroupMembers = new MemoryTable(new String[]
                                         {
                                             "group_id", "user_id"
                                         });
        mtGroups = new MemoryTable(new String[]
                                   {
                                       "id", "name"
                                   });
        mtGroupPermissions = new MemoryTable(new String[]
                                             {
                                                 "group_id", "permission_id"
                                             });
        mtPermissions = new MemoryTable(new String[]
                                        {
                                            "id", "name"
                                        });

        pUsers = new Properties();
        pGroupMembers = new Properties();
        pGroups = new Properties();
        pGroupPermissions = new Properties();
        pPermissions = new Properties();
    }

    public void setUser(int iUser, String sName)
    {
        pUsers.setProperty(Integer.toString(iUser), sName);
    }

    public void setGroupMember(int iGroup, int iUser)
    {
        pGroupMembers.setProperty(Integer.toString(iGroup), Integer.toString(iUser));
    }

    public void setGroup(int iGroup, String sName)
    {
        pGroups.setProperty(Integer.toString(iGroup), sName);
    }

    public void setGroupPermission(int iGroup, int iPermission)
    {
        pGroupPermissions.setProperty(Integer.toString(iGroup), Integer.toString(iPermission));
    }

    public void setPermission(int iPermission, String sName)
    {
        pPermissions.setProperty(Integer.toString(iPermission), sName);
    }

    public boolean hasPermission(int iUser, int iPermission)
    {
        return (true);
    }
}
