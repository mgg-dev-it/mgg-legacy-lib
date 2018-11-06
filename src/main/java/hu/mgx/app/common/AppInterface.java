package hu.mgx.app.common;

import hu.mag.message.MessageHandlerInterface;

public interface AppInterface extends ErrorHandlerInterface, LoggerInterface, FormatInterface, PermissionInterface, VersionInterface, LanguageHandlerInterface, ConnectionHandlerInterface, MessageHandlerInterface {

    public abstract String getAppName();

    public abstract void setGlobal(String sKey, Object oValue);

    public abstract Object getGlobal(String sKey);

    public abstract String getGlobalString(String sKey);

    public abstract String getGlobalStringIsNull(String sKey, String sIfNull);

    public abstract Integer getGlobalInteger(String sKey);
}
