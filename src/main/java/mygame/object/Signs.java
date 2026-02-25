package mygame.object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Signs extends SuperObject {
    public int x, y, width, height;
    public BufferedImage image;
    public Rectangle collisionBox;

    public Signs(String imagePath, int tileCol, int tileRow, int tileWidth, int tileHeight, int tileSize, boolean halfTilePosition) {
        this.x = (tileCol * tileSize) + (halfTilePosition ? tileSize / 2 : 0);
        this.y = (tileRow * tileSize) + (halfTilePosition ? tileSize / 2 : 0);
        this.width = tileWidth * tileSize;
        this.height = tileHeight * tileSize;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        }catch(IOException e) {
            e.printStackTrace();
        }
        collision = true;
        collisionBox = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, width, height, null);
    }

}
