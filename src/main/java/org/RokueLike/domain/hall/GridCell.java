package org.RokueLike.domain.hall;

public class GridCell {

    private String name;
    private boolean isCollectible;
    private boolean canWalk;
    private boolean isMovable;

    protected int positionX;
    protected int positionY;

    public GridCell(String name, int positionX, int positionY) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;

        if (name.startsWith("enchantment")) {
            this.isCollectible = true;
            this.canWalk = false;
            this.isMovable = false;
        } else if (name.equals("hero")) {
            this.isCollectible = false;
            this.canWalk = true;
            this.isMovable = true;
        } else if (name.startsWith("monster")) {
            this.isCollectible = false;
            this.canWalk = false;
            this.isMovable = false;
            if (name.equals("monster_fighter")) {
                this.canWalk = true;
            }
        } else {
            this.isCollectible = false;
            this.isMovable = false;
            this.canWalk = false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCollectible() {
        return isCollectible;
    }

    public void setCollectible(boolean collectible) {
        isCollectible = collectible;
    }

    public boolean isCanWalk() {
        return canWalk;
    }

    public void setCanWalk(boolean canWalk) {
        this.canWalk = canWalk;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public void setMovable(boolean movable) {
        isMovable = movable;
    }
}
