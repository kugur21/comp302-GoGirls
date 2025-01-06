package org.RokueLike.domain.model.entity.monster.behaviour.wizard;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.model.entity.monster.Monster;
import org.RokueLike.domain.model.entity.monster.behaviour.IMonsterBehaviour;
import static org.RokueLike.utils.Constants.*;
public class Teleport implements IMonsterBehaviour {
    private final HallGrid hallGrid;
    private double timeAccumulator = 0;
    public Teleport(HallGrid grid) {
        this.hallGrid = grid;
    }
    @Override
    public void behaviour(Monster monster) {
        timeAccumulator += GAME_DELAY;
        if (timeAccumulator >= WIZARD_TELEPORT) { // 3 seconds have passed
            hallGrid.changeRuneLocation();
            System.out.println("Rune location changed.");
            timeAccumulator = 0; // Reset timer
        }
    }
}
