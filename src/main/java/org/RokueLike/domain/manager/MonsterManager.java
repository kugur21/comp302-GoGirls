package org.RokueLike.domain.manager;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.model.entity.hero.Hero;
import org.RokueLike.domain.model.entity.monster.Arrow;
import org.RokueLike.domain.model.entity.monster.Monster;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.model.entity.monster.behaviour.wizard.*;
import org.RokueLike.utils.Direction;
import org.RokueLike.ui.Window;
import org.RokueLike.ui.screen.GameOverScreen;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static org.RokueLike.utils.Constants.*;

public class MonsterManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Serialization identifier

    private final List<Monster> monsters; // List of all monsters in the hall
    private final HallGrid hallGrid; // The current hall grid
    private final Hero hero; // The hero being interacted with
    private final List<Arrow> activeArrows; // List of active arrows shot by archers
    private final List<Monster> luredFighters; // List of lured fighter monsters
    private Direction lureDirection; // Direction of the lure for fighter monsters
    private final boolean spawn; // Whether to spawn monsters or not

    /// / STRATEGY PATTERN INSTANCE - The MonsterManager defines behaviors for different monster types (e.g., processArcherBehavior, processFighterBehavior) encapsulated as strategies

    public MonsterManager(List<Monster> monsters, HallGrid hallGrid, Hero hero, boolean spawn) {
        this.monsters = monsters;
        this.hallGrid = hallGrid;
        this.hero = hero;
        this.luredFighters = new ArrayList<>();
        this.activeArrows = new ArrayList<>();
        this.spawn = spawn;
    }

    // Spawns a random monster at a safe location in the hall.
    public void spawnMonster() {
        if (!spawn) {
            return;
        }
        int[] location = hallGrid.findRandomSafeCell();
        if (location == null) {
            return;
        }
        Monster newMonster = generateRandomMonster(location[0], location[1]);
        if (newMonster.getType() == Monster.MonsterType.WIZARD) {
            wizardBehaviour(newMonster);
        }
        hallGrid.addMonster(newMonster);
    }

    // Moves all monsters randomly or based on their behavior type.
    public void moveMonsters() {
        for (Monster monster : monsters) {
            switch (monster.getType()) {
                case ARCHER:
                    moveAwayFromHero(monster, monster.getAttackRange());
                    break;
                case FIGHTER:
                    moveRandom(monster);
                    break;
            }
        }
    }

    // Processes interactions between monsters and the hero.
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

    // Handles archer behavior by shooting arrows if the hero is in range.
    public void processArcherBehavior(Monster archer, int attackRange) {
        if (hero.isImmune() || heroNotInRange(archer, attackRange) || GameManager.isCloakActive()) {
            return;
        }
        Direction direction = calculateArrowDirection(archer);
        if (direction != null) {
            Arrow arrow = new Arrow(archer, direction, attackRange, hallGrid);
            activeArrows.add(arrow);
        }
    }

    // Handles fighter behavior by attacking the hero if in range.
    public void processFighterBehavior(Monster fighter, int attackRange) {
        if (hero.isImmune() || heroNotInRange(fighter, attackRange)) {
            return;
        }
        killHero();
    }

    // Handles wizard behavior by relocating the rune.
    public void processWizardBehavior() {
        List<Monster> toRemove = new ArrayList<>(); // Temporary list for deferred removal

        for (Monster monster : monsters) {
            if (monster.getType() == Monster.MonsterType.WIZARD) {
                monster.applyBehaviour();

                // If the behavior decides the monster should be removed, add it to the list
                if (monster.isMarkedForRemoval()) {
                    toRemove.add(monster);
                }
            }
        }
        // Safely remove all marked monsters after iteration
        monsters.removeAll(toRemove);
    }

    public void setWizardBehaviour() {
        for (Monster monster : monsters) {
            if (monster.getType() == Monster.MonsterType.WIZARD) {
                wizardBehaviour(monster);
            }
        }
    }

    // Processes the luring gem's effect by attracting nearby fighters in a direction, called when Luring Gem is activated.
    public void processLuringGem(Direction direction) {
        lureDirection = direction;
        luredFighters.clear();

        for (Monster monster : monsters) {
            if (monster.getType() == Monster.MonsterType.FIGHTER) {
                luredFighters.add(monster);
            }
        }
    }

    // Updates the position of lured fighter monsters, called in the GameLoop.
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

    // Updates the position and activity of archer arrows.
    public void updateArcherArrows() {
        if (GameManager.isCloakActive()) {
            return;
        }

        Iterator<Arrow> iterator = activeArrows.iterator();
        while (iterator.hasNext()) {
            Arrow arrow = iterator.next();
            arrow.move();

            if (arrow.isActive()) {
                if (arrow.getX() == hero.getPositionX() && arrow.getY() == hero.getPositionY()) {
                    killHero();
                    arrow.deactivate();
                }
            } else {
                iterator.remove();
            }
        }
    }

    // Generates a random monster at a specified position.
    private Monster generateRandomMonster(int x, int y) {
        Monster.MonsterType[] monsterTypes = Monster.MonsterType.values();
        Monster.MonsterType randomType = monsterTypes[new Random().nextInt(monsterTypes.length)];
        return new Monster(randomType, x, y);
        //return new Monster(Monster.MonsterType.WIZARD, x, y);
    }

    // Gets offsets for a specific direction.
    private static int[] getDirectionOffsets(Direction direction) {
        return switch (direction) {
            case LEFT -> new int[]{-1, 0};
            case RIGHT -> new int[]{1, 0};
            case UP -> new int[]{0, -1};
            case DOWN -> new int[]{0, 1};
            default -> throw new IllegalArgumentException("Unknown direction: " + direction);
        };
    }

    // Reduces the hero's lives and checks for game over conditions.
    private void killHero() {
        hero.decrementLives();
        if (!hero.isAlive()) {
            GameManager.reset(true);
            Window.addScreen(new GameOverScreen("Game Over! You died."), "GameOverScreen", true);
        } else {
            GameManager.handleHeroSpawn();
        }
    }

    // Calculates the direction of an arrow shot by an archer.
    private Direction calculateArrowDirection(Monster archer) {
        int dx = hero.getPositionX() - archer.getPositionX();
        int dy = hero.getPositionY() - archer.getPositionY();

        if (Math.abs(dx) > Math.abs(dy)) {
            return (dx > 0) ? Direction.RIGHT : Direction.LEFT;
        } else {
            return (dy > 0) ? Direction.DOWN : Direction.UP;
        }
    }

    // Checks if the hero is outside the range of a monster.
    private boolean heroNotInRange(Monster monster, int range) {
        int distanceX = monster.getPositionX() - hero.getPositionX();
        int distanceY = monster.getPositionY() - hero.getPositionY();

        // Check if the hero is in the same row (horizontal) or column (vertical) within the range
        if (distanceX == 0 && Math.abs(distanceY) <= range) {
            // Hero is exactly north or south
            return false;
        } else if (distanceY == 0 && Math.abs(distanceX) <= range) {
            // Hero is exactly east or west
            return false;
        }
        return true; // Hero is not in the specified range
    }

    // Moves a monster by keeping the distance with the hero, used by archer monster.
    private void moveAwayFromHero(Monster monster, int attackRange) {
        int dirX = 0;
        int dirY = 0;

        // Prioritize horizontal or vertical movement (no diagonal movement allowed)
        if (Math.abs(monster.getPositionX() - hero.getPositionX()) <= attackRange) {
            dirX = (monster.getPositionX() > hero.getPositionX()) ? 1 : -1; // Move left or right
        } else if (Math.abs(monster.getPositionY() - hero.getPositionY()) <= attackRange) {
            dirY = (monster.getPositionY() > hero.getPositionY()) ? 1 : -1; // Move up or down
        }

        // Attempt to move in the prioritized direction
        if (dirX != 0 && hallGrid.isSafeLocation(monster, dirX, 0)) {
            // Set facing direction based on horizontal movement
            if (dirX > 0) {
                monster.setFacing(Direction.RIGHT);
            } else {
                monster.setFacing(Direction.LEFT);
            }
            monster.setPosition(monster.getPositionX() + dirX, monster.getPositionY());
        } else if (dirY != 0 && hallGrid.isSafeLocation(monster, 0, dirY)) {
            monster.setPosition(monster.getPositionX(), monster.getPositionY() + dirY);
        } else {
            // If no safe prioritized direction, move randomly
            moveRandom(monster);
        }
    }

    // Moves a monster randomly to a safe location, used by fighter monster.
    private void moveRandom(Monster monster) {
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

    private void wizardBehaviour(Monster wizard) {
        double remainingTimePercentage = 100 * ((double) hero.getRemainingTime() / MAX_TIME);
        if (remainingTimePercentage > WIZARD_TELEPORT_PERCENTAGE) {
            wizard.setBehaviour(new Teleport(hallGrid));
        } else if (remainingTimePercentage > WIZARD_DISAPPEAR_PERCENTAGE) {
            wizard.setBehaviour(new Indecisive());
        } else {
            wizard.setBehaviour(new Closer());
        }
    }

    public void clearArrows() {
        activeArrows.clear();
    }

    public List<Arrow> getArrows() {
        return activeArrows;
    }
}