package mygame.entity;

import mygame.mainLogic.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class OldMan extends Entity {

    public OldMan(GamePanel gp) {
        super(gp);
        getImage();
        setDialogue();
    }

    public void getImage() {
        try{
            wiseMan = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/npc/wiseman.png")));
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setDialogue() {
        dialogues[0] = "Cody:\n - Dear wise old man, \nmy heart longs for Sierra and \nI just don't know how to \ncharm her!";
        dialogues[1] = "Wise old man:\n - Well, boy, if you want to \nwin Sierra’s heart, you're \ngonna need to do more than \njust shoot the breeze.";
        dialogues[2] = "Wise old man:\n - She’s got a soft spot for \nbrave and kind-hearted men. \nYou need to prove yourself.";
        dialogues[3] = "Cody:\n - How?";
        dialogues[4] = "Wise old man:\n - First, you have to conquer \nthe Enchanted Maze to reach \nthe East Fields.\n";
        dialogues[5] = "Wise old man:\n - You need to grab a torch to \nlight the way!";
        dialogues[6] = "Wise old man:\n - Once there, collect as many \nmagic wildflowers as you can.";
        dialogues[7] = "Wise old man:\n - Finally, you have to show \nup at the town fair tonight \nand impress her with your \nskills.\n";
        dialogues[8] = "Wise old man:\n - Good luck, cowboy!";
    }

    public void speak() {
        gp.ui.currentDialogue = dialogues[dialogueIndex];
    }
}
