package org.RokueLike.domain.hall;

import org.RokueLike.domain.model.entity.EntityCell;
import org.RokueLike.domain.model.item.Door;
import org.RokueLike.domain.model.item.Enchantment;
import org.RokueLike.domain.model.item.Object;
import org.RokueLike.domain.model.entity.monster.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HallGrid {

    // Hero's starting coordinates
    private int startX;
    private int startY;

    private final String name; // Name of the hall
    private final GridCell[][] grid; // 2D array representing the grid layout
    private List<Monster> monsters; // List of monsters in the hall
    private List<Object> objects; // List of objects in the hall
    private Enchantment currentEnchantment; // Uncollected enchantment in the hall
    private final Random random = new Random();

    // Initializes the hall with the provided grid data and name.
    public HallGrid(String[][] gridData, String name) {
        this.name = name;
        grid = new GridCell[gridData.length][gridData[0].length];
        initGrid(gridData);
    }

    // Parses grid data to initialize walls, floors, doors, and objects.
    private void initGrid(String[][] gridData) {
        this.objects = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.currentEnchantment = null;

        for (int i = 0; i < gridData.length; i++) {
            for (int j = 0; j < gridData[i].length; j++) {
                String cellValue = gridData[i][j];

                switch (cellValue) {
                    case "#":
                        grid[i][j] = new GridCell("wall", j, i);
                        break;
                    case ".":
                        grid[i][j] = new GridCell("floor", j, i);
                        break;
                    case "d":
                        grid[i][j] = new Door(j, i);
                        break;
                    case "h":
                        grid[i][j] = new GridCell("floor", j, i);
                        this.startX = j;
                        this.startY = i;
                        break;
                    default:
                        try {
                            // Handle objects (e.g., "o1", "o2", ..., "o6")
                            if (cellValue.startsWith("o")) {
                                int objectType = Integer.parseInt(cellValue.substring(1)); // Extract object type
                                if (objectType >= 1 && objectType <= 6) {
                                    String objectName = "object" + objectType; // Create object name
                                    Object object = new Object(objectName, j, i); // Initialize object
                                    grid[i][j] = object;
                                    objects.add(object);
                                } else {
                                    System.err.println("Invalid object type: " + cellValue + " at (" + j + ", " + i + ")");
                                    grid[i][j] = new GridCell("floor", j, i); // Default to floor
                                }
                            } else {
                                System.err.println("Unrecognized grid value: " + cellValue + " at (" + j + ", " + i + ")");
                                grid[i][j] = new GridCell("floor", j, i); // Default to floor
                            }
                        } catch (Exception e) {
                            System.err.println("Error while initializing grid cell at (" + j + ", " + i + "): " + e.getMessage());
                            grid[i][j] = new GridCell("floor", j, i); // Default to floor
                        }
                        break;
                }
            }
        }
        initRune();
    }

    // Randomly places a rune in one of the objects.
    public void initRune() {
        if (objects.isEmpty()) {
            throw new IllegalStateException("No objects available for placing the rune.");
        }
        Object randomObject = objects.get(random.nextInt(objects.size()));
        randomObject.setContainedRune();
    }

    // Opens a door if it is closed.
    public void openDoor() {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                GridCell cell = grid[y][x];
                if (cell instanceof Door door) {
                    if (!door.isOpen()) {
                        door.open(); // Open the door
                        return;
                    }
                }
            }
        }
    }

    // Relocates the rune to another object, wizard behaviour calls this function.
    public void changeRuneLocation() {
        boolean runeAvailable = false;
        for (Object object : objects) {
            if (object.containsRune()) {
                object.removeContainedRune();
                runeAvailable = true;
                break;
            }
        }
        if (runeAvailable) {
            Object randomObject = objects.get(random.nextInt(objects.size()));
            randomObject.setContainedRune();
        }
    }

    // Returns a region containing the rune with a bounded area.
    public int[][] findRuneRegion(int bound) {
        for (Object object : objects) {
            if (object.containsRune()) {
                int runeX = object.getPositionX();
                int runeY = object.getPositionY();

                int startX = Math.max(0, runeX - random.nextInt(bound));
                int startY = Math.max(0, runeY - random.nextInt(bound));
                int endX = Math.min(getWidth() - 1, startX + 3);
                int endY = Math.min(getHeight() - 1, startY + 3);

                if (endX - startX < 3) {
                    startX = Math.max(0, endX - 3);
                }
                if (endY - startY < 3) {
                    startY = Math.max(0, endY - 3);
                }

                List<int[]> region = new ArrayList<>();
                for (int y = startY; y <= endY; y++) {
                    for (int x = startX; x <= endX; x++) {
                        region.add(new int[]{x, y});
                    }
                }
                return region.toArray(new int[0][0]);
            }
        }
        return null;
    }

    // Retrieves a grid cell at the specified coordinates.
    public GridCell getCell(int positionX, int positionY) {
        if (positionX >= 0 && positionX < getWidth() && positionY >= 0 && positionY < getHeight()) {
            return grid[positionY][positionX];
        }
        throw new IllegalArgumentException("Coordinates out of bounds: (" + positionX + ", " + positionY + ")");
    }

    public GridCell getCellInFront(EntityCell entity, int directionX, int directionY) {
        return getCell(entity.getPositionX() + directionX, entity.getPositionY() + directionY);
    }

    // Checks if a location is safe.
    public boolean isSafeLocation(int positionX, int positionY) {
        return getCell(positionX, positionY).getName().equals("floor") && noMonsterThere(positionX, positionY);
    }

    public boolean isSafeLocation(EntityCell entity, int directionX, int directionY) {
        return isSafeLocation(entity.getPositionX() + directionX, entity.getPositionY() + directionY);
    }

    // Finds a random safe cell.
    public int[] findRandomSafeCell() {
        Random random = new Random();
        List<int[]> floorTiles = new ArrayList<>();

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (getCell(x, y).getName().equals("floor") && noMonsterThere(x, y) && notInFrontOfTheDoor(x, y)) {
                    floorTiles.add(new int[]{x, y});
                }
            }
        }
        if (!floorTiles.isEmpty()) {
            return floorTiles.get(random.nextInt(floorTiles.size()));
        }
        return null;
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public Enchantment getCurrentEnchantment() {
        return currentEnchantment;
    }

    public void addEnchantment(Enchantment enchantment) {
        grid[enchantment.getPositionY()][enchantment.getPositionX()] = enchantment;
        currentEnchantment = enchantment;
    }

    public void removeEnchantment() {
        grid[currentEnchantment.getPositionY()][currentEnchantment.getPositionX()] = new GridCell("floor", currentEnchantment.getPositionX(), currentEnchantment.getPositionY());
        currentEnchantment = null;
    }

    private boolean noMonsterThere(int x, int y) {
        for (Monster monster: monsters) {
            if (monster.getPositionX() == x && monster.getPositionY() == y) {
                return false;
            }
        }
        return true;
    }

    private boolean notInFrontOfTheDoor(int x, int y) {
        for (int doorY = 0; doorY < getHeight(); doorY++) {
            for (int doorX = 0; doorX < getWidth(); doorX++) {
                GridCell cell = getCell(doorX, doorY);
                if (cell instanceof Door) {
                    if ((x == doorX && y == doorY - 1) || // Above the door
                            (x == doorX && y == doorY + 1) || // Below the door
                            (x == doorX - 1 && y == doorY) || // Left of the door
                            (x == doorX + 1 && y == doorY)) { // Right of the door
                        return false; // Tile is in front of the door
                    }
                }
            }
        }
        return true; // Tile is not in front of any door
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }

    public String getName() {
        return name;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }


}