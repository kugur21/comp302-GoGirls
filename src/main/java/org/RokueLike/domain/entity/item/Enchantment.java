package org.RokueLike.domain.entity.item;

public class Enchantment extends Item {

    private int duration;
    private boolean isActive;

    public Enchantment(String name, int duration) {
        super(name, ItemType.ENCHANTMENT);
        this.duration = duration;
        this.isActive = false;
    }

    public Enchantment(String name) {
        super(name, ItemType.ENCHANTMENT);
        this.duration = 0;
        this.isActive = false;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
