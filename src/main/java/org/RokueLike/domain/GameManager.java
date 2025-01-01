package org.RokueLike.domain;

import org.RokueLike.domain.loop.GameLoop;
import org.RokueLike.domain.model.entity.hero.Hero;
import org.RokueLike.domain.manager.HeroManager;
import org.RokueLike.domain.model.entity.monster.behaviour.wizard.Indecisive;
import org.RokueLike.domain.model.item.*;
import org.RokueLike.domain.model.item.Enchantment.EnchantmentType;
import org.RokueLike.domain.model.item.Object;
import org.RokueLike.domain.model.entity.monster.Monster;
import org.RokueLike.domain.manager.MonsterManager;
import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.manager.HallManager;
import org.RokueLike.domain.manager.BuildManager;
import org.RokueLike.domain.manager.ItemManager;
import org.RokueLike.domain.manager.TimeManager;
import org.RokueLike.utils.Direction;
import org.RokueLike.utils.MessageBox;


import javax.swing.Timer;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.RokueLike.utils.Constants.*;

public class GameManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier

    // Timer for controlling game loop
    private static Timer timer;
    private static boolean isPaused = false;

    // Core game components
    private static HallManager hallManager;
    private static HallGrid currentHall;
    private static HallGrid closureHall;
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

    private static boolean wizardClosureActive;
    private static double lastWizardThreshold = -1; // Tracks the last percentage category

    private static String currentSaveFileName; // Name of the currently saved file

    //// Singleton Pattern
    private static GameManager instance;

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public static void restoreInstance(GameManager loadedState) {
        instance = loadedState;
    }

    //// LOW COUPLING INSTANCE - The GameManager centralizes game logic and ensures the UI and domain layers remain loosely coupled by delegating tasks to managers and game entities.
    //// CONTROLLER PATTERN INSTANCE - The GameManager class acts as a controller for system operations such as handling enchantment usage, hero movement, and monster spawning

    // Initializes the game, setting up halls, play mode, and starting the game loop.
    public static void init(String fileName) {
        System.out.println("[GameManager]: Starting game...");
        currentSaveFileName = fileName;
        if (!currentSaveFileName.endsWith(".dat")) {
            initHalls(); // Prepare halls for the game
            initPlayMode(); // Initialize core game entities
        }
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

        String[][] closureHallData = BuildManager.getHall("closure");
        if (closureHallData != null) {
            closureHall = new HallGrid(closureHallData, "Closure Hall");
            Monster wizard = new Monster(Monster.MonsterType.WIZARD, 5, 5);
            wizard.setBehaviour(new Indecisive());
            closureHall.setMonsters(new ArrayList<>(List.of(wizard)));
        } else {
            throw new RuntimeException("Hall closure not found");
        }
    }

    //// GameManager creates the hero, HeroManager, MonsterManager, and ItemManager during the game initialization, showcasing a Creator pattern.
    // Initializes gameplay mode with managers and hero.
    public static void initPlayMode() {
        currentHall = hallManager.getCurrentHall();
        hero = new Hero(currentHall.getStartX(), currentHall.getStartY());
        heroManager = new HeroManager(hero, currentHall);
        activeMonsters = currentHall.getMonsters();
        monsterManager = new MonsterManager(activeMonsters, currentHall, hero, true);
        itemManager = new ItemManager(currentHall, hero, monsterManager, true);
        wizardClosureActive = false;

        messageBox = new MessageBox();
        messageBox.addMessage("Welcome to Hall of Earth! Find the rune to unlock the door.", 3);
    }

    // Updates the game state when transitioning to a new hall.
    public static void updateCurrentHall(HallGrid nextHall) {
        currentHall = nextHall;
        hero.setPosition(currentHall.getStartX(), currentHall.getStartY());
        hero.resetRemainingTime();
        heroManager = new HeroManager(hero, currentHall);
        activeMonsters = currentHall.getMonsters();
        monsterManager = new MonsterManager(activeMonsters, currentHall, hero, true);
        itemManager = new ItemManager(currentHall, hero, monsterManager, true);
        wizardClosureActive = false;

        TimeManager.hallReset(); // Reset timers
        GameManager.setLureActive(false);
        GameManager.setRevealActive(false);
        GameManager.setCloakActive(false);
        messageBox.addMessage("Welcome to " + currentHall.getName() + "! Find the rune to unlock the door.", 3);
    }

    // Updates the game state when transitioning to a new hall.
    public static void updateClosureHall() {
        currentHall = closureHall;
        hero.setPosition(7, 7);
        heroManager = new HeroManager(hero, currentHall);
        activeMonsters = currentHall.getMonsters();
        monsterManager = new MonsterManager(activeMonsters, currentHall, hero, false);
        itemManager = new ItemManager(currentHall, hero, monsterManager, false);
        GameManager.setLureActive(false);
        GameManager.setRevealActive(false);
        GameManager.setCloakActive(false);
        messageBox.addMessage("Wizard has trapped you. No way out.", 3);
    }

    // Handles hero respawn after death.
    public static void handleHeroSpawn() {
        try {
            heroManager.respawnHero();
            messageBox.addMessage(hero.getLives() + " lives left! You have " + (IMMUNE_TIME / 1000) + " seconds immunity.", 4);
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

    public static void updateWizardBehaviour() {
        try {
            double remainingTimePercentage = 100 * ((double) hero.getRemainingTime() / MAX_TIME);
            double newThreshold = (remainingTimePercentage < WIZARD_DISAPPEAR_PERCENTAGE) ? 0 :
                    (remainingTimePercentage > WIZARD_TELEPORT_PERCENTAGE) ? WIZARD_TELEPORT_PERCENTAGE : WIZARD_DISAPPEAR_PERCENTAGE;

            // Check if we have moved to a new percentage category
            if (newThreshold != lastWizardThreshold) {
                lastWizardThreshold = newThreshold; // Update the threshold tracker

                // Call monsterManager to set the new behavior
                monsterManager.setWizardBehaviour();
            }
        } catch (Exception e) {
            System.out.println("[GameManager]: Wizard behaviour update failed");
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
                messageBox.addMessage(response, 2);
            }
        } catch (Exception e) {
            System.out.println("[GameManager]: Left click failed");
        }
    }

    // Handles using enchantments, optionally with direction.
    public static void handleEnchantmentUse(EnchantmentType enchantment) {
        try {
            String response = itemManager.useEnchantment(enchantment, monsterManager);
            messageBox.addMessage(response, 4);
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
                    messageBox.addMessage(response, 3);
                } else {
                    messageBox.addMessage("Hero is not near the object. Move closer!", 2);
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
    public static void reset(boolean deleteFile) {
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
            wizardClosureActive = false;

            // Delete the saved file
            if (deleteFile) {
                String pathName = "src/main/resources/saves/";
                if (currentSaveFileName.endsWith(".dat")) {
                    pathName += currentSaveFileName;
                } else {
                    pathName += currentSaveFileName + ".dat";
                }
                File saveFile = new File(pathName);
                if (saveFile.exists() && saveFile.delete()) {
                    System.out.println("[GameManager]: Save file " + currentSaveFileName + " deleted.");
                } else {
                    System.out.println("[GameManager]: Failed to delete save file " + currentSaveFileName);
                }
            }

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

    public static void setWizardClosureActive(boolean wizardClosureActive) {
        GameManager.wizardClosureActive = wizardClosureActive;
        GameManager.updateClosureHall();
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

    public static MessageBox getMessageBox() {
        return messageBox;
    }

    // Method to save the game state to a file
    public static void saveGame(String fileName) {
        try {
            // Ensure the directory structure exists
            File file = new File(fileName);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // Create the directory if it doesn't exist
            }
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
                out.writeObject(getInstance()); // Serialize the GameManager class
                System.out.println("[GameManager]: Game saved successfully to " + fileName);
            }
        } catch (IOException e) {
            System.err.println("[GameManager]: Error saving game - " + e.getMessage());
        }
    }

    // Method to load the game state from a file
    public static void loadGame(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            GameManager loadedState = (GameManager) in.readObject();
            restoreInstance(loadedState);
            System.out.println("[GameManager]: Game loaded successfully from " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[GameManager]: Error loading game - " + e.getMessage());
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Serialize non-static fields
        // Serialize static fields manually
        out.writeObject(hallManager);
        out.writeObject(currentHall);
        out.writeObject(closureHall);
        out.writeObject(hero);
        out.writeObject(heroManager);
        out.writeObject(activeMonsters);
        out.writeObject(monsterManager);
        out.writeObject(itemManager);
        out.writeObject(messageBox);
        out.writeBoolean(revealActive);
        out.writeBoolean(cloakActive);
        out.writeBoolean(lureActive);
        out.writeBoolean(wizardClosureActive);
        out.writeDouble(lastWizardThreshold);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Deserialize non-static fields
        // Deserialize static fields manually
        hallManager = (HallManager) in.readObject();
        currentHall = (HallGrid) in.readObject();
        closureHall = (HallGrid) in.readObject();
        hero = (Hero) in.readObject();
        heroManager = (HeroManager) in.readObject();
        activeMonsters = (List<Monster>) in.readObject();
        monsterManager = (MonsterManager) in.readObject();
        itemManager = (ItemManager) in.readObject();
        messageBox = (MessageBox) in.readObject();
        revealActive = in.readBoolean();
        cloakActive = in.readBoolean();
        lureActive = in.readBoolean();
        wizardClosureActive = in.readBoolean();
        lastWizardThreshold = in.readDouble();
    }

}