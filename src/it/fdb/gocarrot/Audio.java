package it.fdb.gocarrot;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Audio {
    private final String name;
    private final AudioInputStream audioInputStream;
    private final Clip clip;

    public Audio(String name){
        this.name = name;
        try {
            //String song = String.valueOf(getClass().getResource("resources/" + name));
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
