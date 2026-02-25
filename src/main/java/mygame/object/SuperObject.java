package mygame.object;

import mygame.mainLogic.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

// parent class for objects used in the game
public class SuperObject {
    public BufferedImage image, image1, image2, image3;
    public String name;
    public boolean collision = false;
    public int x, y;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public void draw(Graphics2D g2, GamePanel gp) {
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }

}
