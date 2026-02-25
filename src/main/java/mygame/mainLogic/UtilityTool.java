package mygame.mainLogic;

import java.awt.*;
import java.awt.image.BufferedImage;

// performs image processing operations (for the title & final screens)
public class UtilityTool {

    public BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(originalImage, 0, 0, width, height, null);
        g2.dispose();
        return scaledImage;
    }
}

