package org.RokueLike.utils;

public class Constants {

    // General settings
    public static final int GRID_WIDTH = 15;
    public static final int GRID_HEIGHT = 15;

    // BUILD MODE CONSTANTS

    public static final int BUILD_WINDOW_WIDTH = 900;
    public static final int BUILD_WINDOW_HEIGHT = 700;
    public static final int BUILD_TILE_SIZE = 16; // Size of each grid tile in pixels
    public static final int BUILD_GRID_OFFSET_X = 50; // X-offset for hall rendering
    public static final int BUILD_GRID_OFFSET_Y = 50; // Y-offset for hall rendering

    // Offset for each hall
    public static final int BUILD_EARTH_X = BUILD_GRID_OFFSET_X;
    public static final int BUILD_EARTH_Y = BUILD_GRID_OFFSET_Y;
    public static final int BUILD_AIR_X = (int) (BUILD_GRID_OFFSET_X + (BUILD_WINDOW_WIDTH * 0.39));
    public static final int BUILD_AIR_Y = BUILD_GRID_OFFSET_Y;
    public static final int BUILD_WATER_X = BUILD_GRID_OFFSET_X;
    public static final int BUILD_WATER_Y = (int) (BUILD_GRID_OFFSET_Y + (BUILD_WINDOW_HEIGHT * 0.50));
    public static final int BUILD_FIRE_X = (int) (BUILD_GRID_OFFSET_X + (BUILD_WINDOW_WIDTH * 0.39));
    public static final int BUILD_FIRE_Y = (int) (BUILD_GRID_OFFSET_Y + (BUILD_WINDOW_HEIGHT * 0.50));

    // Inventory settings
    public static final int BUILD_INVENTORY_X = (int) (BUILD_WINDOW_WIDTH * 0.77);
    public static final int BUILD_INVENTORY_Y = (int) (BUILD_WINDOW_HEIGHT * 0.28);
    public static final int BUILD_INVENTORY_WIDTH = 60;
    public static final int BUILD_INVENTORY_HEIGHT = 180;
    public static final int BUILD_INVENTORY_SLOT_SIZE = 40;

    // PLAY MODE CONSTANTS

    public static final int PLAY_WINDOW_WIDTH = 1150;
    public static final int PLAY_WINDOW_HEIGHT = 700;
    public static final int PLAY_TILE_SIZE = 36; // Size of each grid tile in pixels
    public static final int PLAY_GRID_OFFSET_X = 70; // X-offset for hall rendering
    public static final int PLAY_GRID_OFFSET_Y = 50; // Y-offset for hall rendering

    public static final int GAME_DELAY = 100;
    public static final int MONSTER_MOVEMENT_DELAY = 500;
    public static final int MONSTER_SPAWN = 8000;
    public static final int ENCHANTMENT_SPAWN = 12000;
    public static final int ENCHANTMENT_DURATION = 6000;
    public static final int WIZARD_BEHAVIOR = 5000;
    public static final int REVEAL_ENCHANTMENT_DURATION = 10000;
    public static final int CLOAK_ENCHANTMENT_DURATION = 20000;
    public static final int IMMUNE_TIME = 3000;

    public static final int MAX_TIME = 60;
    public static final int MAX_LIVES = 4;
    public static final int EXTRA_TIME = 5;

}