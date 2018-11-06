package hu.mgx.app.common;

public interface PermissionInterface {

    //public abstract void setUserID(Object oUserID);
    public abstract Object getPermissionUserIdentifier();

    public abstract Object getPermissionUserName();

    public abstract Object getPermissionUserDisplayName();

    public abstract boolean permissionLogin(Object oUserIdentifierOrName, Object oPassword);

    public abstract boolean permissionLogout();

    //public abstract boolean isLoggedIn(Object oUserID);
    public abstract boolean hasPermission(Object oPermission);
}
