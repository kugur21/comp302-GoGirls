package org.RokueLike.domain;

import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.hall.HallManager;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static Timer timer;

    private static Builder builder;
    private static HallManager hallManager;

    // Not COMPLETED
    public static void startGame() {

        initBuildMode();

        timer = new Timer(20, new GameLoop());
        timer.start();

    }

    // For now, we will randomly place the objects in the grid.
    public static void initBuildMode() {
        builder = new Builder();
        List<HallGrid> halls = new ArrayList<>();

        int[] objectCount = {6, 9, 13, 17};
        String[] hallNames = {"Hall of Earth", "Hall of Air", "Hall of Water", "Hall of Fire"};

        for (int i = 0; i < objectCount.length; i++) {
            builder.resetGrid();
            builder.placeObject(objectCount[i]);
            builder.placeHero();

            String[] gridData = builder.getGridData();
            HallGrid hall = new HallGrid(gridData, hallNames[i]);
            halls.add(hall);

            System.out.println(hallNames[i] + " Layout with " + objectCount[i] + " objects:");
            builder.printGrid();
            System.out.println();
        }
        hallManager = new HallManager(halls);
    }

    public static void genericLoop() {
        // TODO Auto-generated method stub
    }
}
