package org.RokueLike.domain;

import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.hero.HeroManager;
import org.RokueLike.domain.entity.item.Enchantment;
import org.RokueLike.domain.entity.item.Enchantment.EnchantmentType;
import org.RokueLike.domain.entity.item.ItemManager;
import org.RokueLike.domain.entity.monster.Monster;
import org.RokueLike.domain.entity.monster.MonsterManager;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.hall.HallManager;
import org.RokueLike.domain.utils.Direction;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static Timer timer;
    private static int monsterSpawnTimer = 0;
    private static int enchantmentSpawnTimer = 0;
    private static int enchantmentDurationTimer = 0;
    private static int wizardTimer = 0;

    private static Builder builder;
    private static HallManager hallManager;
    private static HallGrid currentHall;
    private static Hero hero;
    private static HeroManager heroManager;
    private static List<Monster> activeMonsters;
    private static MonsterManager monsterManager;
    private static ItemManager itemManager;

    public static void startGame() {

        initBuildMode();
        initPlayMode();

        timer = new Timer(20, new GameLoop());
        timer.start();

        // Finished?

    }

    // For now, we will randomly place the objects in the grid.
    public static void initBuildMode() {
        builder = new Builder();
        List<HallGrid> halls = new ArrayList<>();

        int[] objectCount = {6, 9, 13, 17};
        String[] hallNames = {"Hall of Earth", "Hall of Air", "Hall of Water", "Hall of Fire"};

        for (int i = 0; i < objectCount.length; i++) {
            builder.resetGrid();
            builder.placeObject(objectCount[i]);
            builder.placeHero();

            String[] gridData = builder.getGridData();
            HallGrid hall = new HallGrid(gridData, hallNames[i]);
            halls.add(hall);

            System.out.println(hallNames[i] + " layout with " + objectCount[i] + " objects:");
            builder.printGrid();
            System.out.println();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Finished?
    }

    public static void handleMonsterSpawn() {
        try {
            monsterManager.spawnMonster();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Finished?
    }

    public static void handleEnchantmentSpawn() {
        try {
            itemManager.spawnEnchantment();
            enchantmentDurationTimer = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Finished?
    }

    public static void handleEnchantmentExpiration() {
        if (currentHall.getCurrentEnchantment() != null) {
            enchantmentDurationTimer++;
            if (enchantmentDurationTimer >= 300) {
                System.out.println("Enchantment expired: " + currentHall.getCurrentEnchantment().getEnchantmentType().getName());
                itemManager.disappearEnchantment();
                enchantmentDurationTimer = 0;
            }
        }
    }


    public static void handleMovement(int dirX, int dirY) {
        // TODO: Check conditions related to game ending state

        try {
            boolean moved = heroManager.moveHero(hallManager, dirX, dirY);
            if (!moved) {
                System.out.println("Hero cannot move in that direction.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        monsterManager.moveMonsters();
        //Finished?
    }

    public static void handleWizardBehavior() {
        for (Monster monster : activeMonsters) {
            if (monster.getType() == Monster.MonsterType.WIZARD) {
                monsterManager.processWizardBehavior(monster);
            }
        }
    }

    public static void handleEnchantmentUse(EnchantmentType enchantment) {
        handleEnchantmentUse(enchantment, null);
    }

    public static void handleEnchantmentUse(EnchantmentType enchantment, Direction direction) {
        try {
            itemManager.useEnchantment(enchantment, direction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleLureBehaviour() {
        monsterManager.updateLuredMonsters();
    }

    public static void handleLeftClick(int mouseX, int mouseY) {
        // TODO: Implement this method
        System.out.println("Mouse Clicked");
    }

    public static void updateCurrentHall(HallGrid nextHall) {
        currentHall = nextHall;
        hero.setPosition(currentHall.getStartX(), currentHall.getStartY(), false);
        activeMonsters = currentHall.getMonsters();

        if (!hasWizardsInCurrentHall()) {
            wizardTimer = 0;
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

    public static Hero getHero() {
        return hero;
    }

}
