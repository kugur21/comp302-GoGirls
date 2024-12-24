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

        timer = new Timer(10, new BuildLoop());
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
        //The BuildManager class demonstrates the CREATOR PATTERN by taking responsibility for creating and initializing the halls (createEmptyHall)

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
        int side = random.nextInt(2); // 0=top, 1=bottom
        int position;

        switch (side) {
            case 0: // Top
                position = 1 + random.nextInt(HALL_WIDTH - 2); // Sol ve sağ kenarlardan uzak dur
                hall[0][position] = "d";
                break;
            case 1: // Bottom
                position = 1 + random.nextInt(HALL_WIDTH - 2); // Sol ve sağ kenarlardan uzak dur
                hall[HALL_HEIGHT - 1][position] = "d";
                break;
        }
    }


    public static void placeObjectRandomly(String hallName) {
        String[][] hall = getHall(hallName);
        int objectLimit = getHallObjectLimit(hallName);
        if (hall == null) return;

        int placedObjects = getObjectCount(hallName);
        while (placedObjects < objectLimit) {
            int x = random.nextInt(HALL_WIDTH);
            int y = random.nextInt(HALL_HEIGHT);

            if (hall[y][x].equals(".") && notInFrontOfDoor(hall, x, y)) { // Only place on floor tiles
                int objectType = 1 + random.nextInt(6);
                hall[y][x] = "o" + objectType;
                placedObjects++;
            }
        }
        System.out.println("[BuildManager]: Randomly filled " + hallName + " to its object limit.");
    }

    public static void placeObjectManually(String hallName, int x, int y, int objectType) {
        String[][] hall = getHall(hallName);
        int objectLimit = getHallObjectLimit(hallName);
        if (hall == null) return;

        int placedObjects = getObjectCount(hallName);
        if (placedObjects >= objectLimit) {
            System.out.println("[BuildManager]: Cannot place object. Hall is at full capacity.");
            return;
        }

        if (x > 0 && x < HALL_WIDTH - 1 && y > 0 && y < HALL_HEIGHT - 1 && hall[y][x].equals(".") && notInFrontOfDoor(hall, x, y)) {
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

    private static boolean notInFrontOfDoor(String[][] hall, int x, int y) {
        return (x <= 0 || !"d".equals(hall[y][x - 1])) && // Left of door
                (x >= HALL_WIDTH - 1 || !"d".equals(hall[y][x + 1])) && // Right of door
                (y <= 0 || !"d".equals(hall[y - 1][x])) && // Above door
                (y >= HALL_HEIGHT - 1 || !"d".equals(hall[y + 1][x])); // Below door
    }

    public static String[][] getHall(String hallName) {
        return switch (hallName.toLowerCase()) {
            case "earth" -> hallOfEarth;
            case "air" -> hallOfAir;
            case "water" -> hallOfWater;
            case "fire" -> hallOfFire;
            default -> {
                System.out.println("[BuildManager]: Invalid hall name: " + hallName);
                yield null;
            }
        };
    }

    public static int getObjectCount(String hallName) {
        String[][] hall = getHall(hallName);
        if (hall == null) return -1;

        int objectCount = 0;
        for (String[] row : hall) {
            for (String cell : row) {
                if (cell.startsWith("o")) {
                    objectCount++;
                }
            }
        }
        return objectCount;
    }

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

    public static String[][][] getAllHalls() {
        return new String[][][] {hallOfEarth, hallOfAir, hallOfWater, hallOfFire};
    }

}
