package org.RokueLike.domain.entity.monster;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.item.Enchantment;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.utils.Direction;
import org.RokueLike.domain.utils.MessageBox;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.screen.GameOverScreen;

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

    //STRATEGY PATTERN INSTANCE - The MonsterManager defines behaviors for different monster types (e.g., processArcherBehavior, processFighterBehavior) encapsulated as strategiS
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
                case FIGHTER:
                    processFighterBehavior(monster);
            }
        }
    }

    public void processArcherBehavior(Monster archer) {
        int attackRange = 4;
        if (isHeroInRange(archer, attackRange)) {
            heroMonsterInteraction(archer);
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
        } else {
            randomMove(archer);
        }
    }

    public void processFighterBehavior(Monster fighter) {
        int attackRange = 1;
        if (isHeroInRange(fighter, attackRange)) {
            heroMonsterInteraction(fighter);
        }
        randomMove(fighter);
    }

    public void processWizardBehavior() {
        hallGrid.changeRuneLocation();
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
            }
        }
    }

    public boolean isHeroInRange(Monster monster, int range) {
        int distanceX = Math.abs(monster.getPositionX() - hero.getPositionX());
        int distanceY = Math.abs(monster.getPositionY() - hero.getPositionY());
        return (distanceX + distanceY) <= range;
    }

    public void heroMonsterInteraction(Monster monster) {
        if (!hero.isImmune()) {
            if (monster.isAttacksHero()) {
                System.out.println(monster.getName() + " attacks hero");
                hero.decrementLives();
                if (!hero.isAlive()) {
                    String message;
                    if (hero.getLives() > 0) {
                        message = "You ran out of time. Game Over!";
                    } else {
                        message = "You ran out of lives. Game Over!";
                    }
                    Window.addScreen(new GameOverScreen(message), "GameOverScreen", true);
                }
                GameManager.handleHeroSpawn();
            }
        }
    }

    public void spawnMonster() {
        int[] location = hallGrid.findRandomSafeCell();
        if (location == null) {
            return;
        }
        Monster newMonster = generateRandomMonster(location[0], location[1]);
        if (GameManager.isCloakActive()) {
            if (newMonster.getType() == Monster.MonsterType.ARCHER) {
                newMonster.setAttacksHero(false);
            }
        }
        hallGrid.addMonster(newMonster);
    }

    private Monster generateRandomMonster(int x, int y) {
        Monster.MonsterType[] monsterTypes = Monster.MonsterType.values();
        Monster.MonsterType randomType = monsterTypes[new Random().nextInt(monsterTypes.length)];
        return new Monster(randomType, x, y);
    }

    public void processCloakOfProtection(boolean attacksHero) {
        for (Monster monster : monsters) {
            if (monster.getType() == Monster.MonsterType.ARCHER) {
                monster.setAttacksHero(attacksHero);
            }
        }
    }

    public void processLuringGem(Direction direction) {
        lureDirection = direction;
        luredFighters.clear();

        for (Monster monster : monsters) {
            if (monster.getType() == Monster.MonsterType.FIGHTER) {
                luredFighters.add(monster);
            }
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
            } else {
                completedLures.add(monster);
            }
        }
        luredFighters.removeAll(completedLures);

        if (luredFighters.isEmpty()) {
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