package org.RokueLike.domain.entity.monster;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.utils.Direction;
import org.RokueLike.domain.utils.MessageBox;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterManager {

    private List<Monster> monsters;
    private HallGrid hallGrid;
    private Hero hero;
    private final List<Monster> luredFighters = new ArrayList<>();
    private Direction lureDirection;

    public MonsterManager(List<Monster> monsters, HallGrid hallGrid, Hero hero) {
        this.monsters = monsters;
        this.hallGrid = hallGrid;
        this.hero = hero;
    }

    public void moveMonsters(MessageBox messageBox) {
        for (Monster monster : monsters) {
            switch (monster.getType()) {
                case ARCHER:
                    messageBox.addMessage(processArcherBehavior(monster), 30);
                case FIGHTER:
                    messageBox.addMessage(processFighterBehavior(monster), 30);
            }
        }
    }

    public String processArcherBehavior(Monster archer) {
        int attackRange = 4;
        if (isHeroInRange(archer, attackRange)) {
            heroMonsterInteraction(archer);
            return "Archer attacks the hero!";
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
            return "Archer moves to (" + archer.getPositionX() + ", " + archer.getPositionY() + ")";
        } else {
            return randomMove(archer);
        }
    }

    public String processFighterBehavior(Monster fighter) {
        int attackRange = 1;
        if (isHeroInRange(fighter, attackRange)) {
            heroMonsterInteraction(fighter);
            return "Fighter stabs the hero with a dagger!";
        }
        return randomMove(fighter);
    }

    public String processWizardBehavior(Monster wizard) {
        hallGrid.changeRuneLocation();
        return "Wizard teleports the rune!";
    }

    private String randomMove(Monster monster) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Random random = new Random();

        for (int i = 0; i < directions.length; i++) {
            int randomIndex = random.nextInt(directions.length);
            int dirX = directions[randomIndex][0];
            int dirY = directions[randomIndex][1];

            if (hallGrid.isSafeLocation(monster, dirX, dirY)) {
                monster.setPosition(monster.getPositionX() + dirX, monster.getPositionY() + dirY, true);
                return monster.getName() + " randomly moves to (" + monster.getPositionX() + ", " + monster.getPositionY() + ")";
            }
        }
        return monster.getName() + " cannot move.";
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

    public String spawnMonster() {
        Monster newMonster = generateRandomMonster();
        int[] spawnPosition = hallGrid.findRandomSafeCell();

        if (spawnPosition != null) {
            newMonster.setPosition(spawnPosition[0], spawnPosition[1], false);
            //monsters.add(newMonster); // I think it's abundant, since we have hallGrid.addMonster
            hallGrid.addMonster(newMonster);
            return "Spawned a " + newMonster.getType().getName() +
                    " at (" + newMonster.getPositionX() + ", " + newMonster.getPositionY() + ")";
        } else {
            return "No valid spawn location found for a new monster.";
        }
    }

    private Monster generateRandomMonster() {
        Random random = new Random();
        Monster.MonsterType[] types = Monster.MonsterType.values();
        Monster.MonsterType randomType = types[random.nextInt(types.length)];
        return new Monster(randomType, 0, 0);
    }

    public void processCloakOfProtection(int duration) {
        for (Monster monster : monsters) {
            if (monster.getType() == Monster.MonsterType.ARCHER) {
                monster.setAttacksHero(false);
            }
        }
        System.out.println("Cloak of Protection activated. Archers cannot attack the hero for " + duration + " seconds.");

        Timer protectionTimer = new Timer(duration * 1000, e -> {
            for (Monster monster : monsters) {
                if (monster.getType() == Monster.MonsterType.ARCHER) {
                    monster.setAttacksHero(true);
                }
            }
            System.out.println("Cloak of Protection expired. Archers can attack the hero again.");
        });
        protectionTimer.setRepeats(false);
        protectionTimer.start();
    }

    public void processLuringGem(Direction direction) {
        lureDirection = direction;
        luredFighters.clear();

        for (Monster monster : monsters) {
            if (monster.getType() == Monster.MonsterType.FIGHTER) {
                System.out.println("Fighter at (" + monster.getPositionX() + ", " + monster.getPositionY() + ") is lured.");
                luredFighters.add(monster);
            }
        }

        if (!luredFighters.isEmpty()) {
            System.out.println("Luring Gem effect started for " + luredFighters.size() + " fighters.");
        } else {
            System.out.println("No fighters to lure.");
        }
    }

    public void updateLuredMonsters() {
        if (luredFighters.isEmpty() || lureDirection == null) {
            return;
        }

        List<Monster> completedLures = new ArrayList<>();
        int[] offsets = getDirectionOffsets(lureDirection);
        int deltaX = offsets[0];
        int deltaY = offsets[1];

        for (Monster monster : luredFighters) {
            int nextX = monster.getPositionX() + deltaX;
            int nextY = monster.getPositionY() + deltaY;

            if (hallGrid.isSafeLocation(nextX, nextY)) {
                monster.setPosition(nextX, nextY, true);
                System.out.println("Fighter moves to (" + monster.getPositionX() + ", " + monster.getPositionY() + ").");
            } else {
                System.out.println("Fighter at (" + monster.getPositionX() + ", " + monster.getPositionY() + ") stops.");
                completedLures.add(monster);
            }
        }
        luredFighters.removeAll(completedLures);

        if (luredFighters.isEmpty()) {
            System.out.println("Luring Gem effect ended.");
            lureDirection = null;
        }
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public void setMonsters(List<Monster> monsters) {
        this.monsters = monsters;
    }

    public static int[] getDirectionOffsets(Direction direction) {
        return switch (direction) {
            case LEFT -> new int[]{-1, 0};
            case RIGHT -> new int[]{1, 0};
            case UP -> new int[]{0, -1};
            case DOWN -> new int[]{0, 1};
            default -> throw new IllegalArgumentException("Unknown direction: " + direction);
        };
    }
}
