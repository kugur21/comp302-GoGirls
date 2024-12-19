package org.RokueLike.ui.render;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.item.Enchantment;
import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.utils.MessageBox;
import org.RokueLike.ui.FontLoader;
import org.RokueLike.ui.Textures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PlayModeRenderer {

    private static final int TILE_SIZE = 36;  // Tile size
    private static final int GRID_OFFSET_X = 70;  // Grid X başlangıcı (100'den 70'e kaydırıldı)
    private static final int GRID_OFFSET_Y = 50;  // Grid Y başlangıcı
    private static final int WINDOW_WIDTH = 1150;  // Pencere genişliği
    private static final int WINDOW_HEIGHT = 850;  // Pencere yüksekliği
    private Font pixelFont; // Özel piksel fontu

    private final Rectangle messageBox = new Rectangle(300, 300, 550, 100);

    public PlayModeRenderer() {
        // Textures ve font yükleme
        Textures.init();
        addCustomSprites();
        pixelFont = FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 12f); // Piksel font
    }

    /**
     * Özel sprite'ları Textures sınıfına ekler.
     */
    private void addCustomSprites() {
        Textures.addSprite("player", Textures.loadPNG("imagesekstra/player.png"));
        Textures.addSprite("archer", Textures.loadPNG("imagesekstra/archer4x.png"));
        Textures.addSprite("wizard", Textures.loadPNG("imagesekstra/wizard4x.png"));
        Textures.addSprite("heart", Textures.loadPNG("imagesekstra/heart.png"));
        Textures.addSprite("extra_time", Textures.loadPNG("imagesekstra/extra_time.png"));
        Textures.addSprite("extra_life", Textures.loadPNG("imagesekstra/extraheart.png"));
        Textures.addSprite("reveal", Textures.loadPNG("imagesekstra/reveal.png"));
        Textures.addSprite("cloak_of_protection", Textures.loadPNG("imagesekstra/protection.png"));
        Textures.addSprite("luring_gem", Textures.loadPNG("imagesekstra/luringgem.png"));
        Textures.addSprite("exit_button", Textures.loadPNG("imagesekstra/exit.png"));
        Textures.addSprite("pause_button", Textures.loadPNG("imagesekstra/pause.png"));
        System.out.println("[PlayModeRenderer]: Custom sprites loaded successfully!");
    }

    public void renderPlayMode(Graphics2D g) {
        HallGrid currentHall = GameManager.getCurrentHall();
        Hero hero = GameManager.getHero();
        List<Monster> monsters = GameManager.getActiveMonsters();

        if (currentHall == null || hero == null) return;

        // Arka planı floor_plain sprite'ı ile doldur
        BufferedImage floorPlainImage = Textures.getSprite("floor_plain");
        if (floorPlainImage != null) {
            for (int y = 0; y < WINDOW_HEIGHT / TILE_SIZE; y++) {
                for (int x = 0; x < WINDOW_WIDTH / TILE_SIZE; x++) {
                    g.drawImage(floorPlainImage, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }

        renderGrid(g, currentHall);
        renderMonsters(g, monsters);
        renderHero(g, hero);
        renderEnchantments(g, currentHall);

        int remainingTime = hero.getRemainingTime();
        renderHUD(g, hero, remainingTime);
    }


    private void renderGrid(Graphics2D g, HallGrid hall) {
        BufferedImage floorImage = Textures.getSprite("floor_plain");
        if (floorImage != null) {
            for (int y = 0; y < hall.getHeight(); y++) {
                for (int x = 0; x < hall.getWidth(); x++) {
                    g.drawImage(floorImage, GRID_OFFSET_X + x * TILE_SIZE, GRID_OFFSET_Y + y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        } else {
            System.err.println("[Textures]: Missing sprite for 'floor_plain'");
        }

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
            case "cloak_of_protection" -> Textures.getSprite("cloak");
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
        int HUD_X = GRID_OFFSET_X + GameManager.getCurrentHall().getWidth() * TILE_SIZE + 30; // Daha sola kaydırıldı
        int HUD_Y = GRID_OFFSET_Y;
        int HUD_WIDTH = 200;

        HallGrid currentHall = GameManager.getCurrentHall();
        int gridHeight = currentHall.getHeight() * TILE_SIZE;

        g.setColor(new Color(163, 137, 134)); // Yeni morumsu ve buğdayımsı renk

        g.fillRoundRect(HUD_X, HUD_Y, HUD_WIDTH, gridHeight, 20, 20);

        renderControlButtons(g, HUD_X + 15, HUD_Y + 20);

        g.setFont(pixelFont);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Time:", HUD_X + 15, HUD_Y + 90);
        g.drawString(remainingTime + " seconds", HUD_X + 15, HUD_Y + 110);

        renderHearts(g, hero, HUD_X + 15, HUD_Y + 150);
        renderInventory(g, HUD_X + 15, HUD_Y + 190);
    }

    private void renderControlButtons(Graphics2D g, int controlX, int controlY) {
        BufferedImage exitButton = Textures.getSprite("exit_button");
        if (exitButton != null) {
            g.drawImage(exitButton, controlX, controlY, 32, 32, null);
        }

        BufferedImage pauseButton = Textures.getSprite("pause_button");
        if (pauseButton != null) {
            g.drawImage(pauseButton, controlX + 40, controlY, 32, 32, null);
        }
    }

    private void renderHearts(Graphics2D g, Hero hero, int heartX, int heartY) {
        BufferedImage heartImg = Textures.getSprite("heart");
        if (heartImg != null) {
            int heartSize = 28; // Heart boyutu küçültüldü
            int spacing = 5;
            for (int i = 0; i < hero.getLives(); i++) {
                g.drawImage(heartImg, heartX + i * (heartSize + spacing), heartY, heartSize, heartSize, null);
            }
        }
    }

    private void renderInventory(Graphics2D g, int inventoryX, int inventoryY) {
        BufferedImage inventoryBg = Textures.getSprite("Inventory");
        int inventoryWidth = 180;
        int inventoryHeight = 270;

        if (inventoryBg != null) {
            g.drawImage(inventoryBg, inventoryX, inventoryY, inventoryWidth, inventoryHeight, null);
        }

        int slotSize = 30;
        int gap = 10;
        int itemStartX = inventoryX + 35; // Daha sola kaydırıldı
        int itemStartY = inventoryY + 100;

        String[] demoItems = {"extra_time", "extra_life", "reveal", "cloak", "luring_gem"};
        for (int i = 0; i < 6; i++) {
            g.setColor(new Color(80, 80, 100));
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
