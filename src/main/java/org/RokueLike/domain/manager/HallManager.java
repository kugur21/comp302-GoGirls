package org.RokueLike.domain.manager;

import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.utils.AudioPlayer;

import java.util.List;

import java.io.Serial;
import java.io.Serializable;

public class HallManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier
    private final List<HallGrid> halls; // List of all halls
    private int currentHallIndex; // Index of the current hall
    private final AudioPlayer audioPlayer;
    private static final String AUDIO1_PATH = "src/main/resources/sounds/degisik2.wav";
    public HallManager(List<HallGrid> halls) {
        this.halls = halls;
        this.currentHallIndex = 0; // Start at the first hall
        this.audioPlayer = new AudioPlayer();
        playBackgroundAudio();
    }

    // Retrieves the current hall being played.
    public HallGrid getCurrentHall() {
        return halls.get(currentHallIndex);
    }

    // Moves to the next hall if available, return true if successful.
    public boolean moveToNextHall() {
        if (currentHallIndex < halls.size() - 1) {
            currentHallIndex++;
            return true;
        }
        return false; // Already at the last hall
    }

    private void playBackgroundAudio() {
        audioPlayer.playAudio(AUDIO1_PATH, true); // Loop audio1 for the whole game
    }

}