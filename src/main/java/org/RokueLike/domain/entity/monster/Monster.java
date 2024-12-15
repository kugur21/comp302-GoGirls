package org.RokueLike.domain.entity.monster;

import org.RokueLike.domain.entity.EntityCell;

public class Monster extends EntityCell {

    private final MonsterType type;
    private boolean attacksHero;
    private boolean mobile;

    public Monster(MonsterType type, int x, int y) {
        super(type.getName() + "_monster", x, y);
        this.type = type;
        this.attacksHero = type.doesAttacksHero();
        this.mobile = type.isMobile();
    }

    public MonsterType getType() {
        return type;
    }

    public boolean isAttacksHero() {
        return attacksHero;
    }

    public void setAttacksHero(boolean attacksHero) {
        this.attacksHero = attacksHero;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public enum MonsterType {
        ARCHER("archer", true, true),
        FIGHTER("fighter", true, true),
        WIZARD("wizard", false, false);

        private final String name;
        private final boolean attacksHero;
        private final boolean mobile;

        MonsterType(String name, boolean attacksHero, boolean mobile) {
            this.name = name;
            this.attacksHero = attacksHero;
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public boolean doesAttacksHero() {
            return attacksHero;
        }

        public boolean isMobile() {
            return mobile;
        }
    }
}
