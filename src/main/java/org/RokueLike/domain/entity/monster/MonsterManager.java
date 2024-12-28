package org.RokueLike.domain.entity.monster;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.entity.hero.Hero;
import org.RokueLike.domain.entity.item.Arrow;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.utils.Direction;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.screen.GameOverScreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MonsterManager {

    private List<Monster> monsters;
    private HallGrid hallGrid;
    private Hero hero;
    private final List<Monster> luredFighters = new ArrayList<>();
    private List<Arrow> activeArrows = new ArrayList<>();
    private Direction lureDirection;

    //STRATEGY PATTERN INSTANCE - The MonsterManager defines behaviors for different monster types (e.g., processArcherBehavior, processFighterBehavior) encapsulated as strategiS
    public MonsterManager(List<Monster> monsters, HallGrid hallGrid, Hero hero) {
        this.monsters = monsters;
        this.hallGrid = hallGrid;
        this.hero = hero;
    }

    public void spawnMonster() {
        int[] location = hallGrid.findRandomSafeCell();
        if (location == null) {
            return;
        }
        Monster newMonster = generateRandomMonster(location[0], location[1]);
        hallGrid.addMonster(newMonster);
    }

    public void moveMonsters() {
        for (Monster monster : monsters) {
            switch (monster.getType()) {
                case ARCHER:
                    randomMoveAway(monster, monster.getAttackRange());
                case FIGHTER:
                    randomMove(monster);
            }
        }
    }

    public void heroMonsterInteraction() {
        for (Monster monster : monsters) {
            switch (monster.getType()) {
                case ARCHER:
                    processArcherBehavior(monster, monster.getAttackRange());
                    break;
                case FIGHTER:
                    processFighterBehavior(monster, monster.getAttackRange());
                    break;
            }
        }
    }

    public void processArcherBehavior(Monster archer, int attackRange) {
        if (hero.isImmune() || heroNotInRange(archer, attackRange) || GameManager.isCloakActive()) {
            return;
        }
        Direction direction = calculateArrowDirection(archer);
        if (direction != null) {
            Arrow arrow = new Arrow(archer, direction, attackRange);
            activeArrows.add(arrow);
        }
    }

    public void processFighterBehavior(Monster fighter, int attackRange) {
        if (hero.isImmune() || heroNotInRange(fighter, attackRange)) {
            return;
        }
        killHero();
    }

    public void processWizardBehavior() {
        for (Monster monster : monsters) {
            if (monster.getType() == Monster.MonsterType.WIZARD) {
                hallGrid.changeRuneLocation();
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
                monster.setPosition(nextX, nextY);
            } else {
                completedLures.add(monster);
            }
        }
        luredFighters.removeAll(completedLures);

        if (luredFighters.isEmpty()) {
            lureDirection = null;
        }
    }

    public void updateArcherArrows() {
        if (GameManager.isCloakActive()) {
            return;
        }

        Iterator<Arrow> iterator = activeArrows.iterator();
        while (iterator.hasNext()) {
            Arrow arrow = iterator.next();
            arrow.move();

            if (arrow.getX() == hero.getPositionX() && arrow.getY() == hero.getPositionY()) {
                killHero();
                arrow.deactivate();
            }
            if (arrow.notActive()) {
                iterator.remove();
            }
        }
    }

    private Monster generateRandomMonster(int x, int y) {
        Monster.MonsterType[] monsterTypes = Monster.MonsterType.values();
        Monster.MonsterType randomType = monsterTypes[new Random().nextInt(monsterTypes.length)];
        return new Monster(randomType, x, y);
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

    private void killHero() {
        hero.decrementLives();
        if (!hero.isAlive()) {
            String message;
            if (hero.getLives() > 0) {
                message = "You ran out of time. Game Over!";
            } else {
                message = "You ran out of lives. Game Over!";
            }
            GameManager.reset();
            Window.addScreen(new GameOverScreen(message), "GameOverScreen", true);
        } else {
            GameManager.handleHeroSpawn();
        }
    }

    private Direction calculateArrowDirection(Monster archer) {
        int dx = hero.getPositionX() - archer.getPositionX();
        int dy = hero.getPositionY() - archer.getPositionY();

        if (Math.abs(dx) > Math.abs(dy)) {
            return (dx > 0) ? Direction.RIGHT : Direction.LEFT;
        } else {
            return (dy > 0) ? Direction.DOWN : Direction.UP;
        }
    }

    private boolean heroNotInRange(Monster monster, int range) {
        int distanceX = Math.abs(monster.getPositionX() - hero.getPositionX());
        int distanceY = Math.abs(monster.getPositionY() - hero.getPositionY());
        return (distanceX + distanceY) > range;
    }

    public void randomMoveAway(Monster archer, int attackRange) {
        int dirX = 0;
        int dirY = 0;

        // Prioritize horizontal or vertical movement (no diagonal movement allowed)
        if (Math.abs(archer.getPositionX() - hero.getPositionX()) <= attackRange) {
            dirX = (archer.getPositionX() > hero.getPositionX()) ? 1 : -1; // Move left or right
        } else if (Math.abs(archer.getPositionY() - hero.getPositionY()) <= attackRange) {
            dirY = (archer.getPositionY() > hero.getPositionY()) ? 1 : -1; // Move up or down
        }

        // Attempt to move in the prioritized direction
        if (dirX != 0 && hallGrid.isSafeLocation(archer, dirX, 0)) {
            // Set facing direction based on horizontal movement
            if (dirX > 0) {
                archer.setFacing(Direction.RIGHT);
            } else {
                archer.setFacing(Direction.LEFT);
            }
            archer.setPosition(archer.getPositionX() + dirX, archer.getPositionY());
        } else if (dirY != 0 && hallGrid.isSafeLocation(archer, 0, dirY)) {
            archer.setPosition(archer.getPositionX(), archer.getPositionY() + dirY);
        } else {
            // If no safe prioritized direction, move randomly
            randomMove(archer);
        }
    }

    private void randomMove(Monster monster) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Random random = new Random();

        // Shuffle directions array to ensure randomness
        for (int i = directions.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = directions[i];
            directions[i] = directions[j];
            directions[j] = temp;
        }

        // Try each direction in random order
        for (int[] dir : directions) {
            int dirX = dir[0];
            int dirY = dir[1];

            if (hallGrid.isSafeLocation(monster, dirX, dirY)) {
                // Set the facing direction based on horizontal movement
                if (dirX > 0) {
                    monster.setFacing(Direction.RIGHT);
                } else if (dirX < 0) {
                    monster.setFacing(Direction.LEFT);
                }
                monster.setPosition(monster.getPositionX() + dirX, monster.getPositionY() + dirY);
                return; // Exit after moving once
            }
        }
    }

    public List<Arrow> getArrows() {
        return activeArrows;
    }

}