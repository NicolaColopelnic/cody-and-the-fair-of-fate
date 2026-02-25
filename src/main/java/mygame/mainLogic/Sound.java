package mygame.mainLogic;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

// manages audio playback in the game, enabling sound effects and background music to be played, looped, or stopped
public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];


    public Sound() {
        soundURL[0] = getClass().getResource("/sounds/song.wav");
        if (soundURL[0] == null) {
            System.out.println("Sound file not found!");
        }
        soundURL[1] = getClass().getResource("/sounds/collect.wav");
        if (soundURL[1] == null) {
            System.out.println("Sound file not found!");
        }
        soundURL[2] = getClass().getResource("/sounds/sword.wav");
        if (soundURL[2] == null) {
            System.out.println("Sound file not found!");}
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception ignored) {
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
