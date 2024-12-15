package org.RokueLike.domain.entity.monster;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.hall.HallGrid;

import java.util.List;
import java.util.Random;

public class MonsterManager {

    private List<Monster> monsters;
    private HallGrid hallGrid;
    private Hero hero;

    public MonsterManager(List<Monster> monsters, HallGrid hallGrid, Hero hero) {
        this.monsters = monsters;
        this.hallGrid = hallGrid;
        this.hero = hero;
    }

    public void moveMonsters() {
        for (Monster monster : monsters) {
            switch (monster.getType()) {
                case ARCHER:
                    processArcherBehavior(monster);
                    break;
                case FIGHTER:
                    processFighterBehavior(monster);
                    break;
            }
        }
    }

    public void processArcherBehavior(Monster archer) {
        int attackRange = 4;
        if (isHeroInRange(archer, attackRange)) {
            heroMonsterInteraction(archer);
            System.out.println("Archer attacks the hero!");

            return;
        }

        int dirX = 0;
        int dirY = 0;
        if (Math.abs(archer.getPositionX() - hero.getPositionX()) <= attackRange) {
            dirX = (archer.getPositionX() > hero.getPositionX()) ? 1 : -1;
        }
        if (Math.abs(archer.getPositionY() - hero.getPositionY()) <= attackRange) {
            dirY = (archer.getPositionY() > hero.getPositionY()) ? 1 : -1;
        }
        if (hallGrid.isSafeLocation(archer, dirX, dirY)) {
            archer.setPosition(archer.getPositionX() + dirX, archer.getPositionY() + dirY, true);
            System.out.println("Archer moves to (" + archer.getPositionX() + ", " + archer.getPositionY() + ")");
        } else {
            randomMove(archer);
        }
    }

    public void processFighterBehavior(Monster fighter) {
        int attackRange = 1;
        if (isHeroInRange(fighter, attackRange)) {
            heroMonsterInteraction(fighter);
            System.out.println("Fighter stabs the hero with a dagger!");
            return;
        }
        randomMove(fighter);
    }

    public void processWizardBehavior(Monster wizard) {
        try {
            hallGrid.changeRuneLocation();
            System.out.println("Wizard teleports the rune!");

        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void randomMove(Monster monster) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Random random = new Random();

        for (int i = 0; i < directions.length; i++) {
            int randomIndex = random.nextInt(directions.length);
            int dirX = directions[randomIndex][0];
            int dirY = directions[randomIndex][1];

            if (hallGrid.isSafeLocation(monster, dirX, dirY)) {
                monster.setPosition(monster.getPositionX() + dirX, monster.getPositionY() + dirY, true);
                System.out.println(monster.getName() + " randomly moves to (" + monster.getPositionX() + ", " + monster.getPositionY() + ")");
                return;
            }
        }

        System.out.println(monster.getName() + " cannot move.");
    }

    public boolean isHeroInRange(Monster monster, int range) {
        int distanceX = Math.abs(monster.getPositionX() - hero.getPositionX());
        int distanceY = Math.abs(monster.getPositionY() - hero.getPositionY());
        return (distanceX + distanceY) <= range;
    }

    public void heroMonsterInteraction(Monster monster) {
        if (monster.isAttacksHero()) {
            System.out.println(monster.getType().getName() + " attacks the hero!");
            hero.decrementLives();
            System.out.println("Hero's health: " + hero.getLives());
            if (!hero.isAlive()) {
                System.out.println("Game Over!");
                System.exit(0);
            }
            GameManager.handleHeroSpawn();
        } else {
            System.out.println(monster.getType().getName() + " doesn't attack the hero.");
        }
    }

    public void spawnMonster() {
        Monster newMonster = generateRandomMonster();
        int[] spawnPosition = hallGrid.findRandomSafeCell();

        if (spawnPosition != null) {
            newMonster.setPosition(spawnPosition[0], spawnPosition[1], false);
            //monsters.add(newMonster); // I think it's abundant, since we have hallGrid.addMonster
            hallGrid.addMonster(newMonster);
            System.out.println("Spawned a " + newMonster.getType().getName() +
                    " at (" + newMonster.getPositionX() + ", " + newMonster.getPositionY() + ")");
        } else {
            System.out.println("No valid spawn location found for a new monster.");
        }
    }

    private Monster generateRandomMonster() {
        Random random = new Random();
        Monster.MonsterType[] types = Monster.MonsterType.values();
        Monster.MonsterType randomType = types[random.nextInt(types.length)];
        return new Monster(randomType, 0, 0);
    }

    /**
     * Handles the interaction of a fighter monster with a thrown luring gem.
     * @param fighter The fighter monster to process.
     * @param gemX The x-coordinate of the luring gem.
     * @param gemY The y-coordinate of the luring gem.
     */
    public void processLuringGem(Monster fighter, int gemX, int gemY) {
        // Implementation will go here.
    }



    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

}
