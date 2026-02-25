package mygame.entity;

import mygame.mainLogic.GamePanel;
import mygame.mainLogic.KeyHandler;
import mygame.mainLogic.Sound;
import mygame.mainLogic.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    KeyHandler keyH;
    UtilityTool uTool;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;
        maxLife = 10;
        life = maxLife;
        type = 0;
        this.uTool = new UtilityTool();
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        solidArea = new Rectangle();
        solidArea.x = 6;
        solidArea.y = 12;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 26;
        solidArea.height = 26;
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        maxLife = 6;
        life = maxLife;
        direction = "down";
    }

    public void setDirection(String direction) {
        this.direction = direction;
        switch (direction) {
            case "up":
                currentSprite = up1;
                break;
            case "down":
                currentSprite = down1;
                break;
            case "left":
                currentSprite = left1;
                break;
            case "right":
                currentSprite = right1;
                break;
        }
    }

    public void update() {
        if(attacking) {
            attacking();
        }
        else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
            }

            if (keyH.downPressed) {
                direction = "down";
            }

            if (keyH.leftPressed) {
                direction = "left";
            }

            if (keyH.rightPressed) {
                direction = "right";
            }

            collisionOn = false;
            gp.collisionCheck.checkTile(this);

            int objectIndex = gp.collisionCheck.checkObject(this, true);
            if (objectIndex != 999) {
                pickUpObject(objectIndex);
            }

            int flowerIndex = gp.collisionCheck.checkFlower(this, true);
            if (flowerIndex != 999) {
                pickUpFlower(flowerIndex);
            }

            int oldManIndex = gp.collisionCheck.checkMan(this, gp.oldMan);
            interactMan(oldManIndex);

            int npcIndex = gp.collisionCheck.checkEntity(this, gp.npcs);
            interactNpc(npcIndex);

            int signIndex = gp.collisionCheck.checkSign(this, gp.signs);
            if (signIndex != 999) {
                collisionOn = true;
            }

            int badGuyIndex = gp.collisionCheck.checkMan(this, gp.badGuy);
            if (badGuyIndex != 0) {
                if (gp.changeScene) {
                    gp.currentBg = 5;
                    gp.player.x = 13 * gp.tileSize;
                    gp.player.y = 11 * gp.tileSize;
                }
                contactBadGuy(badGuyIndex);
            }

            move();

            spriteCounter++;
            if (spriteCounter > 8) {
                if (spriteNum == 1)
                    spriteNum = 2;
                else if (spriteNum == 2)
                    spriteNum = 1;
                spriteCounter = 0;
            }
        }
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    // object/entity interactions:
    public void attacking() {
        spriteCounter++;
        if(spriteCounter <= 5) {
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            int currentX = x;
            int currentY = y;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            switch (direction) {
                case "up":
                    y -= attackArea.height;
                    break;
                case "down":
                    y += attackArea.height;
                    break;
                case "left":
                    x -= attackArea.width;
                    break;
                case "right":
                    x += attackArea.width;
                    break;
            }

            solidArea.height = attackArea.height;
            solidArea.width = attackArea.width;

            int badGuyIndex = gp.collisionCheck.checkMan(this, gp.badGuy);
            if(gp.fightMode)
                damage(badGuyIndex);
            x = currentX;
            y = currentY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if(spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void damage(int i) {
        if(i != 0) {
            if(!gp.badGuy.invincible) {
                gp.badGuy.life--;
                gp.badGuy.invincible = true;

                if(gp.badGuy.life <= 0) {
                    gp.badGuy.onPath = false;
                    gp.badGuy = null;
                    gp.winFight = true;
                    gp.showDialogue2 = true;
                }
                if(this.life <= 0) {
                    assert gp.badGuy != null;
                    gp.badGuy.onPath = false;
                    gp.winFight = false;
                    gp.currentBg = 4;
                    this.life = 6;
                    gp.ui.showMessage("Try again!");
                }
            }
            gp.ui.showMessage("Hit!");
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String objectName = gp.objects[i].name;

            if (objectName.equals("torch")) {
                if (i == 0)
                    gp.torch1PickedUp = true;
                if (i == 1)
                    gp.torch2PickedUp = true;

                Sound torchSound = new Sound();
                torchSound.setFile(1);
                torchSound.play();
                gp.ui.showMessage("Torch picked up!");
            }
        }
    }

    public void pickUpFlower(int i) {
        if (i != 999) {
            if (gp.currentBg == 3) {
                gp.flowerCount++;
                Sound flowerSound = new Sound();
                flowerSound.setFile(1);
                flowerSound.play();
                gp.ui.showMessage("You picked up a flower! Total: " + gp.flowerCount);
                gp.flowers[i] = null;
            }
        }
    }

    public void interactNpc(int i) {
        if(i != 999) {
            if(i == 10 && gp.collectedFlowers) {
                gp.currentBg = 5;
                gp.player.x = 13 * gp.tileSize;
                gp.player.y = 11 * gp.tileSize;
            }
            else if(i == 11 && gp.winFight) {
                gp.gameState = gp.dialogueStateSierra;
                gp.npcs[i].speak();
            }
            else {
                gp.gameState = gp.dialogueStateNpc;
                gp.npcs[i].speak();
            }
        }
    }

    public void interactMan(int i) {
        if(i != 0) {
            gp.gameState = gp.dialogueStateMan;
            gp.oldMan.speak();
        }
    }

    public void contactBadGuy(int i) {
        if(i != 999) {
            if(!invincible)
                life--;
            invincible = true;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int drawWidth = gp.tileSize;
        int drawHeight = gp.tileSize;
        int tempX = x;
        int tempY = y;

        switch (direction) {
            case "up":
                if (attacking) {
                    tempY = y - gp.tileSize;
                    if (spriteNum == 1) {
                        image = attackUp1;
                    } else if (spriteNum == 2) {
                        image = attackUp2;
                    }
                    drawWidth = gp.tileSize;
                    drawHeight = gp.tileSize * 2;
                } else {
                    if (spriteNum == 1) {
                        image = up1;
                    } else if (spriteNum == 2) {
                        image = up2;
                    }
                }
                break;

            case "down":
                if (attacking) {
                    if (spriteNum == 1) {
                        image = attackDown1;
                    } else if (spriteNum == 2) {
                        image = attackDown2;
                    }
                    drawHeight = gp.tileSize * 2;
                } else {
                    if (spriteNum == 1) {
                        image = down1;
                    } else if (spriteNum == 2) {
                        image = down2;
                    }
                }
                break;

            case "left":
                if (attacking) {
                    tempX = x - gp.tileSize;
                    if (spriteNum == 1) {
                        image = attackLeft1;
                    } else if (spriteNum == 2) {
                        image = attackLeft2;
                    }
                    drawWidth = gp.tileSize * 2;
                } else {
                    if (spriteNum == 1) {
                        image = left1;
                    } else if (spriteNum == 2) {
                        image = left2;
                    }
                }
                break;

            case "right":
                if (attacking) {
                    if (spriteNum == 1) {
                        image = attackRight1;
                    } else if (spriteNum == 2) {
                        image = attackRight2;
                    }
                    drawWidth = gp.tileSize * 2;
                } else {
                    if (spriteNum == 1) {
                        image = right1;
                    } else if (spriteNum == 2) {
                        image = right2;
                    }
                }
                break;
        }

        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        }

        g2.drawImage(image, tempX, tempY, drawWidth, drawHeight, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void getPlayerImage() {
        try{
            if(gp.ui != null && gp.ui.storyNum == 1) {
                up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_up_1.png")));
                up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_up_2.png")));
                down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_down_1.png")));
                down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_down_2.png")));
                left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_left_1.png")));
                left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_left_2.png")));
                right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_right_1.png")));
                right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_right_2.png")));
                face = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_face.png")));
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void getPlayerAttackImage() {
        try{
            if(gp.ui != null && gp.ui.storyNum == 1) {
                attackUp1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_attack_up_1.png")));
                attackUp1 = uTool.scaleImage(attackUp1, gp.tileSize, gp.tileSize*2);
                attackUp2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_attack_up_2.png")));
                attackUp2 = uTool.scaleImage(attackUp2, gp.tileSize, gp.tileSize*2);
                attackDown1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_attack_down_1.png")));
                attackDown1 = uTool.scaleImage(attackDown1, gp.tileSize*2, gp.tileSize*2);
                attackDown2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_attack_down_2.png")));
                attackDown2 = uTool.scaleImage(attackDown2, gp.tileSize*2, gp.tileSize*2);
                attackLeft1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_attack_left_1.png")));
                attackLeft1 = uTool.scaleImage(attackLeft1, gp.tileSize*2, gp.tileSize);
                attackLeft2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_attack_left_2.png")));
                attackLeft2 = uTool.scaleImage(attackLeft2, gp.tileSize*2, gp.tileSize);
                attackRight1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_attack_right_1.png")));
                attackRight1 = uTool.scaleImage(attackRight1, gp.tileSize*2, gp.tileSize);
                attackRight2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/cowboy_attack_right_2.png")));
                attackRight2 = uTool.scaleImage(attackRight2, gp.tileSize*2, gp.tileSize);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}

