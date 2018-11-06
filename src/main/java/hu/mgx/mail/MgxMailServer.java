package hu.mgx.mail;

import javax.mail.*;

public class MgxMailServer {

    public final static int SMTP = 1;
    public final static int POP3 = 2;
    private int iProtocol = 0;
    private String sHost = null;
    private String sUsername = null;
    private String sPassword = null;

    public class InnerAuthenticator extends Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(sUsername, sPassword);
        }
    }
    private InnerAuthenticator innerAuthenticator = null;

    public MgxMailServer(int iProtocol, String sHost) throws MgxMailException {
        this(iProtocol, sHost, null, null);
    }

    public MgxMailServer(int iProtocol, String sHost, String sUsername, String sPassword) throws MgxMailException {
        if (iProtocol < SMTP || iProtocol > POP3) {
            throw new MgxMailException("Nem megengedett protokoll: " + Integer.toString(iProtocol));
        }
        this.iProtocol = iProtocol;
        this.sHost = sHost;
        this.sUsername = sUsername;
        this.sPassword = sPassword;
        if (sUsername != null && sPassword != null) {
            innerAuthenticator = new InnerAuthenticator();
        }
    }

    public int getProtocol() {
        return (iProtocol);
    }

    public static String getProtocolName(int iProtocol) {
        switch (iProtocol) {
            case SMTP:
                return ("SMTP");
            case POP3:
                return ("POP3");
        }
        return (Integer.toString(iProtocol));
    }

    public java.lang.String getHost() {
        return (sHost);
    }

    public java.lang.String getUsername() {
        return (sUsername);
    }

    public java.lang.String getPassword() {
        return (sPassword);
    }

    public Authenticator getAuthenticator() {
        return (innerAuthenticator);
    }
}
