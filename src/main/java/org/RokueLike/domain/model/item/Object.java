package org.RokueLike.domain.model.item;

import org.RokueLike.domain.hall.GridCell;

public class Object extends GridCell {

    private boolean containsRune; // Indicates if the object contains a rune

    public Object(String name, int x, int y) {
        super(name, x, y);
        this.containsRune = false;
    }

    public boolean containsRune() {
        return containsRune;
    }

    public void setContainedRune() {
        this.containsRune = true;
    }

    public void removeContainedRune() {
        this.containsRune = false;
    }

}