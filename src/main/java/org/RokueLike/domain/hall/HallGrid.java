package org.RokueLike.domain.hall;

import org.RokueLike.domain.entity.monster.Monster;

import java.util.ArrayList;
import java.util.List;

public class HallGrid {

    private int startX;
    private int startY;
    private String name;

    private GridCell[][] grid;
    private List<Monster> monsters;

    public HallGrid(String[] gridData, String name) {
        this.name = name;
        grid = new GridCell[gridData.length][];
        initGrid(gridData);
    }

    private void initGrid(String[] gridData) {
        for (int i = 0; i < gridData.length; i++) {
            grid[i] = new GridCell[gridData[i].length()];
            for (int j = 0; j < gridData[i].length(); j++) {
                switch (gridData[i].charAt(j)) {
                    case '#':
                        grid[i][j] = new GridCell("wall", j, i);
                        break;
                    case 'd':
                        grid[i][j] = new GridCell("door", j, i);
                        break;
                    case '.':
                        grid[i][j] = new GridCell("floor", j, i);
                        break;
                    case 'o':
                        grid[i][j] = new GridCell("object", j, i);
                        break;
                    case 'h':
                        grid[i][j] = new GridCell("floor", j, i);
                        this.startX = j;
                        this.startY = i;
                        break;
                }
            }
        }
        this.monsters = new ArrayList<>();
    }

    public GridCell getCell(int x, int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            return grid[y][x];
        }
        throw new IllegalArgumentException("Coordinates out of bounds: (" + x + ", " + y + ")");
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

    public boolean openDoor(int x, int y) {
        if (getCell(x, y).getName().equals("door")) {
            grid[y][x] = new GridCell("floor", x, y);
            return true;
        }
        return false;
    }

    public boolean removeEnch(int x, int y) {
        if (getCell(x, y).getName().startsWith("ench_")) {
            grid[y][x] = new GridCell("floor", x, y);
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
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
                return true;
            }
        }
        return false;
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
