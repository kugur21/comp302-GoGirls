package org.RokueLike.domain;

import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.hero.HeroManager;
import org.RokueLike.domain.entity.item.Arrow;
import org.RokueLike.domain.entity.item.ArrowManager;
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
import org.RokueLike.ui.Window;
import org.RokueLike.ui.screen.GameOverScreen;


import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static final int GAME_DELAY = 100;
    private static final int MONSTER_SPAWN = 8000;
    private static final int ENCHANTMENT_SPAWN = 12000;
    private static final int ENCHANTMENT_DURATION = 6000;
    private static final int MONSTER_MOVEMENT_DELAY = 800;
    private static final int WIZARD_BEHAVIOR = 5000;
    private static final int REVEAL_ENCHANTMENT_DURATION = 10000;
    public static final int CLOAK_ENCHANTMENT_DURATION = 20000;


    private static int monsterSpawnTimer = 0;
    private static int enchantmentSpawnTimer = 0;
    private static int enchantmentDurationTimer = 0;
    private static int wizardTimer = 0;
    private static int monsterMovementTimer = 0;
    private static int revealTimer = 0;
    private static int cloakTimer = 0;
    private static int frameCounter = 0;

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
        messageBox = new MessageBox();
        arrowManager = new ArrowManager(currentHall); // ArrowManager burada başlatılıyor.
    }

    public static void genericLoop() {
        hero.decreaseMotionOffset();
        for (Monster monster: activeMonsters) {
            monster.decreaseMotionOffset();
        }
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
            enchantmentDurationTimer = 0;
        } catch (Exception e) {
            System.out.println("[GameManager]: Enchantment Spawn Failed");
        }
    }

    public static void handleEnchantmentExpiration() {
        if (currentHall.getCurrentEnchantment() != null) {
            enchantmentDurationTimer++;
            if (enchantmentDurationTimer >= (ENCHANTMENT_DURATION / GAME_DELAY)) {
                itemManager.disappearEnchantment();
                enchantmentDurationTimer = 0;
            }
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

    public static void handleLeftClick(int mouseX, int mouseY) {
        try {
            if (currentHall.getCurrentEnchantment() != null
                    && currentHall.getCurrentEnchantment().getPositionX() == mouseX
                    && currentHall.getCurrentEnchantment().getPositionY() == mouseY) {

                String response = itemManager.collectEnchantment();
                messageBox.addMessage(response, 18);
            }
        } catch (Exception e) {
            System.out.println("[GameManager]: Left click failed");
        }
    }

    public static void handleRightClick(int mouseX, int mouseY) {
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

    public static void updateCurrentHall(HallGrid nextHall) {
        currentHall = nextHall;
        hero.setPosition(currentHall.getStartX(), currentHall.getStartY(), false);
        heroManager = new HeroManager(hero, currentHall);
        activeMonsters = currentHall.getMonsters();
        monsterManager = new MonsterManager(activeMonsters, currentHall, hero);
        itemManager = new ItemManager(currentHall, hero, monsterManager);

        if (!hasWizardsInCurrentHall()) {
            wizardTimer = 0;
        }
        monsterSpawnTimer = 0;
        enchantmentSpawnTimer = 0;
        enchantmentDurationTimer = 0;

        messageBox.addMessage("Welcome to " + currentHall.getName() + "! Proceed with finding the rune!", 50);
    }

    public static void updateRemainingTime() {
        frameCounter++;
        if (frameCounter >= (1000 / GAME_DELAY)) {
            frameCounter = 0;
            if (hero.getRemainingTime() > 0) {
                hero.decrementRemainingTime();
            } else {
                String message = "Game Over! Time is Over";
                GameManager.reset();
                Window.addScreen(new GameOverScreen(message), "GameOverScreen", true);
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

    public static void incrementMonsterSpawnTimer() {
        monsterSpawnTimer++;
    }

    public static boolean isMonsterSpawnTimerReady() {
        return monsterSpawnTimer >= (MONSTER_SPAWN / GAME_DELAY);
    }

    public static void resetMonsterSpawnTimer() {
        monsterSpawnTimer = 0;
    }

    public static void incrementMonsterMovementTimer() {
        monsterMovementTimer++;
    }

    public static boolean isMonsterMovementReady() {
        return monsterMovementTimer >= (MONSTER_MOVEMENT_DELAY / GAME_DELAY);
    }

    public static void resetMonsterMovementTimer() {
        monsterMovementTimer = 0;
    }

    public static void incrementWizardTimer() {
        wizardTimer++;
    }

    public static void resetWizardTimer() {
        wizardTimer = 0;
    }

    public static boolean isWizardTimerReady() {
        return wizardTimer >= (WIZARD_BEHAVIOR / GAME_DELAY);
    }


    public static void incrementEnchantmentSpawnTimer() {
        enchantmentSpawnTimer++;
    }

    public static boolean isEnchantmentSpawnTimerReady() {
        return enchantmentSpawnTimer >= (ENCHANTMENT_SPAWN / GAME_DELAY);
    }

    public static void resetEnchantmentSpawnTimer() {
        enchantmentSpawnTimer = 0;
    }

    public static void incrementRevealTimer() {
        revealTimer++;
    }

    public static boolean isRevealTimerReady() {
        return revealTimer >= (REVEAL_ENCHANTMENT_DURATION / GAME_DELAY);
    }

    public static void resetRevealTimer() {
        revealTimer = 0;
    }

    public static int remainingRevealTimer() {
        if (!isRevealActive()) {
            return 0;
        }

        int elapsedTime = revealTimer * GAME_DELAY;
        int remainingTime = (REVEAL_ENCHANTMENT_DURATION - elapsedTime) / 1000;
        return Math.max(remainingTime, 0);
    }

    public static void incrementCloakTimer() {
        cloakTimer++;
    }

    public static boolean isCloakTimerReady() {
        return  cloakTimer >= (CLOAK_ENCHANTMENT_DURATION / GAME_DELAY);
    }

    public static void resetCloakTimer() {
        cloakTimer = 0;
    }

    public static int remainingCloakTimer() {
        if (!isCloakActive()) {
            return 0;
        }

        int elapsedTime = cloakTimer * GAME_DELAY;
        int remainingTime = (CLOAK_ENCHANTMENT_DURATION - elapsedTime) / 1000;
        return Math.max(remainingTime, 0);
    }

    public static boolean isRevealActive() {
        return revealActive;
    }

    public static void setRevealActive(boolean revealActive) {
        GameManager.revealActive = revealActive;
    }

    public static boolean isCloakActive() {
        return cloakActive;
    }

    public static void setCloakActive(boolean cloakActive) {
        GameManager.cloakActive = cloakActive;
        monsterManager.processCloakOfProtection(!cloakActive);
    }

    public static boolean isLureActive() {
        return lureActive;
    }

    public static void setLureActive(boolean lureActive) {
        messageBox.addMessage("Activating Luring Gem! Decide the direction (A,W,S,D) to lure the Fighter Monsters.", 10);
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
        }}


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

            // Reset all static variables to their default states
            monsterSpawnTimer = 0;
            enchantmentSpawnTimer = 0;
            enchantmentDurationTimer = 0;
            wizardTimer = 0;
            monsterMovementTimer = 0;
            revealTimer = 0;
            cloakTimer = 0;
            frameCounter = 0;

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
}