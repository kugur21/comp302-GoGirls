package org.RokueLike.domain.model.entity.monster;

import org.RokueLike.domain.model.entity.EntityCell;

public class Monster extends EntityCell {

    private final MonsterType type; // The type of the monster (e.g., Archer, Fighter, Wizard)
    private final int attackRange; // The range at which the monster can attack

    //// MODEL-VIEW SEPARATION PRINCIPLE - Domain classes (Hero, Monster, BuildManager, etc.) handle the core game logic and data manipulation independently of the UI

    public Monster(MonsterType type, int x, int y) {
        super(type.getName() + "_monster", x, y);
        this.type = type;
        this.attackRange = type.attackRange();
    }

    public MonsterType getType() {
        return type;
    }

    public int getAttackRange() {return attackRange;}

    // Enum representing the different types of monsters.
    public enum MonsterType {
        ARCHER("archer", 4),
        FIGHTER("fighter", 1),
        WIZARD("wizard", 0);

        private final String name;
        private final int attackRange;

        // Constructs a MonsterType with a name and attack range.
        MonsterType(String name, int attackRange) {
            this.name = name;
            this.attackRange = attackRange;
        }

        public String getName() {
            return name;
        }

        public int attackRange() {return attackRange;}

    }

}