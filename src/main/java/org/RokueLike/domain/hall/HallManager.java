package org.RokueLike.domain.hall;

import java.util.ArrayList;
import java.util.List;

public class HallManager {

    private List<HallGrid> halls;
    private int currentHallIndex;

    public HallManager(List<HallGrid> halls) {
        this.halls = halls;
        this.currentHallIndex = 0;
    }

    public HallGrid getCurrentHall() {
        return halls.get(currentHallIndex);
    }

    public boolean moveToNextHall() {
        if (currentHallIndex < halls.size() - 1) {
            currentHallIndex++;
            return true;
        }
        return false;
    }

}
