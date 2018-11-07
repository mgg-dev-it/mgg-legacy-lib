package hu.mgx.util;

import hu.mgx.app.common.AppInterface;
import hu.mgx.app.common.LoggerInterface;
import hu.mgx.mail.MgxMailException;
import hu.mgx.mail.MgxMailSend;
import hu.mgx.mail.MgxMailServer;
import hu.mgx.swing.table.MemoryTable;
import java.io.UnsupportedEncodingException;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public abstract class MailUtils {

    public MailUtils() {
    }

    public static void SendMail(AppInterface appInterface, String sMailFrom, String sMailFromDisplay, String sMailTo, String sMailToDisplay, String sMailSubject, String sMailText, String sMailAttachment, String sCrLfReplacement, String sSMTP) {
        String sMailToArray[] = null;
        String sMailToDisplayArray[] = null;

        if (sMailFrom.equals("")) {
            appInterface.logLine("Hiba: 'mailfrom' argumentum hiányzik!", LoggerInterface.LOG_ERROR);
            return;
        }
        if (sMailTo.equals("")) {
            appInterface.logLine("Hiba: 'mailto' argumentum hiányzik!", LoggerInterface.LOG_ERROR);
            return;
        }

        sMailToArray = sMailTo.split("\\x7c"); //pipe
        if (!sMailTo.equals("")) {
            for (int i = 0; i < sMailToArray.length; i++) {
                appInterface.logLine("mailto " + Integer.toString(i) + " = " + sMailToArray[i]);
            }
        }

        sMailToDisplayArray = sMailToDisplay.split("\\x7c"); //pipe
        if (!sMailToDisplay.equals("")) {
            for (int i = 0; i < sMailToDisplayArray.length; i++) {
                appInterface.logLine("mailtodisplay " + Integer.toString(i) + " = " + sMailToDisplayArray[i]);
            }
        }

        if (!sMailTo.equals("") && !sMailToDisplay.equals("")) {
            if (sMailToArray.length != sMailToDisplayArray.length) {
                appInterface.logLine("Hiba: 'mailto' argumentum és 'mailtodisplay' argumentumok eltérő számú címzettet tartalmaznak!", LoggerInterface.LOG_ERROR);
                return;
            }
        }

        if (!sCrLfReplacement.equals("")) {
            sMailSubject = StringUtils.stringReplace(sMailSubject, sCrLfReplacement, StringUtils.sCrLf);
        }
        if (sMailSubject.equals("")) {
            appInterface.logLine("Hiba: 'mailsubject' argumentum hiányzik!", LoggerInterface.LOG_ERROR);
            return;
        }
        if (!sCrLfReplacement.equals("")) {
            sMailText = StringUtils.stringReplace(sMailText, sCrLfReplacement, StringUtils.sCrLf);
        }
        if (sMailText.equals("")) {
            appInterface.logLine("Hiba: 'mailtext' argumentum hiányzik!", LoggerInterface.LOG_ERROR);
            return;
        }
        if (sSMTP.equals("")) {
            appInterface.logLine("Hiba: 'smtp' argumentum hiányzik!", LoggerInterface.LOG_ERROR);
            return;
        }

        MgxMailServer mailServer;
        MgxMailSend mailSend;

        try {
            mailServer = new MgxMailServer(MgxMailServer.SMTP, sSMTP); //"10.40.129.21"
            mailSend = new MgxMailSend(mailServer);
            if (sMailFromDisplay.equals("")) {
                mailSend.setFrom(sMailFrom);
            } else {
                mailSend.setFrom(sMailFrom, sMailFromDisplay);
            }

            for (int i = 0; i < sMailToArray.length; i++) {
                if (sMailToDisplay.equals("")) {
                    mailSend.addRecipientTo(sMailToArray[i]);
                } else {
                    mailSend.addRecipientTo(sMailToArray[i], sMailToDisplayArray[i]);
                }
            }

            mailSend.setSubject(sMailSubject);

            if (sMailAttachment.equals("")) {
                mailSend.setContent(sMailText, "text/html; charset=iso-8859-2");
                //mailSend.setText(sMailText);
            } else {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(sMailText);
                //mailSend.setText(sMailText);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();
                //String filename = "file.txt";
                DataSource source = new FileDataSource(sMailAttachment);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(sMailAttachment);
                multipart.addBodyPart(messageBodyPart);

                MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
                mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
                mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
                mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
                mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
                mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
                CommandMap.setDefaultCommandMap(mc);

                mailSend.setContent(multipart);
            }
            mailSend.send();
        } catch (MgxMailException ex) {
            appInterface.handleError(ex);
        } catch (MessagingException ex2) {
            appInterface.handleError(ex2);
        } catch (UnsupportedEncodingException ex3) {
            appInterface.handleError(ex3);
        }

    }

    public static void sendMemoryTable(AppInterface appInterface, MemoryTable memoryTable, String sMailFrom, String sMailFromDisplay, String sMailTo, String sMailToDisplay, String sMailSubject, String sSMTP) {
        String sHtmlText = "";
        String sTableRow = "";
        String sColor = "";

        sHtmlText += "<html>";
        sHtmlText += "<head>";
        sHtmlText += "<title>" + sMailSubject + "</title>";
        sHtmlText += "</head>";
        sHtmlText += "<body bgcolor='#E0F1FF'>";

        appInterface.logLine("----------");
        sHtmlText += "<table border='1' cellpadding='2' cellspacing='1'>";
        sHtmlText += "<tr>";
        for (int i = 0; i < memoryTable.getColumnCount(); i++) {
            if (!memoryTable.getColumnName(i).startsWith("html_")) {
                sHtmlText += "<th nowrap>" + memoryTable.getColumnName(i) + "</th>";
            }
        }
        sHtmlText += "</tr>";
        for (int i = 0; i < memoryTable.getRowCount(); i++) {
            sTableRow = "<tr>";
            sColor = "";
            for (int j = 0; j < memoryTable.getColumnCount(); j++) {
                if (!memoryTable.getColumnName(j).startsWith("html_")) {
                    sTableRow += "<td nowrap>" + StringUtils.isNull(memoryTable.getValueAt(i, j), "&nbsp;") + "</td>";
                } else {
                    if (memoryTable.getColumnName(j).startsWith("html_row_color")) {
                        sColor += StringUtils.isNull(memoryTable.getValueAt(i, j), "");
                    }
                }
            }
            sTableRow += "</tr>";
            if (!sColor.equals("")) {
                sTableRow = StringUtils.stringReplace(sTableRow, "<tr>", "<tr bgcolor='" + sColor + "'>");
            }
            sHtmlText += sTableRow;
        }
        sHtmlText += "</table>";

        appInterface.logLine("----------");

        sHtmlText += "</body>";
        sHtmlText += "</html>";
        appInterface.logLine(sHtmlText);

        SendMail(appInterface, sMailFrom, sMailFromDisplay, sMailTo, sMailToDisplay, sMailSubject, sHtmlText, "", "", sSMTP);
    }
}
