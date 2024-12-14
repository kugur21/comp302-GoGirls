package org.RokueLike.domain;

import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.hero.HeroManager;
import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.hall.HallManager;
import org.RokueLike.domain.utils.Direction;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static Timer timer;

    private static Builder builder;
    private static HallManager hallManager;
    private static HallGrid currentHall;
    private static Hero hero;
    private static HeroManager heroManager;
    private static List<Monster> acitveMonsters;

    // Not COMPLETED
    public static void startGame() {

        initBuildMode();
        initPlayMode();

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

            System.out.println(hallNames[i] + " layout with " + objectCount[i] + " objects:");
            builder.printGrid();
            System.out.println();
        }
        hallManager = new HallManager(halls);
    }

    public static void initPlayMode() {
        currentHall = hallManager.getCurrentHall();
        hero = new Hero(currentHall.getStartX(), currentHall.getStartY());
        heroManager = new HeroManager(hero);
        acitveMonsters = currentHall.getMonsters();
    }

    public static void genericLoop() {
        hero.decreaseMotionOffset();
        for (Monster monster: acitveMonsters) {
            monster.decreaseMotionOffset();
        }
    }

    public static void handlePlayerMovement(int dirX, int dirY) {

        try {
            boolean moved = heroManager.moveHero(currentHall, hallManager, dirX, dirY);
            if (!moved) {
                System.out.println("Hero cannot move in that direction.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: Capture Monster movement

    }

    public static void useEnchantment(String enchantment) {
        useEnchantment(enchantment, null);
    }

    public static void useEnchantment(String enchantment, Direction direction) {
        // TODO: Implement this method
    }

    public static void handleLeftClick(int mouseX, int mouseY) {
        // TODO: Implement this method
        System.out.println("Mouse Clicked");
    }

    public static void updateCurrentHall(HallGrid nextHall) {
        currentHall = nextHall;
        hero.setPosition(currentHall.getStartX(), currentHall.getStartY(), false);
        acitveMonsters = currentHall.getMonsters();
    }

    public static Hero getHero() {
        return hero;
    }

}
