package mygame.mainLogic;

import mygame.object.Heart;
import mygame.object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

// manages graphical elements such as dialogue windows, title screens, pause screens, and the final screen
public class UI {
    GamePanel gp;
    Graphics2D g2;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int storyNum = 1;
    BufferedImage heart_full, heart_half, heart_blank;
    public Font pixelFont;


    public UI(GamePanel gp) {
        this.gp = gp;
        SuperObject heart = new Heart(gp);
        heart_full = heart.image1;
        heart_half = heart.image2;
        heart_blank = heart.image3;

        customFont();
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;
        g2.setColor(Color.white);

        g2.setFont(pixelFont.deriveFont(18f));

        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        if (gp.gameState == gp.playState) {

            if (gp.currentBg == 2) {
                if (!gp.torch1PickedUp && !gp.torch2PickedUp) {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
                    drawDialogueScreenMaze();
                }
            }

            if (gp.currentBg == 3) {
                if (gp.showDialogue1) {
                    drawDialogueScreenField();
                }
            }

            if (gp.currentBg == 4) {
                if (gp.showDialogue3 && gp.collectedFlowers)
                    drawDialogueScreenFair();
            }

            if (gp.currentBg == 5) {
                drawPlayerLife();

                if (gp.showDialogue2)
                    drawDialogueFinal();
            }
        }

        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        if (gp.gameState == gp.dialogueStateMan) {
            drawDialogue();
        }

        if (gp.gameState == gp.dialogueStateSierra) {
            drawDialogue();
        }

        if (gp.gameState == gp.dialogueStateNpc) {
            drawDialogueScreenNpc();
        }

        if (gp.gameState == gp.finalScreenState) {
            drawFinalScreen();
        }

        if(messageOn) {
            g2.setColor(Color.black);
            g2.drawString(message, gp.getWidth()/3-30 + 4, gp.getHeight()/4 + 4);
            g2.setColor(Color.white);
            g2.drawString(message, gp.getWidth()/3-30, gp.getHeight()/4);
            messageCounter++;

            if(messageCounter > 70) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }

    public void drawPlayerLife() {
        int x = gp.tileSize;
        int y = gp.tileSize;
        int i = 0;

        while(i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        x = gp.tileSize;
        i = 0;

        while(i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }
    }

    private void drawDialogue() {
        int x, y, width, height;

        x = gp.tileSize * 2;
        y = gp.tileSize * 7;
        width = gp.screenWidth - gp.tileSize * 4;
        height = gp.tileSize * 5;
        drawSubWindow(x, y, width, height);
        g2.setFont(pixelFont.deriveFont(18f));

        g2.setColor(Color.white);

        x = gp.tileSize * 3;
        y = gp.tileSize * 8;

        String[] lines = currentDialogue.split("\n");
        int lineHeight = 40;

        for (String line : lines) {
            g2.drawString(line, x, y);
            y += lineHeight;
        }
    }

    public void drawDialogueScreenNpc() {
        int x, y, width, height;

        x = gp.tileSize * 2;
        y = gp.tileSize * 7;
        width = gp.screenWidth - gp.tileSize * 4;
        height = gp.tileSize * 3;

        drawSubWindow(x, y, width, height);

        g2.setFont(pixelFont.deriveFont(20f));

        g2.setColor(Color.white);

        x = gp.tileSize * 3;
        y = gp.tileSize * 8;

        String[] lines = (currentDialogue != null) ? currentDialogue.split("\n") : new String[0];
        int lineHeight = 40;

        for (String line : lines) {
            if (!line.isEmpty()) {
                g2.drawString(line, x, y);
                y += lineHeight;
            }
        }
    }

    public void drawDialogueScreenField() {
        if (!gp.showDialogue1) {
            return;
        }

        int width = gp.screenWidth - gp.tileSize * 4;
        int height = gp.tileSize * 4;
        int x = (gp.screenWidth - width) / 2;
        int y = (gp.screenHeight - height) / 2;

        drawSubWindow(x, y, width, height);

        g2.setFont(pixelFont.deriveFont(18f));
        g2.setColor(Color.white);

        FontMetrics metrics = g2.getFontMetrics();
        int line1Width = metrics.stringWidth("Pick as many flowers as you can,");
        int line2Width = metrics.stringWidth("you need to be faster than them!");

        x = (gp.screenWidth - line1Width) / 2;
        y = gp.tileSize * 7;

        g2.drawString("Pick as many flowers as you can,", x, y);
        x = (gp.screenWidth - line2Width) / 2;
        y += gp.tileSize;
        g2.drawString("you need to be faster than them!", x, y);
    }

    public void drawDialogueScreenFair() {

        int width = gp.screenWidth - gp.tileSize * 4;
        int height = gp.tileSize * 6;
        int x = (gp.screenWidth - width) / 2;
        int y = (gp.screenHeight - height) / 2;

        drawSubWindow(x, y, width, height);

        g2.setFont(pixelFont.deriveFont(18f));
        g2.setColor(Color.white);

        FontMetrics metrics = g2.getFontMetrics();
        int line1Width = metrics.stringWidth("Oh no! the bandits showed up");
        int line2Width = metrics.stringWidth("to cause problems!");
        int line3Width = metrics.stringWidth("You have to defeat them before");
        int line4Width = metrics.stringWidth("going up to Sierra.");

        x = (gp.screenWidth - line1Width) / 2;
        y = gp.tileSize * 6;

        g2.drawString("Oh no! the bandits showed up", x, y);
        y += gp.tileSize;

        x = (gp.screenWidth - line2Width) / 2;

        g2.drawString("to cause problems!", x, y);
        y += gp.tileSize;

        x = (gp.screenWidth - line3Width) / 2;

        g2.drawString("You have to defeat them before", x, y);
        y += gp.tileSize;

        x = (gp.screenWidth - line4Width) / 2;

        g2.drawString("going up to Sierra.", x, y);
    }

    public void drawDialogueFinal() {
        if (!gp.showDialogue2) {
            return;
        }

        int width = gp.screenWidth - gp.tileSize * 4;
        int height = gp.tileSize * 4;
        int x = (gp.screenWidth - width) / 2;
        int y = (gp.screenHeight - height) / 2 + 20;

        drawSubWindow(x, y, width, height);

        g2.setFont(pixelFont.deriveFont(18f));
        g2.setColor(Color.white);

        FontMetrics metrics = g2.getFontMetrics();
        int line1Width = metrics.stringWidth("You did it!");
        int line2Width = metrics.stringWidth("Now go confess your love!");

        x = (gp.screenWidth - line1Width) / 2;
        y = gp.tileSize * 7;

        g2.drawString("You did it!", x, y);
        x = (gp.screenWidth - line2Width) / 2;
        y += gp.tileSize;
        g2.drawString("Now go confess your love!", x, y);
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0,0,0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        Color c2 = new Color(0,0,0, 150);

        g2.setColor(c2);
        g2.setStroke(new BasicStroke(7));
        g2.drawRoundRect(x, y, width, height, 35, 35);
    }

    public void drawDialogueScreenMaze() {
        int x, y, width, height;
        if(gp.currentBg == 2) {
            x = gp.tileSize * 2;
            y = gp.tileSize * 5;
            width = gp.screenWidth - gp.tileSize * 4;
            height = gp.tileSize * 4;
            drawSubWindowMaze(x, y, width, height);
        }

        g2.setFont(pixelFont.deriveFont(20f));
        g2.setColor(Color.white);

        x = gp.tileSize * 4;
        y = gp.tileSize * 7;

        String line = " It's too dark in here!";
        g2.drawString(line, x, y);

    }

    public void drawSubWindowMaze(int x, int y, int width, int height) {

        Color c = new Color(150, 150, 150, 170);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        Color c2 = new Color(150,150,150, 120);

        g2.setColor(c2);
        g2.setStroke(new BasicStroke(7));
        g2.drawRoundRect(x, y, width, height, 35, 35);
    }

    public void drawPauseScreen() {
        g2.setFont(pixelFont.deriveFont(40f));
        String text = "PAUSED";

        int x = getXCentered(text);
        int y = gp.screenHeight / 2;

        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }

    public void drawTitleScreen() {

        gp.tileM.loadMap("/maps/title_map");

        String title = "Cody and the";
        g2.setFont(pixelFont.deriveFont(50f));

        int x = getXCentered(title);
        int y = gp.tileSize * 3;
        // draw shadow:
        g2.setColor(Color.black);
        g2.drawString(title, x + 6, y + 6);

        // draw main text on top
        g2.setColor(Color.white);
        g2.drawString(title, x, y);

        String title2 = "Fair of Fate";
        g2.setFont(pixelFont.deriveFont(50f));

        x = getXCentered(title2);
        y = gp.tileSize * 5;
        g2.setColor(Color.black);
        g2.drawString(title2, x + 6, y + 6);
        g2.setColor(Color.white);
        g2.drawString(title2, x, y);

        // draw the characters on the title screen
        UtilityTool utilityTool = new UtilityTool();
        BufferedImage scaledPlayerImage = utilityTool.scaleImage(gp.player.face, gp.tileSize * 3, gp.tileSize * 3);
        g2.drawImage(scaledPlayerImage, gp.screenWidth / 2 - 80, gp.screenHeight / 2 - 30, null);
        BufferedImage scaledWiseManImage = utilityTool.scaleImage(gp.oldMan.wiseMan, gp.tileSize * 3, gp.tileSize * 3);
        g2.drawImage(scaledWiseManImage, gp.screenWidth / 2 + 150, gp.screenHeight / 2 , null);
        BufferedImage scaledSierraImage = utilityTool.scaleImage(gp.sierra.sierra, gp.tileSize * 3, gp.tileSize * 3);
        g2.drawImage(scaledSierraImage, gp.screenWidth / 2 - 300, gp.screenHeight / 2 , null);

        // menu
        g2.setFont(pixelFont.deriveFont(30f));
        String text = "NEW GAME";
        x = getXCentered(text);
        y += gp.tileSize * 6;

        g2.setColor(Color.black);
        g2.drawString(text, x + 4, y + 4);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        if (commandNum == 0) {
            int arrowX = x - gp.tileSize;
            g2.setColor(Color.black);
            g2.drawString(">", arrowX + 4, y + 4);
            g2.setColor(Color.white);
            g2.drawString(">", arrowX, y);
        }

        text = "QUIT";
        x = getXCentered(text);
        y += gp.tileSize;

        g2.setColor(Color.black);
        g2.drawString(text, x + 4, y + 4);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        if (commandNum == 1) {
            int arrowX = x - gp.tileSize;
            g2.setColor(Color.black);
            g2.drawString(">", arrowX + 4, y + 4);
            g2.setColor(Color.white);
            g2.drawString(">", arrowX, y);
        }
    }

    public void drawFinalScreen() {
        gp.tileM.loadMap("/maps/title_map");

        String title = "You win!";
        g2.setFont(pixelFont.deriveFont(50f));
        int x = getXCentered(title);
        int y = gp.tileSize * 3;
        g2.setColor(Color.black);
        g2.drawString(title, x + 6, y + 6);
        g2.setColor(Color.white);
        g2.drawString(title, x, y);

        String title2 = "Cody and Sierra lived";
        g2.setFont(pixelFont.deriveFont(25f));
        x = getXCentered(title2);
        y = gp.tileSize * 4;
        g2.setColor(Color.black);
        g2.drawString(title2, x + 4, y + 4);
        g2.setColor(Color.white);
        g2.drawString(title2, x, y);

        String title3 = "happily ever after!";
        g2.setFont(pixelFont.deriveFont(25f));
        x = getXCentered(title3);
        y = gp.tileSize * 5;
        g2.setColor(Color.black);
        g2.drawString(title3, x + 4, y + 4);
        g2.setColor(Color.white);
        g2.drawString(title3, x, y);

        // draw the characters on the title screen
        UtilityTool utilityTool = new UtilityTool();
        BufferedImage scaledPlayerImage = utilityTool.scaleImage(gp.player.face, gp.tileSize * 3, gp.tileSize * 3);
        g2.drawImage(scaledPlayerImage, gp.screenWidth / 2 - 80, gp.screenHeight / 2 - 30, null);
        BufferedImage scaledWiseManImage = utilityTool.scaleImage(gp.oldMan.wiseMan, gp.tileSize * 3, gp.tileSize * 3);
        g2.drawImage(scaledWiseManImage, gp.screenWidth / 2 + 150, gp.screenHeight / 2 , null);
        BufferedImage scaledSierraImage = utilityTool.scaleImage(gp.sierra.sierra, gp.tileSize * 3, gp.tileSize * 3);
        g2.drawImage(scaledSierraImage, gp.screenWidth / 2 - 300, gp.screenHeight / 2 , null);

        g2.setFont(pixelFont.deriveFont(25f));
        String text = "made by:";
        x = getXCentered(text);
        y += gp.tileSize * 6;
        g2.setColor(Color.black);
        g2.drawString(text, x + 4, y + 4);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = "Nicola Colopelnic";
        x = getXCentered(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x + 4, y + 4);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

    }

    public int getXCentered(String text) {
        int stringWidth = g2.getFontMetrics().stringWidth(text);
        int x = (gp.screenWidth - stringWidth) / 2;
        return x;
    }

    public void customFont() {
        try {
            pixelFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    Objects.requireNonNull(getClass().getResourceAsStream("/PressStart2P-Regular.ttf"))
            );
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
}


