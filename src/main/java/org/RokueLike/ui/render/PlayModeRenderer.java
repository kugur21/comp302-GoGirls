package org.RokueLike.ui.render;

import java.awt.*;

public class PlayModeRenderer {

    private static final int TILE_SIZE = 32;  // Grid tile size
    private static final int GRID_OFFSET_X = 50;  // Offset to center the grid
    private static final int GRID_OFFSET_Y = 50;

    /**
     * Renders the play mode grid.
     *
     * @param g Graphics2D object used for rendering.
     */
    public void renderPlayMode(Graphics2D g) {
        // Get the current grid state from GameManager
        String[][] grid = GameManager.getCurrentGrid();

        // Render the grid
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                Color tileColor = getTileColor(grid[y][x]);
                g.setColor(tileColor);
                g.fillRect(GRID_OFFSET_X + x * TILE_SIZE, GRID_OFFSET_Y + y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.BLACK); // Draw grid borders
                g.drawRect(GRID_OFFSET_X + x * TILE_SIZE, GRID_OFFSET_Y + y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Additional rendering (e.g., player stats, UI)
        renderUI(g);
    }

    /**
     * Determines the color of the tile based on its content.
     *
     * @param tileContent Character representing the tile.
     * @return Color for rendering the tile.
     */
    private Color getTileColor(String tileContent) {
        switch (tileContent) {
            case "#": return Color.DARK_GRAY; // Walls
            case ".": return Color.LIGHT_GRAY; // Floor
            case "d": return Color.ORANGE; // Door
            case "o": return Color.GREEN; // Object
            case "h": return Color.BLUE; // Hero
            case "m": return Color.RED; // Monster
            default: return Color.PINK; // Unknown
        }
    }

    /**
     * Renders the UI such as player stats.
     *
     * @param g Graphics2D object.
     */
    private void renderUI(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Health: " + GameManager.getPlayerHealth(), 10, 30);
        g.drawString("Score: " + GameManager.getPlayerScore(), 10, 60);
    }
}
