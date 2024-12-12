package org.RokueLike.domain.entity.item;

public class Rune extends Item {

    private boolean collected;

    public Rune() {
        super("rune", ItemType.RUNE);
        this.collected = false;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        this.collected = true;
    }

}
