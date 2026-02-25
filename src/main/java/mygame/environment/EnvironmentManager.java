package mygame.environment;

import mygame.mainLogic.GamePanel;

import java.awt.*;
// controls the lighting effect in the maze scene
public class EnvironmentManager {
    GamePanel gp;
    public Lighting lighting;

    public EnvironmentManager(GamePanel gp) {
        this.gp = gp;
    }

    public void setup() {
        lighting = new Lighting(gp, 100);
    }

    public void draw(Graphics2D g2) {
        if (gp.lightingActive) {
            lighting.draw(g2);
        }
    }

}
