package org.RokueLike.ui.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseBuild implements MouseListener {

    private static boolean mouseClicked = false;
    private static int lastClickX = -1;
    private static int lastClickY = -1;
    private static String lastClickedHall = null;

    // Same as BuildModeRenderer
    private static final int TILE_SIZE = 16;
    private static final int GRID_OFFSET_X = 50;
    private static final int GRID_OFFSET_Y = 50;

    // Same as BuildManager
    private static final int GRID_WIDTH = 15;
    private static final int GRID_HEIGHT = 15;

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Determine which hall was clicked
        lastClickedHall = getClickedHall(mouseX, mouseY);

        if (lastClickedHall != null) {
            int[] gridCoords = getTileCoordinates(mouseX, mouseY, lastClickedHall);
            if (gridCoords != null) {
                lastClickX = gridCoords[0];
                lastClickY = gridCoords[1];
                mouseClicked = true;

                System.out.println("[MouseBuild]: Click detected in " + lastClickedHall + " at (" + lastClickX + ", " + lastClickY + ")");
            }
        }
    }

    private String getClickedHall(int mouseX, int mouseY) {
        if (isWithinBounds(mouseX, mouseY, GRID_OFFSET_X, GRID_OFFSET_Y)) return "earth";
        if (isWithinBounds(mouseX, mouseY, GRID_OFFSET_X + 350, GRID_OFFSET_Y)) return "air";
        if (isWithinBounds(mouseX, mouseY, GRID_OFFSET_X, GRID_OFFSET_Y + 350)) return "water";
        if (isWithinBounds(mouseX, mouseY, GRID_OFFSET_X + 350, GRID_OFFSET_Y + 350)) return "fire";
        return null;
    }

    private boolean isWithinBounds(int x, int y, int offsetX, int offsetY) {
        int hallWidth = GRID_WIDTH * TILE_SIZE;
        int hallHeight = GRID_HEIGHT * TILE_SIZE;
        return x >= offsetX && x < offsetX + hallWidth && y >= offsetY && y < offsetY + hallHeight;
    }

    private int[] getTileCoordinates(int mouseX, int mouseY, String hallName) {
        int offsetX, offsetY;

        switch (hallName) {
            case "earth":
                offsetX = GRID_OFFSET_X;
                offsetY = GRID_OFFSET_Y;
                break;
            case "air":
                offsetX = GRID_OFFSET_X + 350;
                offsetY = GRID_OFFSET_Y;
                break;
            case "water":
                offsetX = GRID_OFFSET_X;
                offsetY = GRID_OFFSET_Y + 350;
                break;
            case "fire":
                offsetX = GRID_OFFSET_X + 350;
                offsetY = GRID_OFFSET_Y + 350;
                break;
            default:
                return null;
        }

        int gridX = (mouseX - offsetX) / TILE_SIZE;
        int gridY = (mouseY - offsetY) / TILE_SIZE;
        return new int[]{gridX, gridY};
    }

    public static boolean isMouseClicked() {
        return mouseClicked;
    }

    public static int[] getLastClickCoordinates() {
        return new int[]{lastClickX, lastClickY};
    }

    public static String getLastClickedHall() {
        return lastClickedHall;
    }

    public static void resetMouseClick() {
        mouseClicked = false;
        lastClickX = -1;
        lastClickY = -1;
        lastClickedHall = null;
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
