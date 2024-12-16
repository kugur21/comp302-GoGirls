package org.RokueLike.ui.render;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;

import java.awt.*;
import java.util.List;

public class PlayModeRenderer {

    private static final int TILE_SIZE = 32;  // Grid tile size
    private static final int GRID_OFFSET_X = 50;  // Offset for grid rendering
    private static final int GRID_OFFSET_Y = 50;

    /**
     * Renders the game grid, hero, and active monsters.
     *
     * @param g Graphics2D object used for rendering.
     */
    public void renderPlayMode(Graphics2D g) {
        HallGrid currentHall = GameManager.getCurrentHall();
        Hero hero = GameManager.getHero();
        List<Monster> monsters = GameManager.getActiveMonsters();

        if (currentHall == null || hero == null) return;

        renderGrid(g, currentHall);
        renderHero(g, hero);
        renderMonsters(g, monsters);
        renderUI(g, hero);
    }

    /**
     * Renders the game grid.
     *
     * @param g    Graphics2D object.
     * @param hall HallGrid representing the current grid.
     */
    private void renderGrid(Graphics2D g, HallGrid hall) {
        for (int y = 0; y < hall.getHeight(); y++) {
            for (int x = 0; x < hall.getWidth(); x++) {
                GridCell cell = hall.getCell(x, y);
                //Color tileColor = getTileColor(cell.getType());
                //g.setColor(tileColor);
                g.fillRect(GRID_OFFSET_X + x * TILE_SIZE, GRID_OFFSET_Y + y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(GRID_OFFSET_X + x * TILE_SIZE, GRID_OFFSET_Y + y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            }
        }
    }

    /**
     * Renders the hero.
     *
     * @param g    Graphics2D object.
     * @param hero Hero object representing the player.
     */
    private void renderHero(Graphics2D g, Hero hero) {
        g.setColor(Color.BLUE);
        g.fillOval(
                GRID_OFFSET_X + hero.getPositionX() * TILE_SIZE,
                GRID_OFFSET_Y + hero.getPositionY() * TILE_SIZE,
                TILE_SIZE, TILE_SIZE
        );
    }

    /**
     * Renders monsters on the grid.
     *
     * @param g        Graphics2D object.
     * @param monsters List of active monsters.
     */
    private void renderMonsters(Graphics2D g, List<Monster> monsters) {
        g.setColor(Color.RED);
        for (Monster monster : monsters) {
            g.fillOval(
                    GRID_OFFSET_X + monster.getPositionX() * TILE_SIZE,
                    GRID_OFFSET_Y + monster.getPositionY() * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE
            );
        }
    }

    /**
     * Renders the player UI (health and stats).
     *
     * @param g    Graphics2D object.
     * @param hero Hero object.
     */
    private void renderUI(Graphics2D g, Hero hero) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Health: " + hero.getLives() + " / " + 4, 10, 30);
        g.drawString("Time Remaining: " + hero.getRemainingTime(), 10, 60);
    }

    /**
     * Determines tile color based on the grid cell type.
     *
     * @param cellType Type of the grid cell.
     * @return Color for rendering.
     */
    private Color getTileColor(String cellType) {
        return switch (cellType) {
            case "#" -> Color.DARK_GRAY;  // Wall
            case "." -> Color.LIGHT_GRAY; // Floor
            case "d" -> Color.ORANGE;     // Door
            case "o" -> Color.GREEN;      // Object
            default -> Color.PINK;        // Default unknown
        };
    }
}