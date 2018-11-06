package hu.mgx.mail;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

public class MgxMailSend {

    private MgxMailServer mgxMailServer = null;
    private Session session = null;
    private Message message = null;

    public MgxMailSend(MgxMailServer mgxMailServer) throws MgxMailException {
        if (mgxMailServer.getProtocol() != MgxMailServer.SMTP) {
            throw new MgxMailException("Nem megengedett protokoll: " + MgxMailServer.getProtocolName(mgxMailServer.getProtocol()));
        }
        this.mgxMailServer = mgxMailServer;
        init();
    }

    private void init() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", mgxMailServer.getHost());
        session = Session.getDefaultInstance(properties,  mgxMailServer.getAuthenticator());
        message = new MimeMessage(session);
    }

    public void setFrom(String sFrom) throws MessagingException {
        message.setFrom(new InternetAddress(sFrom));
    }

    public void setFrom(String sFrom, String sPersonal) throws MessagingException, UnsupportedEncodingException {
        message.setFrom(new InternetAddress(sFrom, sPersonal));
    }

    public void setFrom(InternetAddress internetAddress) throws MessagingException {
        message.setFrom(internetAddress);
    }

    public void addRecipientTo(String sRecipient) throws MessagingException {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(sRecipient));
    }

    public void addRecipientTo(String sRecipient, String sPersonal) throws MessagingException, UnsupportedEncodingException {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(sRecipient, sPersonal));
    }

    public void addRecipientCc(String sRecipient) throws MessagingException {
        message.addRecipient(Message.RecipientType.CC, new InternetAddress(sRecipient));
    }

    public void addRecipientBcc(String sRecipient) throws MessagingException {
        message.addRecipient(Message.RecipientType.BCC, new InternetAddress(sRecipient));
    }

    public void addRecipient(Message.RecipientType recipientType, String sRecipient) throws MessagingException {
        message.addRecipient(recipientType, new InternetAddress(sRecipient));
    }

    public void addRecipient(Message.RecipientType recipientType, Address address) throws MessagingException {
        message.addRecipient(recipientType, address);
    }

    public void setSubject(String sSubject) throws MessagingException {
        message.setSubject(sSubject);
    }

    public void setContent(Object oContent, String sContentType) throws MessagingException {
        message.setContent(oContent, sContentType);
    }

    public void setContent(Multipart multipart) throws MessagingException {
        message.setContent(multipart);
    }

    public void setText(String sText) throws MessagingException {
        message.setText(sText);
    }

    public void send() throws MessagingException {
        Transport.send(message);
    }
}
