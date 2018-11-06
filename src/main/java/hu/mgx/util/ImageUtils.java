package hu.mgx.util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author MaG
 */
public abstract class ImageUtils {

    public static ArrayList<BufferedImage> getIconImagesFromResources(Class c, String... sResourceNames) {
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        for (int i = 0; i < sResourceNames.length; i++) {
            java.net.URL imageURL = c.getResource(sResourceNames[i]);
            ImageIcon ii = new ImageIcon(imageURL);
            BufferedImage bi = new BufferedImage(ii.getIconWidth(), ii.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics g = bi.createGraphics();
            ii.paintIcon(null, g, 0, 0);
            //g.dispose();
            images.add(bi);
        }
        return (images);
    }
}
