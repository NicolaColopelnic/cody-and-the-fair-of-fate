package mygame.environment;

import mygame.mainLogic.GamePanel;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Lighting {
    GamePanel gp;
    BufferedImage darknessFilter;
    int radius;

    public Lighting(GamePanel gp, int radius) {
        this.gp = gp;
        this.radius = radius; // the radius of the illuminated circle around the player
        updateLighting();
    }

    // creates a black rectangle the size of the window, then subtracts from it the circle around the player
    // the circle is calculated to be centered in the players position, so as the player moves, the circle moves
    // with it and a gradient effect is created
    public void updateLighting() {
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.createGraphics();

        Area screenArea = new Area(new Rectangle2D.Double(0, 0, gp.screenWidth, gp.screenHeight));

        int centerX = gp.player.x + gp.tileSize / 2;
        int centerY = gp.player.y + gp.tileSize / 2;

        Shape circle = new Ellipse2D.Double(centerX - (double) radius / 2, centerY - (double) radius / 2, radius, radius);
        Area lightArea = new Area(circle);
        screenArea.subtract(lightArea);

        Color[] color = new Color[5];
        float[] fraction = new float[5];

        color[0] = new Color(0, 0, 0, 0f);
        color[1] = new Color(0, 0, 0, 0.25f);
        color[2] = new Color(0, 0, 0, 0.5f);
        color[3] = new Color(0, 0, 0, 0.75f);
        color[4] = new Color(0, 0, 0);

        fraction[0] = 0f;
        fraction[1] = 0.25f;
        fraction[2] = 0.5f;
        fraction[3] = 0.75f;
        fraction[4] = 1f;

        RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, radius, fraction, color);
        g2.setPaint(gPaint);
        g2.fill(lightArea);
        g2.fill(screenArea);

        g2.dispose();
    }

    public void draw(Graphics2D g2) {
        updateLighting();
        g2.drawImage(darknessFilter, 0, 0, null);
    }
}
