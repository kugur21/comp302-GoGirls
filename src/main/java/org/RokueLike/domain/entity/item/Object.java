package org.RokueLike.domain.entity.item;

public class Object extends Item {

    private boolean isOpen;
    private boolean containsRune;
    private Rune containedRune;

    public Object() {
        super("object", ItemType.OBJECT);
        this.isOpen = false;
        this.containsRune = false;
        this.containedRune = null;
    }

    public Object(boolean containsRune, Rune containedRune) {
        super("object", ItemType.OBJECT);
        this.isOpen = false;
        this.containsRune = containsRune;
        this.containedRune = containedRune;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        this.isOpen = true;
    }

    public void close() {
        this.isOpen = false;
    }

    public boolean containsRune() {
        return containsRune;
    }

    public Rune getContainedRune() {
        return containedRune;
    }

    public void setContainedRune(Rune containedRune) {
        this.containsRune = true;
        this.containedRune = containedRune;
    }

    public void removeContainedRune() {
        this.containsRune = false;
        this.containedRune = null;
    }

}
