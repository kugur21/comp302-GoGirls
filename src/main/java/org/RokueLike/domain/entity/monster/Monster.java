package org.RokueLike.domain.entity.monster;

import org.RokueLike.domain.entity.EntityCell;

public class Monster extends EntityCell {

    private final Type type;
    private boolean attacksHero;
    private boolean mobile;

    public Monster(Type type, int x, int y) {
        super(type.getName() + "_monster", x, y);
        this.type = type;
        this.attacksHero = type.doesAttacksHero();
        this.mobile = type.isMobile();
    }

    public Type getType() {
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

    public enum Type {
        ARCHER("archer", true, true),
        FIGHTER("fighter", true, true),
        WIZARD("wizard", false, false);

        private final String name;
        private final boolean attacksHero;
        private final boolean mobile;

        Type(String name, boolean attacksHero, boolean mobile) {
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
