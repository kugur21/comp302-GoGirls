package org.RokueLike.ui.render;

import org.RokueLike.domain.BuildManager;
import org.RokueLike.ui.Window;
import org.RokueLike.utils.FontLoader;
import org.RokueLike.utils.Textures;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.RokueLike.utils.Constants.*;

public class BuildModeRenderer {

    private static int selectedObject = 1; // Currently selected object in the inventory
    private final Font customFont;

    //MODEL-VIEW SEPARATION PRINCIPLE: Renderer classes (BuildModeRenderer, PlayModeRenderer) manage graphical representation and rendering tasks without interfering with the model logic.

    public BuildModeRenderer() {
        customFont =  FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 12f); // Piksel font
    }

    // Renders the entire Build Mode, including halls and inventory.
    public void renderBuildMode(Graphics2D g) {
        renderBackground(g);
        renderInventory(g);

        String[][][] halls = BuildManager.getAllHalls();
        renderHall(g, halls[0], BUILD_EARTH_X, BUILD_EARTH_Y, "Hall Of Earth");
        renderHall(g, halls[1], BUILD_AIR_X, BUILD_AIR_Y, "Hall Of Air");
        renderHall(g, halls[2], BUILD_WATER_X, BUILD_WATER_Y, "Hall Of Water");
        renderHall(g, halls[3], BUILD_FIRE_X, BUILD_FIRE_Y, "Hall Of Fire");
    }

    // Renders the floor background and additional decorative elements.
    private void renderBackground(Graphics2D g) {
        int tilesWide = WINDOW_WIDTH / BUILD_TILE_SIZE;
        int tilesHigh = WINDOW_HEIGHT / BUILD_TILE_SIZE;

        // Render base floor_plain tiles
        for (int y = 0; y < tilesHigh; y++) {
            for (int x = 0; x < tilesWide; x++) {
                g.drawImage(Textures.getSprite("floor_plain"), x * BUILD_TILE_SIZE, y * BUILD_TILE_SIZE, BUILD_TILE_SIZE, BUILD_TILE_SIZE, null);
            }
        }
        // Add a mud cluster every N tiles in both x and y directions
        for (int y = 0; y < tilesHigh; y++) {
            for (int x = 0; x < tilesWide; x++) {
                if(x % 12 == 0 && y % 12 == 0){
                    renderMudPattern(g, x, y);
                }
            }
        }
    }

    // Renders a mud pattern cluster at a specified grid position.
    private void renderMudPattern(Graphics2D g, int clusterX, int clusterY) {
        // Define mud cluster sprite names and their relative positions
        String[][] clusterSprites = {
                {"floor_mud_nw", "floor_mud_n_1", "floor_mud_n_2", "floor_mud_ne"}, // Top row
                {"floor_mud_w", "floor_mud_mid_1", "floor_mud_mid_2", "floor_mud_e"}, // Middle row
                {"floor_mud_sw", "floor_mud_s_1", "floor_mud_s_2", "floor_mud_se"} // Bottom row
        };

        // Iterate through the clusterSprites array to draw the cluster
        for (int row = 0; row < clusterSprites.length; row++) {
            for (int col = 0; col < clusterSprites[row].length; col++) {
                String spriteName = clusterSprites[row][col];
                BufferedImage sprite = Textures.getSprite(spriteName);
                if (sprite != null) {
                    int x = (clusterX + col) * BUILD_TILE_SIZE;
                    int y = (clusterY + row) * BUILD_TILE_SIZE;
                    g.drawImage(sprite, x, y, BUILD_TILE_SIZE, BUILD_TILE_SIZE, null);
                } else {
                    System.err.println("[BuildModeRenderer]: Sprite not found - " + spriteName);
                }
            }
        }
    }

    // Renders the inventory and its items.
    private void renderInventory(Graphics2D g) {
        BufferedImage chestImage = Textures.getSprite("Buildmodechest");
        if (chestImage == null) {
            System.err.println("[BuildModeRenderer]: Chest sprite 'Buildmodechest' not available!");
            return;
        }
        int scaledWidth = BUILD_INVENTORY_WIDTH + 100;
        int scaledHeight = BUILD_INVENTORY_HEIGHT + 100;
        g.drawImage(chestImage, BUILD_INVENTORY_X, BUILD_INVENTORY_Y, scaledWidth, scaledHeight, null);

        // Render object slots
        String[] objectNames = {"chest_closed", "chest_golden_closed", "column", "torch_4", "box", "boxes_stacked"};
        for (int i = 0; i < 6; i++) {
            int x = BUILD_INVENTORY_X + 20 + (i % 3) * BUILD_INVENTORY_SLOT_SIZE;
            int y = BUILD_INVENTORY_Y + 100 + (i / 3) * BUILD_INVENTORY_SLOT_SIZE;
            BufferedImage objectSprite = Textures.getSprite(objectNames[i]);

            if (objectSprite != null) {
                g.drawImage(objectSprite, x, y, BUILD_INVENTORY_SLOT_SIZE, BUILD_INVENTORY_SLOT_SIZE, null);

                // Highlight selected object
                if (getSelectedObject() == i + 1) {
                    g.setColor(Color.YELLOW);
                    g.drawRect(x - 2, y - 2, BUILD_INVENTORY_SLOT_SIZE + 4, BUILD_INVENTORY_SLOT_SIZE + 4);
                }
            }
        }
    }

    // Renders an individual hall grid.
    private void renderHall(Graphics2D g, String[][] hall, int offsetX, int offsetY, String hallName) {
        for (int y = 0; y < hall.length; y++) {
            for (int x = 0; x < hall[y].length; x++) {
                BufferedImage tileSprite = getTileSprite(hall, x, y); // Determine appropriate sprite
                if (tileSprite != null) {
                    g.drawImage(tileSprite, offsetX + x * BUILD_TILE_SIZE, offsetY + y * BUILD_TILE_SIZE, BUILD_TILE_SIZE, BUILD_TILE_SIZE, null);
                }
            }
        }
        g.setColor(Color.WHITE);
        g.setFont(customFont);
        g.drawString(hallName, offsetX, offsetY - 10);
    }

    private BufferedImage getTileSprite(String[][] hall, int x, int y) {
        String tile = hall[y][x];
        return switch (tile) {
            case "#" -> getWallSprite(hall, x, y); // Wall tile
            case "d" -> getDoorSprite(hall, x, y); // Door tile
            // Object tiles
            case "o1" -> Textures.getSprite("chest_closed");
            case "o2" -> Textures.getSprite("chest_golden_closed");
            case "o3" -> Textures.getSprite("column");
            case "o4" -> Textures.getSprite("torch_4");
            case "o5" -> Textures.getSprite("box");
            case "o6" -> Textures.getSprite("boxes_stacked");
            default -> null;
        };
    }

    // Helper method for getting the correct sprite for walls.
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

        if (up && down && left && right) return Textures.getSprite("wall_center");
        if (!up && down && left && right) return Textures.getSprite("wall_top_center");
        if (up && !down && left && right) return Textures.getSprite("wall_top_center");
        if (up && down && !left && right) return Textures.getSprite("wall_top_right");
        if (up && down && left) return Textures.getSprite("wall_top_left");

        return Textures.getSprite("wall_center");
    }

    // Helper method for getting the correct sprite for doors.
    private BufferedImage getDoorSprite(String[][] hall, int x, int y) {
        boolean up = y > 0 && "d".equals(hall[y - 1][x]);
        boolean down = y < hall.length - 1 && "d".equals(hall[y + 1][x]);
        boolean left = x > 0 && "d".equals(hall[y][x - 1]);
        boolean right = x < hall[y].length - 1 && "d".equals(hall[y][x + 1]);

        if (up || down) return Textures.getSprite("door_closed");
        if (left) return Textures.getSprite("door_left");
        if (right) return Textures.getSprite("door_right");

        return Textures.getSprite("door_closed");
    }

    public static void selectObject(int objectIndex) {
        selectedObject = objectIndex;
    }

    public static int getSelectedObject() {
        return selectedObject;
    }

}