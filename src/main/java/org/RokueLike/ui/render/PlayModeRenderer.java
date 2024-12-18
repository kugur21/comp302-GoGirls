package org.RokueLike.ui.render;

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
import java.util.ArrayList;

public class PlayModeRenderer {

    private static final int TILE_SIZE = 32;  // Tile size
    private static final int GRID_OFFSET_X = 50;  // Grid X start
    private static final int GRID_OFFSET_Y = 50;  // Grid Y start

    public void renderPlayMode(Graphics2D g) {
        HallGrid currentHall = GameManager.getCurrentHall();
        Hero hero = GameManager.getHero();
        List<Monster> monsters = GameManager.getActiveMonsters();

        if (currentHall == null || hero == null) return;

        renderGrid(g, currentHall);
        renderMonsters(g, monsters);
        renderHero(g, hero);

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
            case "object" -> Textures.getSprite("box"); // Default placeholder
            default -> Textures.getSprite("black");
        };

        if (img != null) {
            g.drawImage(img, GRID_OFFSET_X + x * TILE_SIZE, GRID_OFFSET_Y + y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        } else {
            System.err.println("[Textures]: Missing sprite for " + cell.getName());
        }
    }

    private void renderHUD(Graphics2D g, Hero hero, int remainingTime) {
        int HUD_X = 550; // HUD X position
        int HUD_Y = 50;  // HUD Y position
        int HUD_WIDTH = 200; // HUD width

        // HUD Background
        g.setColor(new Color(50, 25, 50)); // Dark purple
        g.fillRoundRect(HUD_X, HUD_Y, HUD_WIDTH, 400, 20, 20);

        // Play/Pause Buttons

        // Time Section
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Time:", HUD_X + 20, HUD_Y + 60);
        g.drawString(remainingTime + " seconds", HUD_X + 80, HUD_Y + 60);

        // Health Section (Hearts)


        // Inventory
        renderInventory(g, hero, HUD_X + 20, HUD_Y + 140);
    }

    private void renderInventory(Graphics2D g, Hero hero, int inventoryX, int inventoryY) {
        BufferedImage inventoryBg = Textures.getSprite("Inventory");
        if (inventoryBg != null) {
            g.drawImage(inventoryBg, inventoryX, inventoryY, 160, 160, null);
        }

        // Inventory Slots and Items
        int slotSize = 40;
        int gap = 10;
        int itemStartX = inventoryX + 10;
        int itemStartY = inventoryY + 40;

        List<BufferedImage> itemImages = new ArrayList<>();
        for (Enchantment.EnchantmentType item : hero.getInventory().getItems()) {
            BufferedImage img = Textures.getSprite(item.getName());
            if (img != null) {
                itemImages.add(img);
            }
        }

        for (int i = 0; i < 6; i++) { // Draw 6 slots
            g.setColor(new Color(80, 80, 100)); // Slot background
            g.fillRect(itemStartX + (i % 3) * (slotSize + gap), itemStartY + (i / 3) * (slotSize + gap), slotSize, slotSize);

            if (i < itemImages.size()) {
                BufferedImage itemImg = itemImages.get(i);
                g.drawImage(itemImg,
                        itemStartX + (i % 3) * (slotSize + gap),
                        itemStartY + (i / 3) * (slotSize + gap),
                        slotSize, slotSize, null);
            }
        }
    }

    private void renderHero(Graphics2D g, Hero hero) {
        BufferedImage heroSprite = Textures.getSprite("npc_elf");
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
                case ARCHER -> "npc_dwarf";
                case FIGHTER -> "npc_knight_blue";
                case WIZARD -> "npc_wizzard";
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
