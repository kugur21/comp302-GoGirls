package org.RokueLike.domain.hall;

import org.RokueLike.domain.entity.EntityCell;
import org.RokueLike.domain.entity.item.Door;
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

    public HallGrid(String[] gridData, String name) {
        this.name = name;
        grid = new GridCell[gridData.length][];
        initGrid(gridData);
    }

    private void initGrid(String[] gridData) {
        this.objects = new ArrayList<>();
        this.monsters = new ArrayList<>();

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


    public boolean openDoor() {
        // TODO: Implement this method
        // Finds the door inside the grid, opens it and returns true.
        return false;
    }

    public boolean removeEnchantment() {
        // TODO: Implement this method
        // Finds the enchantment inside the grid, removes it and returns true.
        return false;
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

    public List<Object> getObjects() {
        return objects;
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public void removeMonster(Monster monster) {
        monsters.remove(monster);
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

    public Monster getMonster(int x, int y) {
        for (Monster monster: monsters) {
            if (monster.getPositionX() == x && monster.getPositionY() == y) {
                return monster;
            }
        }
        return null;
    }

}
