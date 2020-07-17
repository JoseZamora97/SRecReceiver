package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 * The type Images loader.
 */
public class ImagesLoader {

    private HashMap<String, Image> images;

    /**
     * Instantiates a new Images loader.
     */
    public ImagesLoader () {
        images = new HashMap<>();
        try {
            loadImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the images.
     *
     * <p>
     *     load the images and fill the Map of images icons.
     * </p>
     *
     * @throws IOException if the images doesnt exist.
     */
    private void loadImages() throws IOException {

        ImageIcon ii;

        ii = loadFromResources("images/pc-50.png");
        images.put("connections", getScaledImage(ii.getImage(), 24, 24));

        ii = loadFromResources("images/play-50.png");
        images.put("start", getScaledImage(ii.getImage(), 36, 36));

        ii = loadFromResources("images/reglas-50.png");
        images.put("logs", getScaledImage(ii.getImage(), 24, 24));
    }

    /**
     *
     * @param resource path to the image.
     * @return image icon of the resource loaded.
     * @throws IOException if the images doesnt exist.
     */
    private ImageIcon loadFromResources(String resource) throws IOException {
        return new ImageIcon(ImageIO.read(
                Objects.requireNonNull(
                        getClass().getClassLoader().getResource(resource)
                )
        ));
    }

    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    /**
     * Gets image icon.
     *
     * @param name the name
     * @return the image icon
     */
    public ImageIcon getImageIcon(String name) {
        return new ImageIcon(images.get(name));
    }

}
