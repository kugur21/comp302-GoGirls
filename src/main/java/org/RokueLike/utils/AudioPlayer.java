package org.RokueLike.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private Clip clip;

    // Constructor
    public AudioPlayer() {
        // No initialization needed for now
        System.out.println("AudioPlayer initialized.");
    }

    public void playAudio(String filePath, boolean loop) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio format: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading audio file: " + filePath);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable.");
            e.printStackTrace();
        }
    }

    public void stopAudio() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
