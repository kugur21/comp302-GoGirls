
package org.RokueLike.ui.render;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.TimeManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.item.Arrow;
import org.RokueLike.domain.entity.item.Door;
import org.RokueLike.domain.entity.item.Enchantment;
import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.domain.entity.monster.MonsterManager;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.utils.Direction;
import org.RokueLike.utils.MessageBox;
import org.RokueLike.utils.FontLoader;
import org.RokueLike.utils.Textures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.RokueLike.utils.Constants.*;

public class PlayModeRenderer {

    private final MessageBoxRenderer messageBoxRenderer;
    private final Font customFont;

    ////MODEL-VIEW SEPARATION PRINCIPLE: Renderer classes (BuildModeRenderer, PlayModeRenderer) manage graphical representation and rendering tasks without interfering with the model logic.

    public PlayModeRenderer() {
        customFont = FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 12f);
        messageBoxRenderer = new MessageBoxRenderer(12f);
    }

    // Renders the entire Play Mode.
    public void renderPlayMode(Graphics2D g, Rectangle exitButtonBounds, Rectangle pauseButtonBounds) {
        HallGrid currentHall = GameManager.getCurrentHall();
        Hero hero = GameManager.getHero();
        List<Monster> monsters = GameManager.getActiveMonsters();
        MessageBox messageBox = GameManager.getMessageBox();
        if (currentHall == null || hero == null) return;

        // High Cohesion and Low Coupling
        renderFloor(g);
        renderMudClusters(g);
        renderGrid(g, currentHall);
        renderRuneRegion(g, currentHall);
        renderHUD(g, hero, hero.getRemainingTime());
        renderControllerButtons(g, exitButtonBounds, pauseButtonBounds);
        renderHero(g, hero);
        renderMonsters(g, monsters);
        renderArrows(g, GameManager.getMonsterManager());

        messageBoxRenderer.renderMessageBox(g, GameManager.getMessageBox());
    }

    // Renders the floor background.
    private void renderFloor(Graphics2D g) {
        BufferedImage floorPlainImage = Textures.getSprite("floor_plain");
        if (floorPlainImage != null) {
            for (int y = 0; y < PLAY_WINDOW_HEIGHT / PLAY_TILE_SIZE; y++) {
                for (int x = 0; x < PLAY_WINDOW_WIDTH / PLAY_TILE_SIZE; x++) {
                    g.drawImage(floorPlainImage, x * PLAY_TILE_SIZE, y * PLAY_TILE_SIZE, PLAY_TILE_SIZE, PLAY_TILE_SIZE, null);
                }
            }
        }
    }

    // Renders mud clusters for visual diversity.
    private void renderMudClusters(Graphics2D g) {
        for (int y = 0; y < PLAY_WINDOW_HEIGHT / PLAY_TILE_SIZE; y += 12) {
            for (int x = 0; x < PLAY_WINDOW_WIDTH / PLAY_TILE_SIZE; x += 12) {
                renderMudPattern(g, x, y);
            }
        }
    }

    // Draws a single mud cluster.
    private void renderMudPattern(Graphics2D g, int clusterX, int clusterY) {
        String[][] clusterSprites = {
                {"floor_mud_nw", "floor_mud_n_1", "floor_mud_n_2", "floor_mud_ne"}, // Top row
                {"floor_mud_w", "floor_mud_mid_1", "floor_mud_mid_2", "floor_mud_e"}, // Middle row
                {"floor_mud_sw", "floor_mud_s_1", "floor_mud_s_2", "floor_mud_se"} // Bottom row
        };
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

    // Renders the hall grid, tiles, and decorations.
    private void renderGrid(Graphics2D g, HallGrid hall) {
        BufferedImage floorImage = Textures.getSprite("floor_plain");
        for (int y = 0; y < hall.getHeight(); y++) {
            for (int x = 0; x < hall.getWidth(); x++) {
                // Render floor_plain first
                if (floorImage != null) {
                    g.drawImage(floorImage, PLAY_GRID_OFFSET_X + x * PLAY_TILE_SIZE, PLAY_GRID_OFFSET_Y + y * PLAY_TILE_SIZE, PLAY_TILE_SIZE, PLAY_TILE_SIZE, null);
                }
                if ((x % 6 == 0 && y % 6 == 0)) {
                    renderMudPattern(g, PLAY_GRID_OFFSET_X + x * PLAY_TILE_SIZE, PLAY_GRID_OFFSET_Y + y * PLAY_TILE_SIZE);
                }

                GridCell cell = hall.getCell(x, y);
                drawTile(g, cell, x, y);
            }
        }
    }

    // Draws specific tiles like walls, doors, and interactive objects.
    private void drawTile(Graphics2D g, GridCell cell, int x, int y) {
        if (cell == null) {
            System.err.println("[PlayModeRenderer]: Null cell at (" + x + ", " + y + ")");
            return;
        }
        BufferedImage img;
        if ("door".equals(cell.getName())) {
            Door door = (Door) cell;
            if (door.isOpen()) {
                img = Textures.getSprite("door_open");
            } else {
                img = Textures.getSprite("door_closed");
            }

            int doorSize = PLAY_TILE_SIZE + 12;
            if (img != null) {
                g.drawImage(img,
                        PLAY_GRID_OFFSET_X + x * PLAY_TILE_SIZE - (doorSize - PLAY_TILE_SIZE) / 2,
                        PLAY_GRID_OFFSET_Y + y * PLAY_TILE_SIZE - (doorSize - PLAY_TILE_SIZE) / 2,
                        doorSize, doorSize, null);
            }
            return;
        } else {
            img = switch (cell.getName()) {
                case "object1" -> Textures.getSprite("chest_closed");
                case "object2" -> Textures.getSprite("chest_golden_closed");
                case "object3" -> Textures.getSprite("column");
                case "object4" -> Textures.getSprite("torch_4");
                case "object5" -> Textures.getSprite("box");
                case "object6" -> Textures.getSprite("boxes_stacked");
                case "wall" -> Textures.getSprite("wall_center");
                case "floor" -> Textures.getSprite("floor_plain");
                case "extra_time" -> Textures.getSprite("extra_time_1_");
                case "extra_life" -> Textures.getSprite("extraheart");
                case "reveal" -> Textures.getSprite("reveal");
                case "luring_gem" -> Textures.getSprite("luringgem");
                case "cloak_of_protection" -> Textures.getSprite("protection");
                default -> Textures.getSprite("black");
            };
        }
        if (img != null) {
            g.drawImage(img, PLAY_GRID_OFFSET_X + x * PLAY_TILE_SIZE, PLAY_GRID_OFFSET_Y + y * PLAY_TILE_SIZE, PLAY_TILE_SIZE, PLAY_TILE_SIZE, null);
        } else {
            System.err.println("[Textures]: Missing sprite for " + cell.getName());
        }
    }

    // Highlights rune regions when reveal power is active
    private void renderRuneRegion(Graphics2D g, HallGrid hall) {
        if (!GameManager.isRevealActive()) {
            return;
        }

        int[][] runeRegion = hall.findRuneRegion(4);
        if (runeRegion == null) {
            return; // Exit if no rune region is defined
        }
        // Rune region looping starts below
        for (int[] coord : runeRegion) {
            int x = coord[0];
            int y = coord[1];

            // Calculate position
            int renderX = PLAY_GRID_OFFSET_X + x * PLAY_TILE_SIZE;
            int renderY = PLAY_GRID_OFFSET_Y + y * PLAY_TILE_SIZE;

            //Color setting to make it highlighted
            g.setColor(new Color(222, 222, 176, 128)); // Light yellow with high transparency
            g.drawRect(renderX, renderY, PLAY_TILE_SIZE, PLAY_TILE_SIZE);
            g.setColor(new Color(225, 225, 183, 64)); // Even lighter yellow
            g.fillRect(renderX + 2, renderY + 2, PLAY_TILE_SIZE - 4, PLAY_TILE_SIZE - 4);
        }
    }

    // Renders the hero character sprite with effects like cloak or immunity
    private void renderHero(Graphics2D g, Hero hero) {
        BufferedImage heroSprite = GameManager.isCloakActive()
                ? Textures.getSprite("protection")
                : Textures.getSprite("player");

        if (heroSprite == null) {
            System.err.println("[PlayModeRenderer]: Failed to load hero sprite");
            return;
        }

        // Set transparency
        float transparency = hero.isImmune() ? 0.5f : 1f;
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));

        // Determine flipping and alignment
        boolean shouldFlip = (GameManager.isCloakActive() && hero.getFacing() == Direction.RIGHT)
                || (!GameManager.isCloakActive() && hero.getFacing() != Direction.RIGHT);

        drawEntitySprite(g, heroSprite, hero.getPositionX(), hero.getPositionY(), shouldFlip);

        // Reset transparency
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    // Renders all active monsters on the grid
    private void renderMonsters(Graphics2D g, List<Monster> monsters) {
        for (Monster monster : monsters) {
            // Determine the sprite based on monster type
            String spriteName = switch (monster.getType()) {
                case ARCHER -> "archer4x";
                case FIGHTER -> "fighter4x";
                case WIZARD -> "wizard4x";
            };

            BufferedImage monsterSprite = Textures.getSprite(spriteName);
            if (monsterSprite != null) {
                // Determine if the monster is facing left or right
                boolean shouldFlip = monster.getFacing() == Direction.RIGHT;

                // Use the shared drawHeroSprite method to render the monster
                drawEntitySprite(g, monsterSprite, monster.getPositionX(), monster.getPositionY(), !shouldFlip);
            } else {
                System.err.println("[PlayModeRenderer]: Failed to load monster sprite for " + monster.getType());
            }
        }
    }

    // Helper method to draw the sprite
    private void drawEntitySprite(Graphics2D g, BufferedImage sprite, int positionX, int positionY, boolean flip) {
        int x = PLAY_GRID_OFFSET_X + (flip ? positionX + 1 : positionX) * PLAY_TILE_SIZE;
        int width = flip ? -PLAY_TILE_SIZE : PLAY_TILE_SIZE;
        int y = PLAY_GRID_OFFSET_Y + positionY * PLAY_TILE_SIZE;

        g.drawImage(sprite, x, y, width, PLAY_TILE_SIZE, null);
    }

    // Renders arrows fired by monsters
    private void renderArrows(Graphics2D g, MonsterManager monsterManager) {
        for (Arrow arrow : monsterManager.getArrows()) {
            BufferedImage arrowSprite = null;

            switch (arrow.getDirection()) {
                case UP -> arrowSprite = Textures.getSprite("arrowup");
                case DOWN -> arrowSprite = Textures.getSprite("arrowdown");
                case LEFT -> arrowSprite = Textures.getSprite("arrowleft");
                case RIGHT -> arrowSprite = Textures.getSprite("arrowright");
            }

            if (arrowSprite != null) {
                g.drawImage(arrowSprite,
                        PLAY_GRID_OFFSET_X + arrow.getX() * PLAY_TILE_SIZE,
                        PLAY_GRID_OFFSET_Y + arrow.getY() * PLAY_TILE_SIZE,
                        PLAY_TILE_SIZE, PLAY_TILE_SIZE, null);
            }
        }
    }

    // Draws the HUD (heads-up display) showing the hero's status.
    private void renderHUD(Graphics2D g, Hero hero, int remainingTime) {
        int HUD_X = PLAY_GRID_OFFSET_X + GameManager.getCurrentHall().getWidth() * PLAY_TILE_SIZE + 30;
        int HUD_Y = PLAY_GRID_OFFSET_Y;
        int HUD_WIDTH = 200;

        HallGrid currentHall = GameManager.getCurrentHall();
        int gridHeight = currentHall.getHeight() * PLAY_TILE_SIZE;

        // Background of the HUD
        g.setColor(new Color(163, 137, 134)); // A soft purple-beige color
        g.fillRoundRect(HUD_X, HUD_Y, HUD_WIDTH, gridHeight, 20, 20);

        // Display remaining time
        g.setFont(customFont);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Time:", HUD_X + 15, HUD_Y + 90);
        g.drawString(remainingTime + " seconds", HUD_X + 15, HUD_Y + 110);

        renderHearts(g, hero, HUD_X + 15, HUD_Y + 150); // Render hero's lives
        renderInventory(g, HUD_X + 15, HUD_Y + 190); // Render inventory items
    }

    // Renders hero's lives as heart icons.
    private void renderHearts(Graphics2D g, Hero hero, int heartX, int heartY) {
        BufferedImage heartImg = Textures.getSprite("heart");
        if (heartImg != null) {
            int heartSize = 28;
            int spacing = 5;
            for (int i = 0; i < hero.getLives(); i++) {
                g.drawImage(heartImg, heartX + i * (heartSize + spacing), heartY, heartSize, heartSize, null);
            }
        }
    }

    // Draws the inventory UI and items collected by the hero.
    private void renderInventory(Graphics2D g, int inventoryX, int inventoryY) {
        BufferedImage inventoryBg = Textures.getSprite("Inventory");
        int inventoryWidth = 180;
        int inventoryHeight = 270;

        // Background for the inventory
        if (inventoryBg != null) {
            g.drawImage(inventoryBg, inventoryX, inventoryY, inventoryWidth, inventoryHeight, null);
        }

        // Render individual items in the inventory
        int slotSize = 30;
        int gap = 10;
        int itemStartX = inventoryX + 35;
        int itemStartY = inventoryY + 100;

        List<Enchantment.EnchantmentType> heroInventory = GameManager.getHero().getInventory().getEnchantments();
        for (int i = 0; i < heroInventory.size(); i++) {
            g.setColor(new Color(80, 80, 100));
            g.fillRect(itemStartX + (i % 3) * (slotSize + gap),
                    itemStartY + (i / 3) * (slotSize + gap),
                    slotSize, slotSize);

            BufferedImage itemImg;
            switch (heroInventory.get(i).getName()) {
                case "extra_time" -> itemImg = Textures.getSprite("extra_time_1_");
                case "extra_life" -> itemImg = Textures.getSprite("extraheart");
                case "reveal" -> itemImg = Textures.getSprite("reveal");
                case "luring_gem" -> itemImg = Textures.getSprite("luringgem");
                case "cloak_of_protection" -> itemImg = Textures.getSprite("protection");
                default -> itemImg = Textures.getSprite("black");
            }
            if (itemImg != null) {
                g.drawImage(itemImg,
                        itemStartX + (i % 3) * (slotSize + gap),
                        itemStartY + (i / 3) * (slotSize + gap),
                        slotSize, slotSize, null);
            }
        }

        // Display active effects (cloak and reveal timers)
        renderActiveEffects(g, inventoryX, inventoryY, inventoryHeight);
    }

    // Displays active effects like cloak and reveal timers.
    private void renderActiveEffects(Graphics2D g, int inventoryX, int inventoryY, int inventoryHeight) {
        if (GameManager.isCloakActive()) {
            int remainingCloakTime = TimeManager.remainingCloakTimer();
            if (remainingCloakTime > 0) {
                g.setFont(customFont);
                g.setColor(Color.WHITE);
                g.drawString("Cloak: " + remainingCloakTime + "s", inventoryX + 10, inventoryY + inventoryHeight + 20);
            }
        }
        if (GameManager.isRevealActive()) {
            int remainingRevealTime = TimeManager.remainingRevealTimer();
            if (remainingRevealTime > 0) {
                g.setFont(customFont);
                g.setColor(Color.WHITE);
                g.drawString("Reveal: " + remainingRevealTime + "s", inventoryX + 10, inventoryY + inventoryHeight + 40);
            }
        }
    }

    // Draws exit and pause buttons.
    public void renderControllerButtons(Graphics2D g, Rectangle exitButtonBounds, Rectangle pauseButtonBounds) {
        BufferedImage exitSprite = Textures.getSprite("exit");
        if (exitSprite != null) {
            g.drawImage(exitSprite, exitButtonBounds.x, exitButtonBounds.y, exitButtonBounds.width, exitButtonBounds.height, null);
        }
        String spriteName = GameManager.isPaused() ? "resume4x" : "pause";
        BufferedImage pauseSprite = Textures.getSprite(spriteName);
        if (pauseSprite != null) {
            g.drawImage(pauseSprite, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height, null);
        }
    }

}