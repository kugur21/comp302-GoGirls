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

    // Timer for controlling game loop
    private static Timer timer;
    private static boolean isPaused = false;

    // Core game components
    private static HallManager hallManager;
    private static HallGrid currentHall;
    private static Hero hero;
    private static HeroManager heroManager;
    private static List<Monster> activeMonsters;
    private static MonsterManager monsterManager;
    private static ItemManager itemManager;
    private static MessageBox messageBox;

    // State for enchantments
    private static boolean revealActive;
    private static boolean cloakActive;
    private static boolean lureActive;

    //// LOW COUPLING INSTANCE - The GameManager centralizes game logic and ensures the UI and domain layers remain loosely coupled by delegating tasks to managers and game entities.
    //// CONTROLLER PATTERN INSTANCE - The GameManager class acts as a controller for system operations such as handling enchantment usage, hero movement, and monster spawning

    // Initializes the game, setting up halls, play mode, and starting the game loop.
    public static void init() {
        System.out.println("[GameManager]: Starting game...");

        initHalls(); // Prepare halls for the game
        initPlayMode(); // Initialize core game entities

        timer = new Timer(GAME_DELAY, new GameLoop()); // Start main game loop
        timer.start();
    }

    // Sets up all halls and places the hero randomly in each hall.
    public static void initHalls() {
        String[][][] hallData = BuildManager.getAllHalls();
        for (String[][] hall : hallData) {
            BuildManager.placeHeroRandomly(hall); // Initialize hero spawn locations
        }

        // Create HallGrid instances for each hall
        List<HallGrid> halls = new ArrayList<>();
        String[] hallNames = {"Hall of Earth", "Hall of Air", "Hall of Water", "Hall of Fire"};
        for (int i = 0; i < hallData.length; i++) {
            HallGrid hall = new HallGrid(hallData[i], hallNames[i]);
            halls.add(hall);
        }
        hallManager = new HallManager(halls);
    }

    //// GameManager creates the hero, HeroManager, MonsterManager, and ItemManager during the game initialization, showcasing a Creator pattern.
    // Initializes gameplay mode with managers and hero.
    public static void initPlayMode() {
        currentHall = hallManager.getCurrentHall();
        hero = new Hero(currentHall.getStartX(), currentHall.getStartY());
        heroManager = new HeroManager(hero, currentHall);
        activeMonsters = currentHall.getMonsters();
        monsterManager = new MonsterManager(activeMonsters, currentHall, hero);
        itemManager = new ItemManager(currentHall, hero, monsterManager);
        messageBox = new MessageBox();
    }

    // Updates the game state when transitioning to a new hall.
    public static void updateCurrentHall(HallGrid nextHall) {
        currentHall = nextHall;
        hero.setPosition(currentHall.getStartX(), currentHall.getStartY());
        hero.resetRemainingTime();
        heroManager = new HeroManager(hero, currentHall);
        activeMonsters = currentHall.getMonsters();
        monsterManager = new MonsterManager(activeMonsters, currentHall, hero);
        itemManager = new ItemManager(currentHall, hero, monsterManager);

        TimeManager.hallReset(); // Reset timers
        messageBox.addMessage("Welcome to " + currentHall.getName() + "! Proceed with finding the rune!", 50);
    }

    // Handles hero respawn after death.
    public static void handleHeroSpawn() {
        try {
            heroManager.respawnHero();
            messageBox.addMessage(hero.getLives() + " lives left! You have 5 seconds immunity.", 35);
        } catch (Exception e) {
            System.out.println("[GameManager]: Hero Respawn Failed");
        }
    }

    // Spawns a monster in the current hall.
    public static void handleMonsterSpawn() {
        try {
            monsterManager.spawnMonster();
        } catch (Exception e) {
            System.out.println("[GameManager]: Monster Spawn Failed");
        }
    }

    // Spawns an enchantment in the current hall.
    public static void handleEnchantmentSpawn() {
        try {
            itemManager.spawnEnchantment();
        } catch (Exception e) {
            System.out.println("[GameManager]: Enchantment Spawn Failed");
        }
    }

    // Handles expiration of enchantments.
    public static void handleEnchantmentExpiration() {
        try {
            itemManager.disappearEnchantment();
        } catch (Exception e) {
            System.out.println("[GameManager]: Enchantment Disappear Failed");
        }
    }

    // Moves the hero based on input direction.
    public static void handleMovement(int dirX, int dirY) {
        try {
            heroManager.moveHero(hallManager, dirX, dirY);
        } catch (Exception e) {
            System.out.println("[GameManager]: Hero movement failed");
        }
    }

    // Updates monster movement logic.
    public static void handleMonsterMovement() {
        try {
            monsterManager.moveMonsters();
        } catch (Exception e) {
            System.out.println("[GameManager]: Monster movement failed");
        }
    }

    // Handles hero and monster interactions.
    public static void handleMonsterHit() {
        try {
            monsterManager.heroMonsterInteraction();
        } catch (Exception e) {
            System.out.println("[GameManager]: Monster hit failed");
        }
    }

    // Processes wizard-specific behavior.
    public static void handleWizardBehavior() {
        try {
            monsterManager.processWizardBehavior();
        } catch (Exception e) {
            System.out.println("[GameManager]: Wizard Behavior Failed");
        }
    }

    // Updates arrows shot by archer monsters.
    public static void handleArcherArrows() {
        try {
            monsterManager.updateArcherArrows();
        } catch (Exception e) {
            System.out.println("[GameManager]: Archer Arrows Update Failed");
        }
    }

    // Marks the fighter monsters that will be lured.
    public static void handleFighterLuring(Direction direction) {
        try {
            monsterManager.processLuringGem(direction);
        } catch (Exception e) {
            System.out.println("[GameManager]: Fighter Luring Failed");
        }
    }

    // Handles collecting enchantments.
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

    // Handles using enchantments, optionally with direction.
    public static void handleEnchantmentUse(EnchantmentType enchantment) {
        try {
            String response = itemManager.useEnchantment(enchantment, monsterManager);
            messageBox.addMessage(response, 30);
        } catch (Exception e) {
            System.out.println("[GameManager]: Enchantment use failed");
        }
    }

    // Activates lure behavior for marked fighter monsters.
    public static void handleLureBehaviour() {
        try {
            monsterManager.updateLuredMonsters();
        } catch (Exception e) {
            System.out.println("[GameManager]: Luring monsters failed");
        }
    }

    // Interacts with an object in the current hall.
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

    // Checks whether there is a wizard in the hall.
    public static boolean hasWizardsInCurrentHall() {
        for (Monster monster : activeMonsters) {
            if (monster.getType() == Monster.MonsterType.WIZARD) {
                return true;
            }
        }
        return false;
    }

    // Resets the game to its initial state.
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

            System.out.println("[GameManager]: Reset successful. Ready for a new game.");

        } catch (Exception e) {
            System.err.println("[GameManager]: Error during reset - " + e.getMessage());
        }
    }

    public static void togglePauseResume() {
        isPaused = !isPaused;
    }

    public static boolean isPaused() {
        return isPaused;
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
    }

    public static void setLureActive(boolean lureActive) {
        GameManager.lureActive = lureActive;
    }

    public static MonsterManager getMonsterManager() {
        return monsterManager;
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