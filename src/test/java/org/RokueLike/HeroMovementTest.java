package org.RokueLike;

import org.RokueLike.domain.hall.GridCell;
import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.manager.HallManager;
import org.RokueLike.domain.manager.HeroManager;
import org.RokueLike.domain.model.entity.hero.Hero;
import org.RokueLike.domain.model.item.Door;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/// Tests for moveHero method in HeroManager
public class HeroMovementTest {
    // ERAY - moveHero in HeroManager

    private HeroManager heroManager;

    @Mock
    private HallGrid mockHallGrid;
    @Mock
    private Hero mockHero;
    @Mock
    private HallManager mockHallManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        heroManager = new HeroManager(mockHero, mockHallGrid);
    }

    @Test
    public void testMoveHeroToFloor() {
        // Setup: Hero encounters a safe floor cell
        GridCell floorCell = mock(GridCell.class);
        when(floorCell.getName()).thenReturn("floor");
        when(mockHallGrid.getCellInFront(mockHero, 1, 0)).thenReturn(floorCell);
        when(mockHallGrid.isSafeLocation(anyInt(), anyInt())).thenReturn(true);

        // Act: Move hero
        heroManager.moveHero(mockHallManager, 1, 0);

        // Assert: Verify hero's position was updated
        verify(mockHero).setPosition(anyInt(), anyInt());
    }

}
