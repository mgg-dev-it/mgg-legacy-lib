package hu.mgx.mail;

import java.util.*;

import javax.mail.*;

public class MgxMailReceive
{

    private Session session = null;
    private Store store = null;
    private Folder folder = null;

    public class PopupAuthenticator extends Authenticator
    {

        public PasswordAuthentication getPasswordAuthentication()
        {
            String username = "";
            String password = "";
            return new PasswordAuthentication(username, password);
        }
    }

    public MgxMailReceive()
    {
        init();
    }

    private void init()
    {
        Properties properties = System.getProperties();

        properties.put("mail.pop3.host", "10.40.129.21");

        Authenticator auth = new PopupAuthenticator();

        session = Session.getDefaultInstance(properties, auth);
        try
        {
            store = session.getStore("pop3");
            store.connect();
            folder = store.getDefaultFolder();
            System.out.println("*" + folder.getFullName() + "*");

            Folder[] folders = folder.list();
            for (int i = 0; i < folders.length; i++)
            {
                System.out.println("-" + folders[i].getFullName() + "-");
            }

            folder = store.getFolder("INBOX");
            System.out.println("*" + folder.getFullName() + "*");
            folder.open(Folder.READ_ONLY);
            Message messages[] = folder.getMessages();
            for (int i = 0, n = messages.length; i < n; i++)
            {
                System.out.println(i + ": " + messages[i].getFrom()[0] + "\t" + messages[i].getSubject());
            }
            folder.close(false);
            store.close();
        }
        catch (NoSuchProviderException nspe)
        {
            System.err.println(nspe.getLocalizedMessage());
            nspe.printStackTrace(System.err);
        }
        catch (MessagingException me)
        {
            System.err.println(me.getLocalizedMessage());
            me.printStackTrace(System.err);
        }
    }
}
