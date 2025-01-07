package org.RokueLike;

import org.RokueLike.domain.hall.HallGrid;
import org.RokueLike.domain.manager.ItemManager;
import org.RokueLike.domain.model.entity.hero.Hero;
import org.RokueLike.domain.model.item.Enchantment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RevealEnchantmentTest {

    private ItemManager itemManager;

    @Mock
    private HallGrid mockHallGrid;
    @Mock
    private Hero mockHero;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        itemManager = new ItemManager(mockHallGrid, mockHero, null, true);
    }

    @Test
    public void testApplyRevealWithRuneFound() {
        // Setup: Hero has the Reveal enchantment and the rune is already found
        when(mockHero.hasEnchantment(Enchantment.EnchantmentType.REVEAL)).thenReturn(true);
        doNothing().when(mockHero).useEnchantment(Enchantment.EnchantmentType.REVEAL);
        when(mockHallGrid.findRuneRegion(4)).thenReturn(null);

        // Act
        String result = itemManager.applyReveal();

        // Assert
        verify(mockHero).useEnchantment(Enchantment.EnchantmentType.REVEAL);
        assertEquals("Rune already found. You can move to the next hall.", result);
    }

    @Test
    public void testApplyRevealSuccess() {
        // Setup: Hero has the Reveal enchantment and the rune region exists
        when(mockHero.hasEnchantment(Enchantment.EnchantmentType.REVEAL)).thenReturn(true);
        doNothing().when(mockHero).useEnchantment(Enchantment.EnchantmentType.REVEAL);
        int[][] mockRegion = new int[][]{{1, 1}, {2, 2}};
        when(mockHallGrid.findRuneRegion(4)).thenReturn(mockRegion);

        // Act
        String result = itemManager.applyReveal();

        // Assert
        verify(mockHero).useEnchantment(Enchantment.EnchantmentType.REVEAL);
        verify(mockHallGrid).findRuneRegion(4);
        verify(mockHallGrid, never()).removeEnchantment(); // Ensure no unintended effects
        assertEquals("Revealed rune region!", result);
    }

    @Test
    public void testApplyRevealWithoutEnchantment() {
        // Setup: Hero does not have the Reveal enchantment
        when(mockHero.hasEnchantment(Enchantment.EnchantmentType.REVEAL)).thenReturn(false);

        // Act
        String result = itemManager.applyReveal();

        // Assert
        verify(mockHero, never()).useEnchantment(any());
        verifyNoInteractions(mockHallGrid);
        assertEquals("Hero does not have a Reveal Enchantment.", result);
    }

}