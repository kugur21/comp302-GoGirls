package org.RokueLike.domain;

import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.hero.HeroManager;
import org.RokueLike.domain.entity.item.Object;
import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;
import org.RokueLike.domain.entity.item.ItemManager;
import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.domain.entity.monster.MonsterManager;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.hall.HallManager;
import org.RokueLike.domain.utils.Direction;
import org.RokueLike.domain.utils.MessageBox;


import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static Timer timer;
    private static int monsterSpawnTimer = 0;
    private static int enchantmentSpawnTimer = 0;
    private static int enchantmentDurationTimer = 0;
    private static int wizardTimer = 0;
    private static int frameCounter = 0;
    private static MessageBox messageBox;

    private static HallManager hallManager;
    private static HallGrid currentHall;
    private static Hero hero;
    private static HeroManager heroManager;
    private static List<Monster> activeMonsters;
    private static MonsterManager monsterManager;
    private static ItemManager itemManager;

    private static boolean onTitleScreen;

    public static void init() {

        System.out.println("[GameManager]: Starting game...");

        initHalls();
        initPlayMode();

        timer = new Timer(20, new GameLoop());
        timer.start();
    }

    public static void initHalls() {
        // Initialize hero spawn locations
        String[][][] hallData = BuildManager.getAllHalls();
        for (String[][] hall : hallData) {
            BuildManager.placeHeroRandomly(hall);
        }

        //Initialize halls
        List<HallGrid> halls = new ArrayList<>();
        String[] hallNames = {"Hall of Earth", "Hall of Air", "Hall of Water", "Hall of Fire"};
        for (int i = 0; i < hallData.length; i++) {
            HallGrid hall = new HallGrid(hallData[i], hallNames[i]);
            halls.add(hall);
        }
        hallManager = new HallManager(halls);
    }

    public static void initPlayMode() {
        currentHall = hallManager.getCurrentHall();
        hero = new Hero(currentHall.getStartX(), currentHall.getStartY());
        heroManager = new HeroManager(hero, currentHall);
        activeMonsters = currentHall.getMonsters();
        monsterManager = new MonsterManager(activeMonsters, currentHall, hero);
        itemManager = new ItemManager(currentHall, hero, monsterManager);
        messageBox = new MessageBox();

        onTitleScreen = true;
    }

    public static void genericLoop() {
        hero.decreaseMotionOffset();
        for (Monster monster: activeMonsters) {
            monster.decreaseMotionOffset();
        }
    }

    public static void handleHeroSpawn() {
        try {
            String response = heroManager.respawnHero();
            messageBox.addMessage(response, 50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleMonsterSpawn() {
        try {
            String response = monsterManager.spawnMonster();
            messageBox.addMessage(response, 50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleEnchantmentSpawn() {
        try {
            String response = itemManager.spawnEnchantment();
            enchantmentDurationTimer = 0;
            messageBox.addMessage(response, 50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleEnchantmentExpiration() {
        if (currentHall.getCurrentEnchantment() != null) {
            enchantmentDurationTimer++;
            if (enchantmentDurationTimer >= 300) {
                messageBox.addMessage("Enchantment expired: " + currentHall.getCurrentEnchantment().getEnchantmentType().getName(), 50);
                String response = itemManager.disappearEnchantment();
                enchantmentDurationTimer = 0;
                messageBox.addMessage(response, 30);
            }
        }
    }

    public static void handleMovement(int dirX, int dirY) {

        try {
            if (!isGameOver()) {
                boolean moved = heroManager.moveHero(hallManager, dirX, dirY);
                if (!moved) {
                    messageBox.addMessage("Hero cannot move in that direction.", 30);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        monsterManager.moveMonsters(messageBox);
    }

    public static void handleWizardBehavior() {
        for (Monster monster : activeMonsters) {
            if (monster.getType() == Monster.MonsterType.WIZARD) {
                String response = monsterManager.processWizardBehavior(monster);
                messageBox.addMessage(response, 30);
            }
        }
    }

    public static void handleEnchantmentUse(EnchantmentType enchantment) {
        handleEnchantmentUse(enchantment, null);
    }

    public static void handleEnchantmentUse(EnchantmentType enchantment, Direction direction) {
        try {
            String response = itemManager.useEnchantment(enchantment, direction);
            messageBox.addMessage(response, 50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleLureBehaviour() {
        try {
            monsterManager.updateLuredMonsters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleLeftClick(int mouseX, int mouseY) {
        try {
            if (currentHall.getCurrentEnchantment() != null
                    && currentHall.getCurrentEnchantment().getPositionX() == mouseX
                    && currentHall.getCurrentEnchantment().getPositionY() == mouseY) {

                String response = itemManager.collectEnchantment();
                messageBox.addMessage(response, 50);
            } else {
                messageBox.addMessage("No enchantment at clicked location (" + mouseX + ", " + mouseY + ").", 50);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error handling left-click.");
        }
    }

    public static void handleRightClick(int mouseX, int mouseY) {
        try {
            GridCell clickedCell = currentHall.getCell(mouseX, mouseY);
            if (clickedCell instanceof Object clickedObject) {
                if (heroManager.isAdjacentTo(mouseX, mouseY)) {
                    String response = itemManager.interactWithObject(clickedObject);
                    messageBox.addMessage(response, 50);
                } else {
                    messageBox.addMessage("Hero is not adjacent to the object at (" + mouseX + ", " + mouseY + ").", 50);
                }
            } else {
                messageBox.addMessage("Clicked cell is not an object at (" + mouseX + ", " + mouseY + ").", 50);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error handling right-click.");
        }
    }

    public static void updateCurrentHall(HallGrid nextHall) {
        currentHall = nextHall;
        hero.setPosition(currentHall.getStartX(), currentHall.getStartY(), false);
        activeMonsters = currentHall.getMonsters();

        if (!hasWizardsInCurrentHall()) {
            wizardTimer = 0;
        }
    }

    public static void updateRemainingTime() {
        frameCounter++;
        if (frameCounter >= 50) {
            frameCounter = 0;
            if (hero.getRemainingTime() > 0) {
                hero.decrementRemainingTime();
                System.out.println("Remaining Time: " + hero.getRemainingTime());
            } else {
                System.out.println("Time's up! Game Over.");
            }
        }
    }

    public static boolean hasWizardsInCurrentHall() {
        for (Monster monster : activeMonsters) {
            if (monster.getType() == Monster.MonsterType.WIZARD) {
                return true;
            }
        }
        return false;
    }

    public static void incrementWizardTimer() {
        wizardTimer++;
    }

    public static void resetWizardTimer() {
        wizardTimer = 0;
    }

    public static boolean isWizardTimerReady() {
        return wizardTimer >= 250;
    }

    public static void incrementMonsterSpawnTimer() {
        monsterSpawnTimer++;
    }

    public static boolean isMonsterSpawnTimerReady() {
        return monsterSpawnTimer >= 400;
    }

    public static void resetMonsterSpawnTimer() {
        monsterSpawnTimer = 0;
    }

    public static void incrementEnchantmentSpawnTimer() {
        enchantmentSpawnTimer++;
    }

    public static boolean isEnchantmentSpawnTimerReady() {
        return enchantmentSpawnTimer >= 600;
    }

    public static void resetEnchantmentSpawnTimer() {
        enchantmentSpawnTimer = 0;
    }

    public static boolean isGameOver() {
        return !hero.isAlive();
    }

    public static Hero getHero() {
        return hero;
    }

    public static HallGrid getCurrentHall() {
        return currentHall;
    }

    public static List<Monster> getActiveMonsters() {
        return activeMonsters;
    }

}