package mygame.entity;

import mygame.mainLogic.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class Npc extends Entity {

    public Npc(GamePanel gp, int npcType, int x, int y) {
        super(gp);
        this.npcType = npcType;
        direction = "down";
        speed = 1;
        getImage();
        setDialogue();

        this.x = x * gp.tileSize;
        this.y = y * gp.tileSize;
    }

    public void getImage() {
        try {
            if (npcType == 0) {
                npc0 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/npc/npc4.png")));
            } else if (npcType == 1) {
                npc1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/npc/npc5.png")));
            }
            else if (npcType == 2) {
                npc2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/npc/npc1.png")));
            }
            else if (npcType == 3) {
                npc3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/npc/npc2.png")));
            }
            else if (npcType == 4) {
                npc4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/npc/npc3.png")));
            }
            else if (npcType == 5) {
                badGuyLeft = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/badguy/badguy_left.png")));
            }
            else if (npcType == 6) {
                badGuyRight = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/badguy/badguy_right.png")));
            }
            else if (npcType == 7) {
                badGuy = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/badguy/badguy_down_1.png")));
            }
            else if (npcType == 8) {
                sierra = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/npc/sierra.png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDialogue() {
        if (npcType == 4) {
            dialogue = "Random man:\n - Hello there, cowboy!";
        } else if (npcType == 3) {
            dialogue = "Random man:\n - It's a fine day, isn't it?";
        }
        else if (npcType == 1) {
            dialogue = "Random man:\n - How's it going, man?";
        }
        else if (npcType == 0 || npcType == 2) {
            dialogue = "Random man:\n - I'm just standing here.";
        }
        else if (npcType == 5 || npcType == 6 || npcType == 7) {
            dialogue = "Bad guy:\n - Get out of here, man!";
        }
        else if(npcType == 8 && gp.winFight) {
            dialogues[0] = "Cody:\n - Sierra, I've been through \nmazes, fields, and battles \ntoday,";
            dialogues[1] = "Cody:\n - But none of it compares to \nthe thought of standing here, \nhoping you’ll see me for who \nI really am.";
            dialogues[2] = "Sierra:\n - I saw everything you did \ntoday. You didn’t just fight \nfor me, you fought for this \ntown.";
            dialogues[3] = "Sierra:\n - I didn’t think a cowboy \nlike you could be so kind and \nskilled.";
            dialogues[4] = "Sierra:\n - You’ve got my heart, Cody!";
        }
        else if(npcType == 8) {
            dialogue = "Sierra:\n - Hello there, Cody!";
        }
    }

    public void speak() {
        if(npcType == 8 && gp.winFight) {
            gp.ui.currentDialogue = dialogues[dialogueIndex];
        }
        else
            gp.ui.currentDialogue = dialogue;
    }
}
