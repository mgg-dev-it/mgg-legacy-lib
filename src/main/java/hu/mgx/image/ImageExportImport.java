package hu.mgx.image;

import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import hu.mgx.util.*;

public class ImageExportImport
{

    private javax.swing.Timer timer;
    private hu.mgx.app.swing.AppSplash appSplash;

    public ImageExportImport()
    {
    }

    public byte[] imageImport(java.awt.Component parentComponent)
    {
        byte b[] = new byte[0];
        File f = chooseFileToOpen(parentComponent);
        if (f != null)
        {
            try
            {
                b = FileUtils.readFileIntoByteArray(f);
            /////preview(b);
            }
            catch (FileNotFoundException fnfe)
            {
            }
            catch (IOException ioe)
            {
            }
        }
        return (b);
    }

    public byte[] imageImportTest(java.awt.Component parentComponent)
    {
        byte b[] = new byte[0];
        File f = chooseFileToOpen(parentComponent);
        if (f != null)
        {
            try
            {
                b = FileUtils.readFileIntoByteArray(f);
                preview(b);
            }
            catch (FileNotFoundException fnfe)
            {
            }
            catch (IOException ioe)
            {
            }
        }
        return (b);
    }

    private void preview(byte[] imageData)
    {
        ImageIcon imageIcon = new ImageIcon(imageData);
        appSplash = new hu.mgx.app.swing.AppSplash(imageIcon.getImage(), imageIcon.getIconWidth(), imageIcon.getIconHeight(), "image preview");
        appSplash.setVisible(true);
        timer = new javax.swing.Timer(2000, new ActionListener()
                              {

                                  public void actionPerformed(ActionEvent evt)
                                  {
                                      timer.stop();
                                      appSplash.setVisible(false);
                                      appSplash.dispose();
                                  }
                              });
        timer.setRepeats(false);
        timer.start();
//        JFrame jFrame = new JFrame("Image preview");
//        jFrame.pack();
//        jFrame.setVisible(true);
    }

    private File chooseFileToOpen(java.awt.Component parentComponent)
    {
        JFileChooser jFileChooser = new JFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("gif");
        filter.addExtension("jpg");
        filter.setDescription("Képfájlok");
        jFileChooser.setFileFilter(filter);
        //v47 int iDialogResult = jFileChooser.showSaveDialog(parentComponent);
        int iDialogResult = jFileChooser.showOpenDialog(parentComponent);
        if (iDialogResult != JFileChooser.APPROVE_OPTION)
        {
            return (null);
        }
        File f = jFileChooser.getSelectedFile();
        if (!f.exists())
        {
            JOptionPane.showMessageDialog(parentComponent, "A fájl nem található.");
            return (null);
        }
        return (f);
    }
}
