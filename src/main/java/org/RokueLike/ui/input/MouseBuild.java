package org.RokueLike.ui.input;

import org.RokueLike.domain.BuildManager;
import org.RokueLike.ui.render.BuildModeRenderer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static org.RokueLike.utils.Constants.*;

//Classes like MouseBuild, MousePlay, and Keyboard bridge input events from the UI to the GameManager
public class MouseBuild implements MouseListener {

    private static boolean mouseClicked = false;
    // Last mouse click information
    private static int lastClickX = -1;
    private static int lastClickY = -1;
    private static String lastClickedHall = null;

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Handle clicks within the chest area for inventory object selection
        if(isWithinChest(mouseX, mouseY)) {
            int objectIndex = getSelectedObject(mouseX, mouseY);
            if(objectIndex != -1) {
                BuildModeRenderer.selectObject(objectIndex);
                System.out.println("[MouseBuild]: Selected " + objectIndex);
            }
        } else {
            // Handle clicks within hall grids
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
    }

    // Checks if the mouse coordinates are within the inventory chest area.
    private boolean isWithinChest(int x, int y) {
        return x >= BUILD_INVENTORY_X && x <= WINDOW_WIDTH && y >= BUILD_INVENTORY_Y && y <= WINDOW_HEIGHT;
    }

    // Determines the selected object based on mouse click within the chest.
    private int getSelectedObject(int mouseX, int mouseY) {
        // TODO: Not generic, can be problematic, needs fixing
        int relativeX = (mouseX - BUILD_INVENTORY_X) / 48;
        int relativeY = (mouseY - BUILD_INVENTORY_Y - 100) / 48;
        int index = relativeY * 3 + relativeX; // 3 objects per row
        return (index >= 0 && index < 6) ? index + 1 : -1;
    }

    // Determines which hall was clicked based on the mouse coordinates.
    private String getClickedHall(int mouseX, int mouseY) {
        if (isWithinBounds(mouseX, mouseY, BUILD_EARTH_X, BUILD_EARTH_Y)) return "earth";
        if (isWithinBounds(mouseX, mouseY, BUILD_AIR_X, BUILD_AIR_Y)) return "air";
        if (isWithinBounds(mouseX, mouseY, BUILD_WATER_X, BUILD_WATER_Y)) return "water";
        if (isWithinBounds(mouseX, mouseY, BUILD_FIRE_X, BUILD_FIRE_Y)) return "fire";
        return null;
    }

    // Checks if the mouse coordinates are within a specific hall's bounds.
    private boolean isWithinBounds(int x, int y, int offsetX, int offsetY) {
        int hallWidth = GRID_WIDTH * BUILD_TILE_SIZE;
        int hallHeight = GRID_HEIGHT * BUILD_TILE_SIZE;
        return x >= offsetX && x < offsetX + hallWidth && y >= offsetY && y < offsetY + hallHeight;
    }

    // Converts mouse coordinates to grid coordinates within a specific hall.
    private int[] getTileCoordinates(int mouseX, int mouseY, String hallName) {
        int offsetX, offsetY;
        switch (hallName) {
            case "earth":
                offsetX = BUILD_EARTH_X;
                offsetY = BUILD_EARTH_Y;
                break;
            case "air":
                offsetX = BUILD_AIR_X;
                offsetY = BUILD_AIR_Y;
                break;
            case "water":
                offsetX = BUILD_WATER_X;
                offsetY = BUILD_WATER_Y;
                break;
            case "fire":
                offsetX = BUILD_FIRE_X;
                offsetY = BUILD_FIRE_Y;
                break;
            default:
                return null;
        }

        int gridX = (mouseX - offsetX) / BUILD_TILE_SIZE;
        int gridY = (mouseY - offsetY) / BUILD_TILE_SIZE;
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