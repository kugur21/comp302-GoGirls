package org.RokueLike;

import org.RokueLike.domain.GameManager;
import org.RokueLike.domain.manager.MonsterManager;
import org.RokueLike.domain.model.entity.hero.Hero;
import org.RokueLike.domain.model.entity.monster.Arrow;
import org.RokueLike.domain.model.entity.monster.Monster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/// Tests for updateArcherArrows method in MonsterManager
public class ArcherArrowsTest {
    // EMSALI - updateArcherArrows in MonsterManager
    private MonsterManager monsterManager;

    @Mock
    private Hero mockHero;

    private List<Arrow> activeArrows;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        List<Monster> monsters = new ArrayList<>();
        monsterManager = spy(new MonsterManager(monsters, null, mockHero, true));
        activeArrows = monsterManager.getArrows();
        activeArrows.clear();

        GameManager.setCloakActive(false);
    }

    @Test
    public void testArrowsNotUpdatedWhenCloakActive() {
        // Mock GameManager behavior for cloak being active
        GameManager.setCloakActive(true);

        // Act
        monsterManager.updateArcherArrows();

        // Assert
        verify(monsterManager, never()).killHero();
        assertTrue(activeArrows.isEmpty());
    }
    @Test
    public void testArrowHitsHero() {
        // Add a mock arrow in the path of the hero
        Arrow mockArrow = mock(Arrow.class);
        when(mockArrow.isActive()).thenReturn(true);
        when(mockArrow.getX()).thenReturn(5);
        when(mockArrow.getY()).thenReturn(5);
        activeArrows.add(mockArrow);

        when(mockHero.getPositionX()).thenReturn(5);
        when(mockHero.getPositionY()).thenReturn(5);

        // Mock killHero to do nothing during this test
        doNothing().when(monsterManager).killHero();

        // Act
        monsterManager.updateArcherArrows();

        // Assert
        verify(monsterManager).killHero();
        verify(mockArrow).deactivate();
    }


}

