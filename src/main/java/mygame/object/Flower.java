package mygame.object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Flower extends SuperObject {
    public Rectangle collisionBox;
    private final long spawnTime;
    private static final long LIFETIME = 4000;

    public Flower() {
        name = "flower";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/flower.png")));
        } catch(IOException e) {
            e.printStackTrace();
        }
        collision = true;
        collisionBox = new Rectangle();
        collisionBox.x = 0;
        collisionBox.y = 0;
        collisionBox.width = 32;
        collisionBox.height = 32;

        this.spawnTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - spawnTime > LIFETIME;
    }
}
