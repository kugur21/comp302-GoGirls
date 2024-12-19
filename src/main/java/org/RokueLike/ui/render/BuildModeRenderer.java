package org.RokueLike.ui.render;

import org.RokueLike.domain.BuildManager;
import org.RokueLike.ui.Textures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class BuildModeRenderer {

    private static final int TILE_SIZE = 16;  // Pixel size for each grid tile
    private static final int GRID_OFFSET_X = 50;  // Offset for rendering halls
    private static final int GRID_OFFSET_Y = 50;


    /**
     * Renders all halls in Build Mode.
     *
     * @param g Graphics2D object for rendering.
     */
    public void renderBuildMode(Graphics2D g) {
        // Get all halls from BuildManager
        String[][][] halls = BuildManager.getAllHalls();

        // Render each hall in its respective position
        renderHall(g, halls[0], GRID_OFFSET_X, GRID_OFFSET_Y, "Hall Of Earth");
        renderHall(g, halls[1], GRID_OFFSET_X + 350, GRID_OFFSET_Y, "Hall Of Air");
        renderHall(g, halls[2], GRID_OFFSET_X, GRID_OFFSET_Y + 350, "Hall Of Water");
        renderHall(g, halls[3], GRID_OFFSET_X + 350, GRID_OFFSET_Y + 350, "Hall Of Fire");
    }

    /**
     * Renders an individual hall grid.
     *
     * @param g          Graphics2D object for rendering.
     * @param hall       2D String array representing the hall grid.
     * @param offsetX    X position offset for rendering.
     * @param offsetY    Y position offset for rendering.
     * @param hallName   Name of the hall to display as a label.
     */
    private void renderHall(Graphics2D g, String[][] hall, int offsetX, int offsetY, String hallName) {
        // Render the grid
        Textures.init();
        for (int y = 0; y < hall.length; y++) {
            for (int x = 0; x < hall[y].length; x++) {
                BufferedImage tileSprite = getTileSprite(hall, x, y); // Determine sprite based on neighbors
                if (tileSprite != null) {
                    g.drawImage(tileSprite, offsetX + x * TILE_SIZE, offsetY + y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                } else {
                    g.setColor(Color.PINK);
                    g.fillRect(offsetX + x * TILE_SIZE, offsetY + y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }

                //g.setColor(Color.BLACK); // Draw grid lines
                //g.drawRect(offsetX + x * TILE_SIZE, offsetY + y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Render hall label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(hallName, offsetX + (hall[0].length * TILE_SIZE) / 2 - 50, offsetY - 10);
    }

    /**
     * Determines the color of the tile based on its content.
     *
     * @param tileContent The character representing the tile.
     * @return The color for rendering the tile.
     */
    private Color getTileColor(String tileContent) {
        switch (tileContent) {
            case "#":
                return Color.DARK_GRAY; // Walls
            case ".":
                return Color.LIGHT_GRAY; // Floor
            case "d":
                return Color.ORANGE; // Door
            case "o":
                return Color.GREEN; // Object
            case "h":
                return Color.BLUE; // Hero
            default:
                return Color.PINK; // Unknown tile
        }
    }

    private BufferedImage getTileSprite(String[][] hall, int x, int y) {
        String tile = hall[y][x];
        switch (tile) {
            case "#": // Wall tile
                return getWallSprite(hall, x, y);
            case ".": // Floor tile
                return getFloorSprite(hall, x, y);
            case "d": // Door tile
                return getDoorSprite(hall, x, y);
            case "o": // Object tile
                return getObjectSprite(hall, x, y);
            default:
                return null;
        }
    }

    private BufferedImage getWallSprite(String[][] hall, int x, int y) {
        boolean up = y > 0 && "#".equals(hall[y - 1][x]);
        boolean down = y < hall.length - 1 && "#".equals(hall[y + 1][x]);
        boolean left = x > 0 && "#".equals(hall[y][x - 1]);
        boolean right = x < hall[y].length - 1 && "#".equals(hall[y][x + 1]);

        boolean upLeftCorner = x==0 && y==0;
        boolean downLeftCorner = x==hall.length-1 && y==0;
        boolean upRightCorner = x==0 && y==hall.length-1;
        boolean downRightCorner = x==hall.length-1 && y==hall.length-1;

        if(upLeftCorner) return Textures.getSprite("wall_left");
        if(downLeftCorner) return Textures.getSprite("wall_left");
        if(upRightCorner) return Textures.getSprite("wall_right");
        if(downRightCorner) return Textures.getSprite("wall_right");

        //if (!up && !left && down && right) return Textures.getSprite("wall_top_left");
        //if (!up && !right && down && left) return Textures.getSprite("wall_top_right");
        //if (!down && !left && up && right) return Textures.getSprite("wall_bottom_left");
        //if (!down && !right && up && left) return Textures.getSprite("wall_bottom_right");
        if (up && down && left && right) return Textures.getSprite("wall_center");
        if (!up && down && left && right) return Textures.getSprite("wall_top_center");
        if (up && !down && left && right) return Textures.getSprite("wall_top_center");
        if (up && down && !left && right) return Textures.getSprite("wall_top_right");
        if (up && down && left && !right) return Textures.getSprite("wall_top_left");


        // Default fallback sprite
        return Textures.getSprite("wall_center");
    }

    private BufferedImage getFloorSprite(String[][] hall, int x, int y) {
        Random random = new Random();

        // Weighted probability for floor variants
        int probability = random.nextInt(100); // Generate a random number between 0 and 99

        if (probability < 90) { // 80% chance for plain floor
            return Textures.getSprite("floor_plain");
        } else if (probability < 90) { // 10% chance for mud variant 1
            return Textures.getSprite("floor_mud_mid_1");
        } else if (probability < 95) { // 5% chance for mud variant 2
            return Textures.getSprite("floor_mud_mid_2");
        } else { // 5% chance for mud edges
            return random.nextBoolean()
                    ? Textures.getSprite("floor_mud_nw")
                    : Textures.getSprite("floor_mud_sw");
        }
    }

    private BufferedImage getObjectSprite(String[][] hall, int x, int y) {
        // Example: Choose objects based on adjacency to walls
        boolean nearWall = (y > 0 && "#".equals(hall[y - 1][x])) ||
                (y < hall.length - 1 && "#".equals(hall[y + 1][x])) ||
                (x > 0 && "#".equals(hall[y][x - 1])) ||
                (x < hall[y].length - 1 && "#".equals(hall[y][x + 1]));

        if (nearWall) return Textures.getSprite("wall_goo");
        return Textures.getSprite("chest_closed");//değişecek
    }

    private BufferedImage getDoorSprite(String[][] hall, int x, int y) {
        // Determine neighbors
        boolean up = y > 0 && "d".equals(hall[y - 1][x]);
        boolean down = y < hall.length - 1 && "d".equals(hall[y + 1][x]);
        boolean left = x > 0 && "d".equals(hall[y][x - 1]);
        boolean right = x < hall[y].length - 1 && "d".equals(hall[y][x + 1]);

        // Vertical or horizontal door placement
        if (up || down) return Textures.getSprite("door_closed");
        if (left) return Textures.getSprite("door_left");
        if (right) return Textures.getSprite("door_right");

        // Default door sprite
        return Textures.getSprite("door_closed");
    }
}
