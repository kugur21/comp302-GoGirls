package org.RokueLike.domain.entity.monster;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.item.Arrow;
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

        // Kahraman menzil içindeyse ok at



        if (isHeroInRange(archer, attackRange)) {
            Direction direction = calculateArrowDirection(archer, hero);
            if (direction != null) {
                Arrow arrow = new Arrow(archer.getPositionX(), archer.getPositionY(), direction, attackRange);
                GameManager.getArrowManager().addArrow(arrow);
                System.out.println("[Archer]: Arrow shot in direction: " + direction);
            }
        }



        // Hareket mantığı
        int dirX = 0;
        int dirY = 0;
        if (Math.abs(archer.getPositionX() - hero.getPositionX()) <= attackRange) {
            dirX = (archer.getPositionX() > hero.getPositionX()) ? 1 : -1;
        }
        if (Math.abs(archer.getPositionY() - hero.getPositionY()) <= attackRange) {
            dirY = (archer.getPositionY() > hero.getPositionY()) ? 1 : -1;
        }

        // Güvenli bir alana hareket ettir
        if (hallGrid.isSafeLocation(archer, dirX, dirY)) {
            archer.setPosition(archer.getPositionX() + dirX, archer.getPositionY() + dirY, true);
        } else {
            randomMove(archer);

        }
        GameManager.debugArrows();
    }
    private Direction calculateArrowDirection(Monster archer, Hero hero) {
        int dx = hero.getPositionX() - archer.getPositionX();
        int dy = hero.getPositionY() - archer.getPositionY();

        if (Math.abs(dx) > Math.abs(dy)) {
            return (dx > 0) ? Direction.RIGHT : Direction.LEFT;
        } else {
            return (dy > 0) ? Direction.DOWN : Direction.UP;
        }
    }




    public void processFighterBehavior(Monster fighter) {
        int attackRange = 1;
        if (isHeroInRange(fighter, attackRange)) {
            heroMonsterInteraction(fighter);
        }
        randomMove(fighter);
    }

    public void processWizardBehavior(Monster wizard) {
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
        if (monster.isAttacksHero()) {
            hero.decrementLives();
            if (hero.notAlive()) {
                System.exit(0);
            }
            GameManager.handleHeroSpawn();
        }
    }

    public void spawnMonster() {
        int[] location = hallGrid.findRandomSafeCell();
        if (location == null) {
            return;
        }
        Monster newMonster = generateRandomMonster(location[0], location[1]);
        hallGrid.addMonster(newMonster);
    }

    private Monster generateRandomMonster(int x, int y) {
        Monster.MonsterType[] monsterTypes = Monster.MonsterType.values();
        Monster.MonsterType randomType = monsterTypes[new Random().nextInt(monsterTypes.length)];
        return new Monster(randomType, x, y);
    }

    public void processCloakOfProtection(int duration) {
        for (Monster monster : monsters) {
            if (monster.getType() == Monster.MonsterType.ARCHER) {
                monster.setAttacksHero(false);
            }
        }

        Timer protectionTimer = new Timer(duration * 1000, e -> {
            for (Monster monster : monsters) {
                if (monster.getType() == Monster.MonsterType.ARCHER) {
                    monster.setAttacksHero(true);
                }
            }
        });
        protectionTimer.setRepeats(false);
        protectionTimer.start();
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