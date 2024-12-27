package org.RokueLike.domain;

import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.hero.HeroManager;
import org.RokueLike.domain.entity.item.*;
import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;
import org.RokueLike.domain.entity.item.Object;
import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.domain.entity.monster.MonsterManager;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.hall.HallManager;
import org.RokueLike.utils.Direction;
import org.RokueLike.utils.MessageBox;


import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

import static org.RokueLike.utils.Constants.*;

public class GameManager {

    private static Timer timer;
    private static boolean isPaused = false;
    private static MessageBox messageBox;

    private static HallManager hallManager;
    private static HallGrid currentHall;
    private static Hero hero;
    private static HeroManager heroManager;
    private static List<Monster> activeMonsters;
    private static MonsterManager monsterManager;
    private static ItemManager itemManager;
    private static ArrowManager arrowManager;

    private static boolean revealActive;
    private static boolean cloakActive;
    private static boolean lureActive;

    //LOW COUPLING INSTANCE - The GameManager centralizes game logic and ensures the UI and domain layers remain loosely coupled by delegating tasks to managers and game entities.
    //CONTROLLER PATTERN INSTANCE - The GameManager class acts as a controller for system operations such as handling enchantment usage, hero movement, and monster spawning

    public static void init() {
        System.out.println("[GameManager]: Starting game...");

        initHalls();
        initPlayMode();

        timer = new Timer(GAME_DELAY, new GameLoop());
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

    //GameManager creates the hero, HeroManager, MonsterManager, and ItemManager during the game initialization, showcasing a Creator pattern.
    public static void initPlayMode() {
        currentHall = hallManager.getCurrentHall();
        hero = new Hero(currentHall.getStartX(), currentHall.getStartY());
        heroManager = new HeroManager(hero, currentHall);
        activeMonsters = currentHall.getMonsters();
        monsterManager = new MonsterManager(activeMonsters, currentHall, hero);
        itemManager = new ItemManager(currentHall, hero, monsterManager);
        arrowManager = new ArrowManager(currentHall);
        messageBox = new MessageBox();
    }

    public static void updateCurrentHall(HallGrid nextHall) {
        currentHall = nextHall;
        hero.setPosition(currentHall.getStartX(), currentHall.getStartY());
        hero.resetRemainingTime();
        heroManager = new HeroManager(hero, currentHall);
        activeMonsters = currentHall.getMonsters();
        monsterManager = new MonsterManager(activeMonsters, currentHall, hero);
        itemManager = new ItemManager(currentHall, hero, monsterManager);

        TimeManager.hallReset();
        messageBox.addMessage("Welcome to " + currentHall.getName() + "! Proceed with finding the rune!", 50);
    }

    public static void handleHeroSpawn() {
        try {
            heroManager.respawnHero();
            messageBox.addMessage(hero.getLives() + " lives left! You have 5 seconds immunity.", 35);
        } catch (Exception e) {
            System.out.println("[GameManager]: Hero Respawn Failed");
        }
    }

    public static void handleMonsterSpawn() {
        try {
            monsterManager.spawnMonster();
        } catch (Exception e) {
            System.out.println("[GameManager]: Monster Spawn Failed");
        }
    }

    public static void handleEnchantmentSpawn() {
        try {
            itemManager.spawnEnchantment();
        } catch (Exception e) {
            System.out.println("[GameManager]: Enchantment Spawn Failed");
        }
    }

    public static void handleEnchantmentExpiration() {
        try {
            itemManager.disappearEnchantment();
        } catch (Exception e) {
            System.out.println("[GameManager]: Enchantment Disappear Failed");
        }
    }

    public static void handleMovement(int dirX, int dirY) {
        try {
            if (hero.isAlive()) {
                heroManager.moveHero(hallManager, dirX, dirY);
            }
        } catch (Exception e) {
            System.out.println("[GameManager]: Hero movement failed");
        }
    }

    public static void handleMonsterMovement() {
        try {
            if (hero.isAlive()) {
                monsterManager.moveMonsters();
            }
        } catch (Exception e) {
            System.out.println("[GameManager]: Monster movement failed");
        }
    }

    public static void handleWizardBehavior() {
        for (Monster monster : activeMonsters) {
            if (monster.getType() == Monster.MonsterType.WIZARD) {
                monsterManager.processWizardBehavior();
            }
        }
    }

    public static void handleEnchantmentCollection(int mouseX, int mouseY) {
        try {
            GridCell clickedCell = currentHall.getCell(mouseX, mouseY);
            if (clickedCell instanceof Enchantment) {
                String response = itemManager.collectEnchantment();
                messageBox.addMessage(response, 18);
            }
        } catch (Exception e) {
            System.out.println("[GameManager]: Left click failed");
        }
    }

    public static void handleEnchantmentUse(EnchantmentType enchantment) {
        handleEnchantmentUse(enchantment, null);
    }

    public static void handleEnchantmentUse(EnchantmentType enchantment, Direction direction) {
        try {
            String response = itemManager.useEnchantment(enchantment, direction);
            messageBox.addMessage(response, 30);
        } catch (Exception e) {
            System.out.println("[GameManager]: Enchantment use failed");
        }
    }

    public static void handleLureBehaviour() {
        try {
            monsterManager.updateLuredMonsters();
        } catch (Exception e) {
            System.out.println("[GameManager]: Luring monsters failed");
        }
    }

    public static void handleObjectInteraction(int mouseX, int mouseY) {
        try {
            GridCell clickedCell = currentHall.getCell(mouseX, mouseY);
            if (clickedCell instanceof Object clickedObject) {
                if (heroManager.isAdjacentTo(mouseX, mouseY)) {
                    String response = itemManager.interactWithObject(clickedObject);
                    messageBox.addMessage(response, 25);
                } else {
                    messageBox.addMessage("Hero is not near the object. Move closer!", 5);
                }
            }
        } catch (Exception e) {
            System.out.println("[GameManager]: Right click failed");
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

    public static boolean isRevealActive() {
        return revealActive;
    }

    public static boolean isCloakActive() {
        return cloakActive;
    }

    public static boolean isLureActive() {
        return lureActive;
    }

    public static void setRevealActive(boolean revealActive) {
        GameManager.revealActive = revealActive;
    }

    public static void setCloakActive(boolean cloakActive) {
        GameManager.cloakActive = cloakActive;
        monsterManager.processCloakOfProtection(!cloakActive);
    }

    public static void setLureActive(boolean lureActive) {
        messageBox.addMessage("Activating Luring Gem! Decide the direction (A,W,S,D) to lure the Fighter Monsters.", 20);
        GameManager.lureActive = lureActive;
    }

    public static ArrowManager getArrowManager() {
        return arrowManager;
    }
    public static void debugArrows() {
        if (arrowManager == null) {
            System.out.println("[GameManager]: ArrowManager not initialized!");
            return;
        }

        for (Arrow arrow : arrowManager.getArrows()) {
            System.out.println("[Arrow]: Position X=" + arrow.getX() + " Y=" + arrow.getY());
        }
    }

    public static void togglePauseResume() {
        isPaused = !isPaused;
    }

    public static boolean isPaused() {
        return isPaused;
    }

    public static void reset() {
        try {
            // Stop and dispose of the timer safely
            if (timer != null) {
                timer.stop();
                timer = null;
            }
            TimeManager.gameReset();
            isPaused = false;
            revealActive = false;
            cloakActive = false;
            lureActive = false;

            // Log reset action for debugging
            System.out.println("[GameManager]: Reset successful. Ready for a new game.");

        } catch (Exception e) {
            // Handle any unexpected issues during reset
            System.err.println("[GameManager]: Error during reset - " + e.getMessage());
        }
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

    public static MessageBox getMessageBox() {
        return messageBox;
    }

}