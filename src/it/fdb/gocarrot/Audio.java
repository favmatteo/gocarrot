package it.fdb.gocarrot;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Audio {
    private final AudioInputStream audioInputStream;
    private final Clip clip;

    public Audio(String name){
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File("resources/" + name));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void play(){
        clip.start();
    }

    public void stop(){
        clip.stop();
    }
}
