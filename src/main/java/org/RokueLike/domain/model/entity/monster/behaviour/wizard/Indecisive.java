package org.RokueLike.domain.model.entity.monster.behaviour.wizard;
import org.RokueLike.domain.model.entity.monster.Monster;
import org.RokueLike.domain.model.entity.monster.behaviour.IMonsterBehaviour;
import static org.RokueLike.utils.Constants.*;
public class Indecisive implements IMonsterBehaviour {
    private double timeElapsed = 0;
    public Indecisive() {
    }
    @Override
    public void behaviour(Monster monster) {
        timeElapsed += GAME_DELAY;
        if (timeElapsed >= WIZARD_DISAPPEAR) { // 2 seconds have passed
            monster.markForRemoval(); // Mark the monster for removal
            System.out.println("Monster disappears after being indecisive.");
        }
    }
}
