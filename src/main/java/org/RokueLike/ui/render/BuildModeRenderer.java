package org.RokueLike.ui.render;

import org.RokueLike.domain.BuildManager;

import java.awt.*;

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
        for (int y = 0; y < hall.length; y++) {
            for (int x = 0; x < hall[y].length; x++) {
                Color tileColor = getTileColor(hall[y][x]);
                g.setColor(tileColor);
                g.fillRect(offsetX + x * TILE_SIZE, offsetY + y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.BLACK); // Grid lines
                g.drawRect(offsetX + x * TILE_SIZE, offsetY + y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
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
}
