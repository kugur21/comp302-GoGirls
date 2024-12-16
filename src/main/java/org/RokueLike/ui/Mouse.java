package org.RokueLike.ui;

import org.RokueLike.domain.GameManager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
        int gridX = getGridCoordinate(e.getX());
        int gridY = getGridCoordinate(e.getY());

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
    private int getGridCoordinate(int pixelCoordinate) {
        final int CELL_SIZE = 32; // Example: 32x32 pixels per grid cell
        return pixelCoordinate / CELL_SIZE;
    }

}
