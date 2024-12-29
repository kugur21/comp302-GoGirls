package org.RokueLike.domain.manager;

import org.RokueLike.domain.hall.HallGrid;

import java.util.List;

public class HallManager {

    private final List<HallGrid> halls; // List of all halls
    private int currentHallIndex; // Index of the current hall

    public HallManager(List<HallGrid> halls) {
        this.halls = halls;
        this.currentHallIndex = 0; // Start at the first hall
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

}