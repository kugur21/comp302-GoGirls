package org.RokueLike.domain;

// halların dataları tutar, game managera aktarır
import javax.swing.*;
import java.util.Random;

public class BuildManager {
    private static Timer timer;
    private static final int HALL_WIDTH = 15;
    private static final int HALL_HEIGHT = 15;

    private static String[][] hallOfEarth;
    private static String[][] hallOfAir;
    private static String[][] hallOfWater;
    private static String[][] hallOfFire;

    private static final Random random = new Random();

    public static void init() {
        // Initialize all 4 halls
        System.out.println("[BuildManager]: Initializing build mode...");
        hallOfEarth = createEmptyHall();
        hallOfAir = createEmptyHall();
        hallOfWater = createEmptyHall();
        hallOfFire = createEmptyHall();

        // Add doors
        placeDoor(hallOfEarth);
        placeDoor(hallOfAir);
        placeDoor(hallOfWater);
        placeDoor(hallOfFire);

        timer = new Timer(20, new BuildLoop());
        timer.start();

        System.out.println("[BuildManager]: All halls initialized.");
    }

    /*
    "#" -> walls
    "." -> floor
    "d" -> door
    "o" -> object
    "h" -> hero
     */
    private static String[][] createEmptyHall() {
        String[][] hall = new String[HALL_HEIGHT][HALL_WIDTH];
        for (int y = 0; y < HALL_HEIGHT; y++) {
            for (int x = 0; x < HALL_WIDTH; x++) {
                if (x == 0 || x == HALL_WIDTH - 1 || y == 0 || y == HALL_HEIGHT - 1) {
                    hall[y][x] = "#"; // Walls
                } else {
                    hall[y][x] = "."; // Floor
                }
            }
        }
        return hall;
    }

    private static void placeDoor(String[][] hall) {
        int side = random.nextInt(4); // Random side: 0=top, 1=bottom, 2=left, 3=right
        int position;

        switch (side) {
            case 0: // Top
                position = 1 + random.nextInt(HALL_WIDTH - 2);
                hall[0][position] = "d";
                break;
            case 1: // Bottom
                position = 1 + random.nextInt(HALL_WIDTH - 2);
                hall[HALL_HEIGHT - 1][position] = "d";
                break;
            case 2: // Left
                position = 1 + random.nextInt(HALL_HEIGHT - 2);
                hall[position][0] = "d";
                break;
            case 3: // Right
                position = 1 + random.nextInt(HALL_HEIGHT - 2);
                hall[position][HALL_WIDTH - 1] = "d";
                break;
        }
    }

    public static void placeObjectRandomly(String hallName, int numberOfObjects) {
        String[][] hall = getHall(hallName);
        if (hall == null) return;

        int placedObjects = 0;
        while (placedObjects < numberOfObjects) {
            int x = random.nextInt(HALL_WIDTH);
            int y = random.nextInt(HALL_HEIGHT);

            if (hall[y][x].equals(".")) { // Only place on floor tiles
                hall[y][x] = "o";
                placedObjects++;
            }
        }
        System.out.println("[BuildManager]: Placed " + numberOfObjects + " objects in " + hallName);
    }

    public static void placeObjectManually(String hallName, int x, int y, int objectType) {
        String[][] hall = getHall(hallName);
        if (hall == null) return;

        if (x > 0 && x < HALL_WIDTH - 1 && y > 0 && y < HALL_HEIGHT - 1 && hall[y][x].equals(".")) {
            hall[y][x] = "o" + objectType;
            System.out.println("[BuildManager]: Object type " + objectType + " placed at (" + x + ", " + y + ") in " + hallName);
        } else {
            System.out.println("[BuildManager]: Invalid position or cell not empty.");
        }
    }

    public static void placeHeroRandomly(String[][] hall) {
        int x = random.nextInt(HALL_WIDTH);
        int y = random.nextInt(HALL_HEIGHT);

        int placedHero = 0;
        while (placedHero < 1) {
            if (hall[y][x].equals(".")) { // Only place on floor tiles
                hall[y][x] = "h";
                placedHero++;
            } else {
                x = random.nextInt(HALL_WIDTH);
                y = random.nextInt(HALL_HEIGHT);
            }
        }
    }

    public static String[][] getHall(String hallName) {
        switch (hallName.toLowerCase()) {
            case "earth":
                return hallOfEarth;
            case "air":
                return hallOfAir;
            case "water":
                return hallOfWater;
            case "fire":
                return hallOfFire;
            default:
                System.out.println("[BuildManager]: Invalid hall name: " + hallName);
                return null;
        }
    }

    public static String[][][] getAllHalls() {
        return new String[][][] {hallOfEarth, hallOfAir, hallOfWater, hallOfFire};
    }

    public static String[][] getAllHallsInGridFormat() {
        String[][] hallsInGridFormat = new String[4][HALL_HEIGHT];
        String[][][] allHalls = getAllHalls();

        for (int gridIndex = 0; gridIndex < 4; gridIndex++) {
            for (int rowIndex = 0; rowIndex < HALL_HEIGHT; rowIndex++) {
                StringBuilder rowString = new StringBuilder();
                for (int colIndex = 0; colIndex < HALL_WIDTH; colIndex++) {
                    rowString.append(allHalls[gridIndex][rowIndex][colIndex]);
                }
                hallsInGridFormat[gridIndex][rowIndex] = rowString.toString();
            }
        }
        return hallsInGridFormat;
    }

}
