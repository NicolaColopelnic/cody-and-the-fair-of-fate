package mygame.entity;

import mygame.mainLogic.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class BadGuy extends Entity {
    public BadGuy(GamePanel gp) {
        super(gp);
        name = "bad guy";
        speed = 2;
        type = 1;
        direction = "down";
        maxLife = 3;
        life = maxLife;

        solidArea = new Rectangle();
        solidArea.x = 6;
        solidArea.y = 12;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 26;
        solidArea.height = 26;

        getImage();
    }

    public void getImage() {
        try {
            if (gp.ui != null) {
                up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/badguy/badguy_up_1.png")));
                up2 = ImageIO.read(getClass().getResourceAsStream("/badguy/badguy_up_2.png"));
                down1 = ImageIO.read(getClass().getResourceAsStream("/badguy/badguy_down_1.png"));
                down2 = ImageIO.read(getClass().getResourceAsStream("/badguy/badguy_down_2.png"));
                left1 = ImageIO.read(getClass().getResourceAsStream("/badguy/badguy_left_1.png"));
                left2 = ImageIO.read(getClass().getResourceAsStream("/badguy/badguy_left_2.png"));
                right1 = ImageIO.read(getClass().getResourceAsStream("/badguy/badguy_right_1.png"));
                right2 = ImageIO.read(getClass().getResourceAsStream("/badguy/badguy_right_2.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAction() {

        if(onPath) {
            int goalCol = (gp.player.x + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.y + gp.player.solidArea.y)/gp.tileSize;
            searchPath(goalCol, goalRow);
        }
        else {
            actionLockCounter++;
            if (actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;

                if (i <= 25)
                    direction = "up";
                else if (i > 25 && i <= 50)
                    direction = "down";
                else if (i > 50 && i <= 75)
                    direction = "left";
                else
                    direction = "right";

                actionLockCounter = 0;
            }
        }
    }
}
