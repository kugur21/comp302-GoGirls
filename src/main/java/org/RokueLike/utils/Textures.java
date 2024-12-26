package org.RokueLike.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class Textures {

    private static HashMap<String, BufferedImage> sprites; // Stores the sprites mapped by their names

    // Initializes the sprite map by loading data from the sprite sheet and JSON.
    public static void init() {
        sprites = new HashMap<>();

        try {
            // Load sprite sheet image from resources
            InputStream spriteSheetStream = Textures.class.getClassLoader().getResourceAsStream("images/0x72_16x16DungeonTileset.v5.png");
            if (spriteSheetStream == null) {
                throw new IllegalArgumentException("Sprite sheet not found in resources/images.");
            }
            BufferedImage spriteSheet = ImageIO.read(spriteSheetStream);

            // Load JSON metadata from resources
            InputStream jsonStream = Textures.class.getClassLoader().getResourceAsStream("0x72_16x16DungeonTileset.v5.json");
            if (jsonStream == null) {
                throw new IllegalArgumentException("JSON file not found in resources.");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonStream);

            // Extract and map sprites from JSON metadata
            JsonNode slices = root.get("meta").get("slices");
            for (JsonNode slice : slices) {
                String name = slice.get("name").asText();
                JsonNode bounds = slice.get("keys").get(0).get("bounds");

                int x = bounds.get("x").asInt();
                int y = bounds.get("y").asInt();
                int w = bounds.get("w").asInt();
                int h = bounds.get("h").asInt();

                BufferedImage sprite = spriteSheet.getSubimage(x, y, w, h);
                sprites.put(name, sprite);
            }
            System.out.println("[Textures]: Successfully loaded sprites from JSON!");

            // Load additional PNGs from the "imagesekstra" folder
            loadAllPNGs();

        } catch (Exception e) {
            System.err.println("[Textures]: Failed to load sprites!");
        }
    }

    // Loads all PNG images from the "imagesekstra" folder into the sprite map.
    private static void loadAllPNGs() {
        try {
            File folder = new File(Objects.requireNonNull(Textures.class.getClassLoader().getResource("imagesekstra")).getFile());
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.getName().endsWith(".png")) {
                    String name = file.getName().replace(".png", "");
                    BufferedImage image = ImageIO.read(file);
                    sprites.put(name, image);
                    System.out.println("[Textures]: Loaded " + name);
                }
            }
        } catch (Exception e) {
            System.err.println("[Textures]: Failed to load PNGs from folder: " + "imagesekstra");
        }
    }

    // Retrieves a sprite by its name, used throughout the program.
    public static BufferedImage getSprite(String name) {
        BufferedImage sprite = sprites.get(name);
        if (sprite != null) return sprite;
        else {
            System.err.println("[Textures]: Sprite '" + name + "' not found!");
            return null;
        }
    }

}