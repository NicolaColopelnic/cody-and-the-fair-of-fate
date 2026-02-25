package mygame.entity;

import mygame.mainLogic.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

// parent class for entities (characters) in the game
public class Entity {
    GamePanel gp;
    public int x, y;
    public int speed;
    public String name;

    // life measurements:
    public int life, maxLife;
    public boolean invincible = false;
    public int invincibleCounter = 0;

    // characters images:
    public BufferedImage up1, up2, down1, down2, right1, right2, left1, left2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage face, sierra, wiseMan, badGuyLeft, badGuyRight, badGuy;
    public BufferedImage npc0, npc1, npc2, npc3, npc4;
    public BufferedImage currentSprite;

    // movement and collision detection:
    public String direction;
    public int actionLockCounter = 0;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public boolean collisionOn = false;
    public boolean onPath = false;

    // attack:
    public boolean attacking = false;
    public Rectangle attackArea = new Rectangle(0,0,36,36);

    // dialogues:
    public String[] dialogues = new String[20];
    public int dialogueIndex = 0;
    public String dialogue = new String();

    public int npcType;
    public int type; // 0 - player, 1 - bad guy

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() { }

    public void setDirection(String direction) { }

    public void speak() { }

    public void setPosition(int tileX, int tileY) {
        this.x = tileX * gp.tileSize;
        this.y = tileY * gp.tileSize;
    }

    public void checkCollision() {
        collisionOn = false;
        gp.collisionCheck.checkTile(this);
        gp.collisionCheck.checkObject(this, false);
        boolean contactPlayer = gp.collisionCheck.checkPlayer(this);

        // player damage:
        if(this.type == 1 && contactPlayer) {
            if(!gp.player.invincible) {
                gp.player.life--;
                gp.player.invincible = true;
            }
        }
    }

    public void move() {
        if (!collisionOn) {
            switch (direction) {
                case "up":    y -= speed; break;
                case "down":  y += speed; break;
                case "left":  x -= speed; break;
                case "right": x += speed; break;
            }
        }
    }

    public void update() {
        if (onPath) {
            // player current tile
            int goalCol = (gp.player.x + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.y + gp.player.solidArea.y) / gp.tileSize;

            searchPath(goalCol, goalRow);
        } else {
            setAction();
        }

        checkCollision();

        move();

        spriteCounter++;

        if (spriteCounter > 10) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    // enemy following the player:
    public void searchPath(int goalCol, int goalRow) {
        int startCol = (x + solidArea.x) / gp.tileSize;
        int startRow = (y + solidArea.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if (gp.pFinder.search() && !gp.pFinder.pathList.isEmpty()) {

            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;

            if (nextRow < startRow) {
                direction = "up";
            } else if (nextRow > startRow) {
                direction = "down";
            } else if (nextCol < startCol) {
                direction = "left";
            } else if (nextCol > startCol) {
                direction = "right";
            }

            checkCollision();

            if (collisionOn) {
                if (direction.equals("up") || direction.equals("down")) {
                    direction = (nextCol < startCol) ? "left" : "right";
                } else {
                    direction = (nextRow < startRow) ? "up" : "down";
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1)
                    image = up1;
                if (spriteNum == 2)
                    image = up2;
                break;

            case "down":
                if (spriteNum == 1)
                    image = down1;
                if (spriteNum == 2)
                    image = down2;
                break;

            case "left":
                if (spriteNum == 1)
                    image = left1;
                if (spriteNum == 2)
                    image = left2;
                break;

            case "right":
                if (spriteNum == 1)
                    image = right1;
                if (spriteNum == 2)
                    image = right2;
                break;
        }
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void drawOldMan(Graphics2D g2) {
        g2.drawImage(wiseMan, x, y, gp.tileSize, gp.tileSize, null);
    }

    public void drawStaticNpc(Graphics2D g2) {
        if(npcType == 0)
            g2.drawImage(npc0, x, y, gp.tileSize, gp.tileSize, null);
        if(npcType == 1)
            g2.drawImage(npc1, x, y, gp.tileSize, gp.tileSize, null);
        if(npcType == 2)
            g2.drawImage(npc2, x, y, gp.tileSize, gp.tileSize, null);
        if(npcType == 3)
            g2.drawImage(npc3, x, y, gp.tileSize, gp.tileSize, null);
        if(npcType == 4)
            g2.drawImage(npc4, x, y, gp.tileSize, gp.tileSize, null);
        if(npcType == 5)
            g2.drawImage(badGuyLeft, x, y, gp.tileSize, gp.tileSize, null);
        if(npcType == 6)
            g2.drawImage(badGuyRight, x, y, gp.tileSize, gp.tileSize, null);
        if(npcType == 7)
            g2.drawImage(badGuy, x, y, gp.tileSize, gp.tileSize, null);
        if(npcType == 8)
            g2.drawImage(sierra, x, y, gp.tileSize, gp.tileSize, null);
    }

    public void drawStaticBadGuy(Graphics2D g2) {
        g2.drawImage(down1, x, y, gp.tileSize, gp.tileSize, null);
    }
}
