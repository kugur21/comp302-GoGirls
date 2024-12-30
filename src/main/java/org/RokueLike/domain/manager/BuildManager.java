package org.RokueLike.domain.manager;

import org.RokueLike.domain.loop.BuildLoop;
import org.RokueLike.utils.MessageBox;

import javax.swing.*;
import java.util.Random;

import static org.RokueLike.utils.Constants.*;

public class BuildManager {

    // Halls representing different elements
    private static String[][] hallOfEarth;
    private static String[][] hallOfAir;
    private static String[][] hallOfWater;
    private static String[][] hallOfFire;
    private static String[][] closureHall;

    private static MessageBox messageBox;
    private static final Random random = new Random();

    // Initializes all halls and their elements.
    public static void init() {
        System.out.println("[BuildManager]: Initializing build mode...");
        messageBox = new MessageBox();

        hallOfEarth = createEmptyHall();
        hallOfAir = createEmptyHall();
        hallOfWater = createEmptyHall();
        hallOfFire = createEmptyHall();
        closureHall = createEmptyHall();

        // Add doors
        placeDoor(hallOfEarth);
        placeDoor(hallOfAir);
        placeDoor(hallOfWater);
        placeDoor(hallOfFire);


        Timer timer = new Timer(10, new BuildLoop());
        timer.start();
        System.out.println("[BuildManager]: All halls initialized.");
    }

    //// The BuildManager class demonstrates the CREATOR PATTERN by taking responsibility for creating and initializing the halls (createEmptyHall)
    // Creates an empty hall with walls and floor.
    private static String[][] createEmptyHall() {
        String[][] hall = new String[GRID_HEIGHT][GRID_WIDTH];
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (x == 0 || x == GRID_WIDTH - 1 || y == 0 || y == GRID_HEIGHT - 1) {
                    hall[y][x] = "#"; // Walls
                } else {
                    hall[y][x] = "."; // Floor
                }
            }
        }
        return hall;
    }

    // Places a door at a random position on the top or bottom wall.
    private static void placeDoor(String[][] hall) {
        int side = random.nextInt(2); // 0=top, 1=bottom
        int position;

        switch (side) {
            case 0: // Top
                position = 1 + random.nextInt(GRID_WIDTH - 2); // Avoid corners
                hall[0][position] = "d";
                break;
            case 1: // Bottom
                position = 1 + random.nextInt(GRID_WIDTH - 2); // Avoid corners
                hall[GRID_HEIGHT - 1][position] = "d";
                break;
        }
    }

    // Places objects randomly in a hall until the object limit is reached.
    public static void placeObjectRandomly(String hallName) {
        String[][] hall = getHall(hallName);
        int objectLimit = getHallObjectLimit(hallName);
        if (hall == null) return;

        int placedObjects = getObjectCount(hallName);
        while (placedObjects < objectLimit) {
            int x = random.nextInt(GRID_WIDTH);
            int y = random.nextInt(GRID_HEIGHT);

            if (hall[y][x].equals(".") && notInFrontOfDoor(hall, x, y)) { // Only place on valid tiles
                int objectType = 1 + random.nextInt(6); // Randomize object type
                hall[y][x] = "o" + objectType;
                placedObjects++;
            }
        }
        System.out.println("[BuildManager]: Randomly filled " + hallName + " to its object limit.");
    }

    // Manually places an object in a specified position.
    public static void placeObjectManually(String hallName, int x, int y, int objectType) {
        String[][] hall = getHall(hallName);
        int objectLimit = getHallObjectLimit(hallName);
        if (hall == null) return;

        int placedObjects = getObjectCount(hallName);
        if (placedObjects >= objectLimit) {
            messageBox.addMessage("Can't place object. Hall is at full capacity.", 1);
            return;
        }

        if (x > 0 && x < GRID_WIDTH - 1 && y > 0 && y < GRID_HEIGHT - 1 && hall[y][x].equals(".") && notInFrontOfDoor(hall, x, y)) {
            hall[y][x] = "o" + objectType;
        } else {
            System.out.println("[BuildManager]: Invalid position or cell not empty.");
        }
    }

    // Places the hero randomly on a valid tile.
    public static void placeHeroRandomly(String[][] hall) {
        int x = random.nextInt(GRID_WIDTH);
        int y = random.nextInt(GRID_HEIGHT);

        int placedHero = 0;
        while (placedHero < 1) {
            if (hall[y][x].equals(".")) { // Only place on floor tiles
                hall[y][x] = "h";
                placedHero++;
            } else {
                x = random.nextInt(GRID_WIDTH);
                y = random.nextInt(GRID_HEIGHT);
            }
        }
    }

    // Retrieves the hall data by its name.
    public static String[][] getHall(String hallName) {
        return switch (hallName.toLowerCase()) {
            case "earth" -> hallOfEarth;
            case "air" -> hallOfAir;
            case "water" -> hallOfWater;
            case "fire" -> hallOfFire;
            case "closure" -> closureHall;
            default -> {
                System.out.println("[BuildManager]: Invalid hall name: " + hallName);
                yield null;
            }
        };
    }

    // Counts the number of objects placed in a hall.
    public static int getObjectCount(String hallName) {
        String[][] hall = getHall(hallName);
        if (hall == null) return -1;

        int objectCount = 0;
        for (String[] row : hall) {
            for (String cell : row) {
                if (cell.startsWith("o")) { // Object cells start with "o"
                    objectCount++;
                }
            }
        }
        return objectCount;
    }

    // Returns the object limit for a given hall.
    public static int getHallObjectLimit(String hallName) {
        return switch (hallName.toLowerCase()) {
            case "earth" -> 6;
            case "air" -> 9;
            case "water" -> 13;
            case "fire" -> 17;
            default -> {
                System.out.println("[BuildManager]: Invalid hall name: " + hallName);
                yield -1;
            }
        };
    }

    // Returns all halls as a 3D array.
    public static String[][][] getAllHalls() {
        return new String[][][] {hallOfEarth, hallOfAir, hallOfWater, hallOfFire};
    }

    public static MessageBox getMessageBox() {
        return messageBox;
    }

    // Checks if a position is not adjacent to a door.
    private static boolean notInFrontOfDoor(String[][] hall, int x, int y) {
        return (x <= 0 || !"d".equals(hall[y][x - 1])) && // Left of door
                (x >= GRID_WIDTH - 1 || !"d".equals(hall[y][x + 1])) && // Right of door
                (y == 0 || !"d".equals(hall[y - 1][x])) && // Above door
                (y >= GRID_HEIGHT - 1 || !"d".equals(hall[y + 1][x])); // Below door
    }

}