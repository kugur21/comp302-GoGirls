package org.RokueLike.ui.input;

import org.RokueLike.domain.GameManager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MousePlay implements MouseListener {

    // Same as PlayModeRenderer
    private static final int TILE_SIZE = 36;  // Tile size
    private static final int GRID_OFFSET_X = 70;  // Grid X başlangıcı (100'den 70'e kaydırıldı)
    private static final int GRID_OFFSET_Y = 50;


    @Override
    public void mouseClicked(MouseEvent e) {
        int gridX = getGridCoordinate(e.getX(), GRID_OFFSET_X);
        int gridY = getGridCoordinate(e.getY(), GRID_OFFSET_Y);

        if (e.getButton() == MouseEvent.BUTTON1) {
            GameManager.handleLeftClick(gridX, gridY);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            GameManager.handleRightClick(gridX, gridY);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    /**
     * Converts screen coordinates to grid coordinates.
     * This assumes that each grid cell has a fixed pixel size.
     * Adjust `CELL_SIZE` based on your game's implementation.
     */
    private int getGridCoordinate(int pixelCoordinate, int offset) {
        final int CELL_SIZE = 16; // Same as PlayModeRenderer
        return (pixelCoordinate - offset) / TILE_SIZE;
    }

}
