package org.RokueLike;

import org.RokueLike.domain.manager.BuildManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

/// Tests for placeObjectManually method in BuildManager
public class ObjectPlacementTest {
    // SERBAY - placeObjectManually in BuildManager

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        BuildManager.init();  // Initializes the BuildManager and its halls
    }

    @Test
    public void testSuccessfulObjectPlacement() {
        // Arrange
        int x = 2, y = 3, objectType = 1;
        String hallName = "earth";

        // Act
        BuildManager.placeObjectManually(hallName, x, y, objectType);

        // Assert
        String[][] hall = BuildManager.getHall(hallName);
        assert hall != null;
        assertEquals("o1", hall[y][x], "Object should be successfully placed in the hall.");
    }
    @Test
    public void testPlacementExceedsCapacity() {
        // Arrange
        String hallName = "earth";
        int objectType = 1;

        // Simulate the hall reaching capacity
        int limit = BuildManager.getHallObjectLimit(hallName);
        for (int i = 1; i <= limit; i++) {
            BuildManager.placeObjectManually(hallName, i, 1, objectType);
        }

        // Act
        BuildManager.placeObjectManually(hallName, 5, 5, objectType);  // Attempt to place an additional object

        // Assert
        String[][] hall = BuildManager.getHall(hallName);
        assert hall != null;
        assertEquals(".", hall[5][5], "Object should not be placed beyond the hall's capacity.");
    }

}