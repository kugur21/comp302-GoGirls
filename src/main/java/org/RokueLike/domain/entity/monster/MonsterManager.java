package org.RokueLike.domain.entity.monster;

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
            // TODO: Add logic to reduce hero health or trigger other effects
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
        if (isPassable(archer, dirX, dirY)) {
            archer.setPosition(archer.getPositionX() + dirX, archer.getPositionY() + dirY, true);
            System.out.println("Archer moves to (" + archer.getPositionX() + ", " + archer.getPositionY() + ")");
        } else {
            randomMove(archer);
        }
    }

    public void processFighterBehavior(Monster fighter) {
        int attackRange = 1;
        if (isHeroInRange(fighter, attackRange)) {
            // TODO: Add logic to reduce hero health or trigger other effects
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


    private boolean isPassable(Monster monster, int x, int y) {
        return hallGrid.getCellInFront(monster, x, y).getName().equals("floor") && !hallGrid.isThereMonster(monster.getPositionX() + x, monster.getPositionY() + y);
    }

    private void randomMove(Monster monster) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Random random = new Random();

        for (int i = 0; i < directions.length; i++) {
            int randomIndex = random.nextInt(directions.length);
            int dirX = directions[randomIndex][0];
            int dirY = directions[randomIndex][1];

            if (isPassable(monster, dirX, dirY)) {
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





    /**
     * Handles the interaction of a fighter monster with a thrown luring gem.
     * @param fighter The fighter monster to process.
     * @param gemX The x-coordinate of the luring gem.
     * @param gemY The y-coordinate of the luring gem.
     */
    public void processLuringGem(Monster fighter, int gemX, int gemY) {
        // Implementation will go here.
    }

    /**
     * Spawns monsters in the specified hall grid at random locations.
     * @param hallGrid The hall grid where monsters will spawn.
     * @param spawnCount The number of monsters to spawn.
     */
    public void spawnMonsters(HallGrid hallGrid, int spawnCount) {
        // Implementation will go here.
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

}
