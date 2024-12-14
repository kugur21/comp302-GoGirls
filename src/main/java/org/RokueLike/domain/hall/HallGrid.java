package org.RokueLike.domain.hall;

public class HallGrid {

    private int startX;
    private int startY;
    private String name;

    private GridCell[][] grid;
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
                        grid[i][j] = new GridCell("hero", j, i);
                        this.startX = j;
                        this.startY = i;
                        break;
                }
            }
        }
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

}
