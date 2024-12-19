package org.RokueLike.domain.entity.monster;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.item.Enchantment;
import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.ui.Textures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PlayModeRenderer {

    private static final int TILE_SIZE = 32;  // Tile size
    private static final int GRID_OFFSET_X = 50;  // Grid X start
    private static final int GRID_OFFSET_Y = 50;  // Grid Y start

    public PlayModeRenderer() {
        // Initialize Textures and load custom sprites
        Textures.init();
        addCustomSprites();
    }

    /**
     * Adds custom sprites to the Textures class.
     */
    private void addCustomSprites() {
        Textures.addSprite("player", Textures.loadPNG("imagesekstra/player.png"));
        Textures.addSprite("archer", Textures.loadPNG("imagesekstra/archer4x.png"));
        Textures.addSprite("wizard", Textures.loadPNG("imagesekstra/wizard4x.png"));
        Textures.addSprite("heart", Textures.loadPNG("imagesekstra/heart.png"));
        Textures.addSprite("extra_time", Textures.loadPNG("imagesekstra/extra_time.png"));
        Textures.addSprite("extra_life", Textures.loadPNG("imagesekstra/extraheart.png"));
        Textures.addSprite("reveal", Textures.loadPNG("imagesekstra/reveal.png"));
        Textures.addSprite("cloak", Textures.loadPNG("imagesekstra/protection.png"));
        Textures.addSprite("luring_gem", Textures.loadPNG("imagesekstra/luringgem.png"));
        System.out.println("[PlayModeRenderer]: Custom sprites loaded successfully!");
    }

    public void renderPlayMode(Graphics2D g) {
        HallGrid currentHall = GameManager.getCurrentHall();
        Hero hero = GameManager.getHero();
        List<Monster> monsters = GameManager.getActiveMonsters();

        if (currentHall == null || hero == null) return;

        renderGrid(g, currentHall);
        renderMonsters(g, monsters);
        renderHero(g, hero);
        renderEnchantments(g, currentHall);

        int remainingTime = hero.getRemainingTime();
        renderHUD(g, hero, remainingTime);
    }

    private void renderGrid(Graphics2D g, HallGrid hall) {
        for (int y = 0; y < hall.getHeight(); y++) {
            for (int x = 0; x < hall.getWidth(); x++) {
                GridCell cell = hall.getCell(x, y);
                drawTile(g, cell, x, y);
            }
        }
    }

    private void drawTile(Graphics2D g, GridCell cell, int x, int y) {
        BufferedImage img = switch (cell.getName()) {
            case "wall" -> Textures.getSprite("wall_center");
            case "floor" -> Textures.getSprite("floor_plain");
            case "door" -> Textures.getSprite("door_closed");
            case "chest" -> Textures.getSprite("chest_closed");
            case "skull" -> Textures.getSprite("skull");
            case "column" -> Textures.getSprite("column");
            case "extra_time_enchantment" -> Textures.getSprite("extra_time");
            case "extra_life_enchantment" -> Textures.getSprite("extra_life");
            case "reveal_enchantment" -> Textures.getSprite("reveal");
            case "cloak_of_protection_enchantment" -> Textures.getSprite("cloak");
            case "luring_gem_enchantment" -> Textures.getSprite("luring_gem");
            default -> Textures.getSprite("black");
        };

        if (img != null) {
            g.drawImage(img, GRID_OFFSET_X + x * TILE_SIZE, GRID_OFFSET_Y + y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        } else {
            System.err.println("[Textures]: Missing sprite for " + cell.getName());
        }
    }
    private void renderEnchantments(Graphics2D g, HallGrid hall) {
        for (int y = 0; y < hall.getHeight(); y++) {
            for (int x = 0; x < hall.getWidth(); x++) {
                GridCell cell = hall.getCell(x, y);
                if (cell instanceof Enchantment enchantment) {
                    BufferedImage img = Textures.getSprite(enchantment.getEnchantmentType().getName());
                    if (img != null) {
                        g.drawImage(img,
                                GRID_OFFSET_X + x * TILE_SIZE,
                                GRID_OFFSET_Y + y * TILE_SIZE,
                                TILE_SIZE, TILE_SIZE, null);
                    }
                }
            }
        }
    }


    private void renderHUD(Graphics2D g, Hero hero, int remainingTime) {
        int HUD_X = 550; // HUD X position
        int HUD_Y = 50;  // HUD Y position
        int HUD_WIDTH = 200; // HUD width

        // HUD Background
        g.setColor(new Color(50, 25, 50)); // Dark purple
        g.fillRoundRect(HUD_X, HUD_Y, HUD_WIDTH, 400, 20, 20);

        // Time Section
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Time:", HUD_X + 20, HUD_Y + 60);
        g.drawString(remainingTime + " seconds", HUD_X + 80, HUD_Y + 60);

        // Health Section (Hearts)
        renderHearts(g, hero, HUD_X + 20, HUD_Y + 100);

        // Inventory
        renderInventory(g, HUD_X + 20, HUD_Y + 140);
    }

    private void renderHearts(Graphics2D g, Hero hero, int heartX, int heartY) {
        BufferedImage heartImg = Textures.getSprite("heart");
        if (heartImg != null) {
            int heartSize = 32; // Size of each heart
            int spacing = 5;    // Spacing between hearts
            for (int i = 0; i < hero.getLives(); i++) {
                g.drawImage(heartImg, heartX + i * (heartSize + spacing), heartY, heartSize, heartSize, null);
            }
        } else {
            System.err.println("[Textures]: Heart sprite not found!");
        }
    }

    private void renderInventory(Graphics2D g, int inventoryX, int inventoryY) {
        BufferedImage inventoryBg = Textures.getSprite("Inventory");
        int inventoryWidth = 160;  // Adjust the width if needed
        int inventoryHeight = 200; // Increase the height to make it larger

        // Draw the inventory background
        if (inventoryBg != null) {
            g.drawImage(inventoryBg, inventoryX, inventoryY, inventoryWidth, inventoryHeight, null);
        }

        // Inventory Slots (static demo items)
        int slotSize = 40;
        int gap = 10;
        int itemStartX = inventoryX + 10;
        int itemStartY = inventoryY + 60; // Adjusted start Y position to fit the taller inventory

        // Example items: Replace with dynamic item fetching if needed
        String[] demoItems = {"extra_time", "extra_life", "reveal", "cloak", "luring_gem"};

        for (int i = 0; i < 6; i++) { // Draw 6 slots
            g.setColor(new Color(80, 80, 100)); // Slot background
            g.fillRect(itemStartX + (i % 3) * (slotSize + gap), itemStartY + (i / 3) * (slotSize + gap), slotSize, slotSize);

            if (i < demoItems.length) {
                BufferedImage itemImg = Textures.getSprite(demoItems[i]);
                if (itemImg != null) {
                    g.drawImage(itemImg,
                            itemStartX + (i % 3) * (slotSize + gap),
                            itemStartY + (i / 3) * (slotSize + gap),
                            slotSize, slotSize, null);
                }
            }
        }
    }

    private void renderHero(Graphics2D g, Hero hero) {
        BufferedImage heroSprite = Textures.getSprite("player");
        if (heroSprite != null) {
            g.drawImage(heroSprite,
                    GRID_OFFSET_X + hero.getPositionX() * TILE_SIZE,
                    GRID_OFFSET_Y + hero.getPositionY() * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE, null);
        }
    }

    private void renderMonsters(Graphics2D g, List<Monster> monsters) {
        for (Monster monster : monsters) {
            String spriteName = switch (monster.getType()) {
                case ARCHER -> "archer";
                case FIGHTER -> "fighter4x";
                case WIZARD -> "wizard";
            };

            BufferedImage monsterSprite = Textures.getSprite(spriteName);
            if (monsterSprite != null) {
                g.drawImage(monsterSprite,
                        GRID_OFFSET_X + monster.getPositionX() * TILE_SIZE,
                        GRID_OFFSET_Y + monster.getPositionY() * TILE_SIZE,
                        TILE_SIZE, TILE_SIZE, null);
            }
        }
    }
}
