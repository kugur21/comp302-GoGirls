package org.RokueLike.domain.entity.item;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.hall.HallGrid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrowManager {
    private final List<Arrow> arrows = new ArrayList<>();
    private final HallGrid hallGrid;

    public ArrowManager(HallGrid hallGrid) {
        this.hallGrid = hallGrid;
    }

    public void addArrow(Arrow arrow) {
        arrows.add(arrow);
    }
    public void updateArrows() {
        Iterator<Arrow> iterator = arrows.iterator();
        while (iterator.hasNext()) {
            Arrow arrow = iterator.next();
            arrow.move();

            // Sınırlar içinde mi kontrol et
            if (!hallGrid.isSafeLocation(arrow.getX(), arrow.getY())) {
                arrow.deactivate();
            }

            if (!arrow.isActive()) {
                iterator.remove(); // Eğer ok aktif değilse listeden kaldır
            }
        }

    }

    public List<Arrow> getArrows() {
        return arrows;
    }
}
