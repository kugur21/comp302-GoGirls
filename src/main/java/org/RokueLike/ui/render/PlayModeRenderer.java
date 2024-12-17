package org.RokueLike.ui.render;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.item.Door;
import org.RokueLike.domain.entity.item.Enchantment;
import org.RokueLike.domain.entity.item.Object;
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

        if (currentHall == null || hero == null) return;;

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
                Color tileColor = getTileColor(hall.getCell(x, y));
                g.setColor(tileColor);
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

    private Color getTileColor(GridCell cell) {
        if (cell.getName().equals("wall")) {
            return Color.DARK_GRAY;
        } else if (cell.getName().equals("floor")) {
            return Color.LIGHT_GRAY;
        } else if (cell instanceof Door) {
            return Color.ORANGE;
        } else if (cell instanceof Object) {
            return Color.GREEN;
        } else if (cell instanceof Hero) {
            return Color.BLUE;
        } else if (cell instanceof Monster) {
            return Color.RED;
        } else if (cell instanceof Enchantment) {
            return Color.PINK;
        } else {
            return Color.WHITE;
        }
    }
}