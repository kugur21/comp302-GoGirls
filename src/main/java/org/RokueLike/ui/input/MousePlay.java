package org.RokueLike.ui.input;

import org.RokueLike.domain.GameManager;
import org.RokueLike.ui.screen.PlayModeScreen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static org.RokueLike.utils.Constants.*;

public class MousePlay implements MouseListener {

    private final PlayModeScreen playModeScreen;
    private final Rectangle pauseButtonBounds;
    private final Rectangle exitButtonBounds;

    //Classes like MouseBuild, MousePlay, and Keyboard bridge input events from the UI to the GameManager
    public MousePlay(Rectangle pauseButtonBounds, Rectangle exitButtonBounds, PlayModeScreen playModeScreen) {
        this.pauseButtonBounds = pauseButtonBounds;
        this.exitButtonBounds = exitButtonBounds;
        this.playModeScreen = playModeScreen;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point clickPoint = e.getPoint();
        if (pauseButtonBounds.contains(clickPoint)) {
            GameManager.togglePauseResume(); // Toggle game pause/resume
            return;
        }
        if (exitButtonBounds.contains(clickPoint)) {
            playModeScreen.returnToLaunchScreen(); // Returns to Launch Screen
            return;
        }

        int gridX = getGridCoordinate(e.getX(), PLAY_GRID_OFFSET_X);
        int gridY = getGridCoordinate(e.getY(), PLAY_GRID_OFFSET_Y);

        // Handle left and right mouse button actions
        if (e.getButton() == MouseEvent.BUTTON1) { // Left click
            GameManager.handleLeftClick(gridX, gridY);
        } else if (e.getButton() == MouseEvent.BUTTON3) { // Right click
            GameManager.handleRightClick(gridX, gridY);
        }
    }

    private int getGridCoordinate(int pixelCoordinate, int offset) {
        return (pixelCoordinate - offset) / PLAY_TILE_SIZE;
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}

}