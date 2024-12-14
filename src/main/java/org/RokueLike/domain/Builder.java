package org.RokueLike.domain;

import java.util.Random;

public class Builder {

    private String[][] grid;

    public Builder(int width, int height) {
        grid = new String[width][height];
        initializeGrid();
    }

    public Builder() {
        this(25, 25);
    }

    private void initializeGrid() {
        Random random = new Random();

        int height = grid.length;
        int width = grid[0].length;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height - 1 || j == 0 || j == width - 1) {
                    grid[i][j] = "#"; // Walls at edges
                } else {
                    grid[i][j] = "."; // Empty floor
                }
            }
        }

        int side = random.nextInt(4);
        int doorPosition;

        switch (side) {
            case 0:
                doorPosition = random.nextInt(width - 2) + 1;
                grid[0][doorPosition] = "d";
                break;
            case 1:
                doorPosition = random.nextInt(width - 2) + 1;
                grid[height - 1][doorPosition] = "d";
                break;
            case 2:
                doorPosition = random.nextInt(height - 2) + 1;
                grid[doorPosition][0] = "d";
                break;
            case 3:
                doorPosition = random.nextInt(height - 2) + 1;
                grid[doorPosition][width - 1] = "d";
                break;
        }
    }

    public void placeObject(int x, int y) {
        if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
            if (grid[y][x].equals(".")) {
                grid[y][x] = "o";
                System.out.println("Object placed at (" + x + ", " + y + ")");
            } else {
                System.out.println("Cannot place object at (" + x + ", " + y + "). Cell is not empty.");
            }
        } else {
            System.out.println("Invalid coordinates (" + x + ", " + y + ")");
        }
    }

    public void placeObject(int numberOfObjects) {
        Random random = new Random();
        int height = grid.length;
        int width = grid[0].length;

        int placedObjects = 0;

        while (placedObjects < numberOfObjects) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (grid[y][x].equals(".")) {
                grid[y][x] = "o";
                placedObjects++;
            }
        }

        System.out.println(numberOfObjects + " objects randomly placed on the grid.");
    }

    public void placeHero(int x, int y) {
        if (x >= 0 && x < grid[0].length && y >= 0 && y < grid.length) {
            if (grid[y][x].equals(".")) {
                grid[y][x] = "h";
                System.out.println("Hero placed at (" + x + ", " + y + ")");
            } else {
                System.out.println("Cannot place hero at (" + x + ", " + y + "). Cell is not empty.");
            }
        } else {
            System.out.println("Invalid coordinates (" + x + ", " + y + ").");
        }
    }


    public void placeHero() {
        Random random = new Random();
        int height = grid.length;
        int width = grid[0].length;

        boolean placed = false;

        while (!placed) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if (grid[y][x].equals(".")) {
                grid[y][x] = "h";
                placed = true;
                System.out.println("Hero placed at (" + x + ", " + y + ")");
            }
        }
    }


    public void resetGrid() {
        initializeGrid();
        System.out.println("Grid reset.");
    }

    public void printGrid() {
        for (String[] row : grid) {
            System.out.println(String.join("", row));
        }
    }

    public String[] getGridData() {
        String[] gridData = new String[grid.length];
        for (int i = 0; i < grid.length; i++) {
            gridData[i] = String.join("", grid[i]);
        }
        return gridData;
    }

}
