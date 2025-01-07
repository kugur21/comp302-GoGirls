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

    @Test
    public void testMoveHeroToClosedDoor() {
        // Setup: Hero encounters a closed door
        Door doorCell = mock(Door.class);
        when(doorCell.getName()).thenReturn("door");
        when(mockHallGrid.getCellInFront(mockHero, 0, 1)).thenReturn(doorCell);
        when(doorCell.isOpen()).thenReturn(false);

        // Act: Move hero
        heroManager.moveHero(mockHallManager, 0, 1);

        // Assert: Verify no hall transition or hero movement
        verify(mockHallManager, never()).moveToNextHall();
        verify(mockHero, never()).setPosition(anyInt(), anyInt());
    }

    @Test
    public void testMoveHeroToOpenDoorNextHall() {
        // Spy on HeroManager to mock the handleHallTransition behavior
        HeroManager heroManagerSpy = spy(heroManager);

        // Mock the door cell
        Door doorCell = mock(Door.class);
        when(doorCell.getName()).thenReturn("door");
        when(mockHallGrid.getCellInFront(mockHero, 0, 1)).thenReturn(doorCell);
        when(doorCell.isOpen()).thenReturn(true);

        // Mock hall transition
        when(mockHallManager.moveToNextHall()).thenReturn(true);
        HallGrid nextHallGrid = mock(HallGrid.class);
        when(mockHallManager.getCurrentHall()).thenReturn(nextHallGrid);

        // Do nothing for handleHallTransition
        doNothing().when(heroManagerSpy).handleHallTransition(nextHallGrid);

        // Act: Move hero
        heroManagerSpy.moveHero(mockHallManager, 0, 1);

        // Assert: Verify interactions
        verify(mockHallManager).moveToNextHall();
        verify(mockHallManager).getCurrentHall();
        verify(heroManagerSpy).handleHallTransition(nextHallGrid);
    }

    @Test
    public void testMoveHeroToOpenDoorVictory() {
        // Spy on HeroManager to mock the handleGameWin behavior
        HeroManager heroManagerSpy = spy(heroManager);

        // Mock the door cell
        Door doorCell = mock(Door.class);
        when(doorCell.getName()).thenReturn("door");
        when(mockHallGrid.getCellInFront(mockHero, 0, 1)).thenReturn(doorCell);
        when(doorCell.isOpen()).thenReturn(true);

        // Mock game ending
        when(mockHallManager.moveToNextHall()).thenReturn(false);

        // Do nothing for handleGameWin
        doNothing().when(heroManagerSpy).handleGameWin();

        // Act: Move hero
        heroManagerSpy.moveHero(mockHallManager, 0, 1);

        // Assert: Verify interactions
        verify(mockHallManager).moveToNextHall();
        verify(heroManagerSpy).handleGameWin();
    }
}