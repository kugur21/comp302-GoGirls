
package org.RokueLike.ui.render;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.item.Door;
import org.RokueLike.domain.entity.item.Enchantment;
import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.utils.MessageBox;
import org.RokueLike.ui.FontLoader;
import org.RokueLike.ui.Textures;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.RokueLike.domain.GameManager.isPaused;

public class PlayModeRenderer {

    private static final int TILE_SIZE = 36;  // Tile size
    private static final int GRID_OFFSET_X = 70;  // Grid X başlangıcı (100'den 70'e kaydırıldı)
    private static final int GRID_OFFSET_Y = 50;  // Grid Y başlangıcı
    private static final int WINDOW_WIDTH = 1150;  // Pencere genişliği
    private static final int WINDOW_HEIGHT = 850;  // Pencere yüksekliği
    private Font pixelFont; // Özel piksel fontu

    public static final Rectangle messageBox = new Rectangle(200, 480, 600, 50);

    public PlayModeRenderer() {
        // Textures ve font yükleme
        //Textures.init(); // Main de initialize edildiği için burada tekrar çağırılmasına gerek yok
        addCustomSprites();
        pixelFont = FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 12f); // Piksel font
    }

    ////MODEL-VIEW SEPARATION PRINCIPLE: Renderer classes (BuildModeRenderer, PlayModeRenderer) manage graphical representation and rendering tasks without interfering with the model logic.

    /**
     * Özel sprite'ları Textures sınıfına ekler.
     */
    private void addCustomSprites() {
        Textures.addSprite("player", Textures.loadPNG("imagesekstra/player.png"));
        Textures.addSprite("player_cloak", Textures.loadPNG("imagesekstra/player_cloak.png"));
        Textures.addSprite("archer", Textures.loadPNG("imagesekstra/archer4x.png"));
        Textures.addSprite("wizard", Textures.loadPNG("imagesekstra/wizard4x.png"));
        Textures.addSprite("heart", Textures.loadPNG("imagesekstra/heart.png"));
        Textures.addSprite("extra_time", Textures.loadPNG("imagesekstra/extra_time_1_.png"));
        Textures.addSprite("extra_life", Textures.loadPNG("imagesekstra/extraheart.png"));
        Textures.addSprite("reveal", Textures.loadPNG("imagesekstra/reveal.png"));
        Textures.addSprite("cloak_of_protection", Textures.loadPNG("imagesekstra/protection.png"));
        Textures.addSprite("luring_gem", Textures.loadPNG("imagesekstra/luringgem.png"));
        Textures.addSprite("exit_button", Textures.loadPNG("imagesekstra/exit.png"));
        Textures.addSprite("pause_button", Textures.loadPNG("imagesekstra/pause.png"));
        Textures.addSprite("resume_button", Textures.loadPNG("imagesekstra/resume4x.png"));

        System.out.println("[PlayModeRenderer]: Custom sprites loaded successfully!");
    }

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
        renderMonsters(g, monsters);
        renderHero(g, hero);
        renderEnchantments(g, currentHall);

        int remainingTime = hero.getRemainingTime();
        renderHUD(g, hero, remainingTime);
        renderControllerButtons(g, exitButtonBounds, pauseButtonBounds);
        renderMessageBox(g, messageBox);
    }

    private void renderFloor(Graphics2D g) {
        BufferedImage floorPlainImage = Textures.getSprite("floor_plain");
        if (floorPlainImage != null) {
            for (int y = 0; y < WINDOW_HEIGHT / TILE_SIZE; y++) {
                for (int x = 0; x < WINDOW_WIDTH / TILE_SIZE; x++) {
                    g.drawImage(floorPlainImage, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }

    private void renderMudClusters(Graphics2D g) {
        for (int y = 0; y < WINDOW_HEIGHT / TILE_SIZE; y += 12) {
            for (int x = 0; x < WINDOW_WIDTH / TILE_SIZE; x += 12) {
                renderMudPattern(g, x, y);
            }
        }
    }

    private void renderMudPattern(Graphics2D g, int clusterX, int clusterY) {

        g.drawImage(Textures.getSprite("floor_mud_nw"), clusterX * TILE_SIZE, clusterY * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        g.drawImage(Textures.getSprite("floor_mud_n_1"), (clusterX + 1) * TILE_SIZE, clusterY * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        g.drawImage(Textures.getSprite("floor_mud_n_2"), (clusterX + 2) * TILE_SIZE, clusterY * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        g.drawImage(Textures.getSprite("floor_mud_ne"), (clusterX + 3) * TILE_SIZE, clusterY * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);


        g.drawImage(Textures.getSprite("floor_mud_w"), clusterX * TILE_SIZE, (clusterY + 1) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        g.drawImage(Textures.getSprite("floor_mud_mid_1"), (clusterX + 1) * TILE_SIZE, (clusterY + 1) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        g.drawImage(Textures.getSprite("floor_mud_mid_2"), (clusterX + 2) * TILE_SIZE, (clusterY + 1) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        g.drawImage(Textures.getSprite("floor_mud_e"), (clusterX + 3) * TILE_SIZE, (clusterY + 1) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);


        g.drawImage(Textures.getSprite("floor_mud_sw"), clusterX * TILE_SIZE, (clusterY + 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        g.drawImage(Textures.getSprite("floor_mud_s_1"), (clusterX + 1) * TILE_SIZE, (clusterY + 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        g.drawImage(Textures.getSprite("floor_mud_s_2"), (clusterX + 2) * TILE_SIZE, (clusterY + 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        g.drawImage(Textures.getSprite("floor_mud_se"), (clusterX + 3) * TILE_SIZE, (clusterY + 2) * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
    }



    private void renderGrid(Graphics2D g, HallGrid hall) {
        BufferedImage floorImage = Textures.getSprite("floor_plain");

        for (int y = 0; y < hall.getHeight(); y++) {
            for (int x = 0; x < hall.getWidth(); x++) {
                // Render floor_plain first
                if (floorImage != null) {
                    g.drawImage(floorImage, GRID_OFFSET_X + x * TILE_SIZE, GRID_OFFSET_Y + y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }


                if ((x % 6 == 0 && y % 6 == 0)) {
                    renderMudPattern(g, GRID_OFFSET_X + x * TILE_SIZE, GRID_OFFSET_Y + y * TILE_SIZE);
                }


                GridCell cell = hall.getCell(x, y);
                drawTile(g, cell, x, y);
            }
        }
    }



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

            int doorSize = TILE_SIZE + 12; // Kapının boyutunu artırmak için ekleme yapıldı
            if (img != null) {
                g.drawImage(img,
                        GRID_OFFSET_X + x * TILE_SIZE - (doorSize - TILE_SIZE) / 2,
                        GRID_OFFSET_Y + y * TILE_SIZE - (doorSize - TILE_SIZE) / 2,
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
                case "skull" -> Textures.getSprite("skull");
                case "wall" -> Textures.getSprite("wall_center");
                case "floor" -> Textures.getSprite("floor_plain");
                case "extra_time_enchantment" -> Textures.getSprite("extra_time");
                case "extra_life_enchantment" -> Textures.getSprite("extra_life");
                case "reveal_enchantment" -> Textures.getSprite("reveal");
                case "luring_gem_enchantment" -> Textures.getSprite("luring_gem");
                default -> Textures.getSprite("black");
            };
        }
        if (img != null) {
            g.drawImage(img, GRID_OFFSET_X + x * TILE_SIZE, GRID_OFFSET_Y + y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        } else {
            System.err.println("[Textures]: Missing sprite for " + cell.getName());
        }
    }



    private void renderRuneRegion(Graphics2D g, HallGrid hall) {
        if (!GameManager.isRevealActive()) {
            return;
        }

        //Activation Check above

        int[][] runeRegion = hall.findRuneRegion(3); // Adjust the bound parameter as needed
        if (runeRegion == null || runeRegion.length == 0) {
            return; // Exit if no rune region is defined
        }
        //
        // Rune region looping starts below
        for (int[] coord : runeRegion) {
            int x = coord[0];
            int y = coord[1];

            // Calculate position
            int renderX = GRID_OFFSET_X + x * TILE_SIZE;
            int renderY = GRID_OFFSET_Y + y * TILE_SIZE;

            //Color setting to make it highlighted
            g.setColor(new Color(222, 222, 176, 128)); // Light yellow with high transparency
            g.drawRect(renderX, renderY, TILE_SIZE, TILE_SIZE);


            g.setColor(new Color(225, 225, 183, 64)); // Even lighter yellow
            g.fillRect(renderX + 2, renderY + 2, TILE_SIZE - 4, TILE_SIZE - 4);

        }


        // TODO: Highlight the rune region
        // Get the rune region with hall.findRuneRegion()

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

        g.setFont(pixelFont);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Time:", HUD_X + 15, HUD_Y + 90);
        g.drawString(remainingTime + " seconds", HUD_X + 15, HUD_Y + 110);

        renderHearts(g, hero, HUD_X + 15, HUD_Y + 150);
        renderInventory(g, HUD_X + 15, HUD_Y + 190);

    }

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

    private void renderInventory(Graphics2D g, int inventoryX, int inventoryY) {
        BufferedImage inventoryBg = Textures.getSprite("Inventory");
        int inventoryWidth = 180;
        int inventoryHeight = 270;

        if (inventoryBg != null) {
            g.drawImage(inventoryBg, inventoryX, inventoryY, inventoryWidth, inventoryHeight, null);
        }

        int slotSize = 30;
        int gap = 10;
        int itemStartX = inventoryX + 35;
        int itemStartY = inventoryY + 100;

        List<Enchantment.EnchantmentType> heroInventory = GameManager.getHero().getInventory().getItems();

        for (int i = 0; i < heroInventory.size(); i++) {
            g.setColor(new Color(80, 80, 100));
            g.fillRect(itemStartX + (i % 3) * (slotSize + gap),
                    itemStartY + (i / 3) * (slotSize + gap),
                    slotSize, slotSize);

            BufferedImage itemImg = Textures.getSprite(heroInventory.get(i).getName());
            if (itemImg != null) {
                g.drawImage(itemImg,
                        itemStartX + (i % 3) * (slotSize + gap),
                        itemStartY + (i / 3) * (slotSize + gap),
                        slotSize, slotSize, null);
            }
        }
        if (GameManager.isCloakActive()) {
            int remainingCloakTime = GameManager.remainingCloakTimer();
            if (remainingCloakTime > 0) {
                g.setFont(pixelFont);
                g.setColor(Color.WHITE);

                int cloakTextX = inventoryX + 10;
                int cloakTextY = inventoryY + inventoryHeight + 20;

                g.drawString("Cloak: " + remainingCloakTime + "s", cloakTextX, cloakTextY);
            }
        }
        if (GameManager.isRevealActive()) {
            int remainingRevealTime = GameManager.remainingRevealTimer();
            if (remainingRevealTime > 0) {
                g.setFont(pixelFont);
                g.setColor(Color.WHITE);

                int cloakTextX = inventoryX + 10;
                int cloakTextY = inventoryY + inventoryHeight + 20;

                g.drawString("Reveal: " + remainingRevealTime + "s", cloakTextX, cloakTextY);
            }
        }
    }

    public void renderControllerButtons(Graphics2D g, Rectangle exitButtonBounds, Rectangle pauseButtonBounds) {
        BufferedImage exitSprite = Textures.getSprite("exit_button");
        if (exitSprite != null) {
            g.drawImage(exitSprite, exitButtonBounds.x, exitButtonBounds.y, exitButtonBounds.width, exitButtonBounds.height, null);
        }
        String spriteName = GameManager.isPaused() ? "resume_button" : "pause_button";
        BufferedImage pauseSprite = Textures.getSprite(spriteName);
        if (pauseSprite != null) {
            g.drawImage(pauseSprite, pauseButtonBounds.x, pauseButtonBounds.y, pauseButtonBounds.width, pauseButtonBounds.height, null);
        }
    }

    public void renderMessageBox(Graphics2D g, MessageBox message) {
        if(message.getMessage() == null || message.getTime() <= 0)
            return;

        g.setColor(Color.BLACK);
        g.fillRoundRect(messageBox.x, messageBox.y, messageBox.width, messageBox.height, 10, 10);
        g.setColor(Color.WHITE);
        g.drawRoundRect(messageBox.x, messageBox.y, messageBox.width, messageBox.height, 10, 10);

        g.setFont(new Font("Dialog", Font.PLAIN, 20));

        try {
            int textPosX = messageBox.x + (messageBox.width - g.getFontMetrics().stringWidth(message.getMessage())) / 2;
            int textPosY = messageBox.y + ((messageBox.height - g.getFontMetrics().getHeight()) / 2) + g.getFontMetrics().getAscent();
            g.drawString(message.getMessage(), textPosX, textPosY);
        } catch(NullPointerException e) {
        }
    }

    private void renderHero(Graphics2D g, Hero hero) {
        BufferedImage heroSprite = Textures.getSprite("player"); // Default sprite
        if (heroSprite != null) {
            if (GameManager.isCloakActive()) {
                // Apply transparency
                heroSprite = Textures.getSprite("cloak_of_protection");
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // 50% transparency
            }

            g.drawImage(heroSprite,
                    GRID_OFFSET_X + hero.getPositionX() * TILE_SIZE,
                    GRID_OFFSET_Y + hero.getPositionY() * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE, null);

            // Reset transparency
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
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