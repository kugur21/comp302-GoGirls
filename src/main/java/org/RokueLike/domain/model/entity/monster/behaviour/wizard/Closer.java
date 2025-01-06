package org.RokueLike.domain.model.entity.monster.behaviour.wizard;
import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.model.entity.monster.Monster;
import org.RokueLike.domain.model.entity.monster.behaviour.IMonsterBehaviour;
public class Closer implements IMonsterBehaviour {
    public Closer() {
    }
    @Override
    public void behaviour(Monster monster) {
        GameManager.setWizardClosureActive(true);
    }
}
