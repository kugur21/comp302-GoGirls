package org.RokueLike.domain.hall;

import org.RokueLike.domain.entity.EntityCell;
import org.RokueLike.domain.entity.item.Door;
import org.RokueLike.domain.entity.item.Enchantment;
import org.RokueLike.domain.entity.item.Object;
import org.RokueLike.domain.entity.monster.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HallGrid {

    private int startX;
    private int startY;
    private String name;

    private GridCell[][] grid;
    private List<Monster> monsters;
    private List<Object> objects;
    private Enchantment currentEnchantment;

    public HallGrid(String[] gridData, String name) {
        this.name = name;
        grid = new GridCell[gridData.length][];
        initGrid(gridData);
    }

    private void initGrid(String[] gridData) {
        this.objects = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.currentEnchantment = null;


        for (int i = 0; i < gridData.length; i++) {
            grid[i] = new GridCell[gridData[i].length()];
            for (int j = 0; j < gridData[i].length(); j++) {
                switch (gridData[i].charAt(j)) {
                    case '#':
                        grid[i][j] = new GridCell("wall", j, i);
                        break;
                    case '.':
                        grid[i][j] = new GridCell("floor", j, i);
                        break;
                    case 'd':
                        grid[i][j] = new Door(j, i);
                        break;
                    case 'o':
                        Object object = new Object(j, i);
                        grid[i][j] = object;
                        objects.add(object);
                        break;
                    case 'h':
                        grid[i][j] = new GridCell("floor", j, i);
                        this.startX = j;
                        this.startY = i;
                        break;
                }
            }
        }
        initRune();
    }

    public void openDoor() {
        boolean doorOpened = false;

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                GridCell cell = grid[y][x];

                if (cell instanceof Door door) {
                    if (!door.isOpen()) {
                        door.open(); // Open the door
                        System.out.println("Door opened at (" + x + ", " + y + ").");
                        doorOpened = true;
                    }
                }
            }
        }

        if (!doorOpened) {
            System.out.println("No doors found to open in this hall.");
        }

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

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public boolean isThereMonster(int x, int y) {
        for (Monster monster: monsters) {
            if (monster.getPositionX() == x && monster.getPositionY() == y) {
                return false;
            }
        }
        return true;
    }

    public void initRune() {
        if (objects.isEmpty()) {
            throw new IllegalStateException("No objects available for placing the rune.");
        }
        Random random = new Random();
        Object randomObject = objects.get(random.nextInt(objects.size()));

        randomObject.setContainedRune();
        System.out.println("Rune initialized in object at (" + randomObject.getPositionX() + ", " + randomObject.getPositionY() + ")");
    }


    public void changeRuneLocation() {
        for (Object object : objects) {
            if (object.containsRune()) {
                object.removeContainedRune();
                System.out.println("Rune removed from (" + object.getPositionX() + ", " + object.getPositionY() + ")");
                break;
            }
        }

        Random random = new Random();
        Object randomObject = objects.get(random.nextInt(objects.size()));
        randomObject.setContainedRune();
        System.out.println("Rune teleported to (" + randomObject.getPositionX() + ", " + randomObject.getPositionY() + ")");
    }

    public int[][] findRuneRegion(int bound) {
        for (Object object : objects) {
            if (object.containsRune()) {
                int runeX = object.getPositionX();
                int runeY = object.getPositionY();

                Random random = new Random();
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
        throw new IllegalStateException("No rune found in any object.");
    }

    public GridCell getCell(int x, int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            return grid[y][x];
        }
        throw new IllegalArgumentException("Coordinates out of bounds: (" + x + ", " + y + ")");
    }

    public GridCell getCellInFront(EntityCell entity, int directionX, int directionY) {
        return getCell(entity.getPositionX() + directionX, entity.getPositionY() + directionY);
    }

    public boolean isSafeLocation(int positionX, int positionY) {
        return getCell(positionX, positionY).getName().equals("floor") && isThereMonster(positionX, positionY);
    }

    public boolean isSafeLocation(EntityCell entity, int directionX, int directionY) {
        return isSafeLocation(entity.getPositionX() + directionX, entity.getPositionY() + directionY);
    }

    public int[] findRandomSafeCell() {
        Random random = new Random();
        List<int[]> floorTiles = new ArrayList<>();

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (getCell(x, y).getName().equals("floor") && isThereMonster(x, y)) {
                    floorTiles.add(new int[]{x, y});
                }
            }
        }

        if (!floorTiles.isEmpty()) {
            return floorTiles.get(random.nextInt(floorTiles.size()));
        }
        return null;
    }
}