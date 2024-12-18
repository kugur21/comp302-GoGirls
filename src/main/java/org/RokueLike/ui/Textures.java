package org.RokueLike.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

public class Textures {
    private static HashMap<String, BufferedImage> sprites;

    public static void init() {
        sprites = new HashMap<>();
        try {
            // Sprite Sheet Yükleme (PNG)
            InputStream spriteSheetStream = Textures.class.getClassLoader()
                    .getResourceAsStream("images/0x72_16x16DungeonTileset.v5.png");
            if (spriteSheetStream == null) {
                throw new IllegalArgumentException("Sprite sheet not found in resources/images.");
            }
            BufferedImage spriteSheet = ImageIO.read(spriteSheetStream);

            // JSON Dosyasını Yükleme
            InputStream jsonStream = Textures.class.getClassLoader()
                    .getResourceAsStream("0x72_16x16DungeonTileset.v5.json");

            if (jsonStream == null) {
                throw new IllegalArgumentException("JSON file not found in resources.");
            }


            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonStream);

            // JSON'dan Sprite'ları Parçalama
            JsonNode slices = root.get("meta").get("slices");
            for (JsonNode slice : slices) {
                String name = slice.get("name").asText();
                JsonNode bounds = slice.get("keys").get(0).get("bounds");

                int x = bounds.get("x").asInt();
                int y = bounds.get("y").asInt();
                int w = bounds.get("w").asInt();
                int h = bounds.get("h").asInt();

                // Sprite'ı Kes ve HashMap'e Ekle
                BufferedImage sprite = spriteSheet.getSubimage(x, y, w, h);
                sprites.put(name, sprite);
            }

            System.out.println("[Textures]: Successfully loaded sprites from JSON!");
        } catch (Exception e) {
            System.err.println("[Textures]: Failed to load sprites!");
            e.printStackTrace();
        }
    }

    public static BufferedImage getSprite(String name) {
        BufferedImage sprite = sprites.get(name);
        if (sprite != null) return sprite;
        else {
            System.err.println("[Textures]: Sprite '" + name + "' not found!");
            return null;
        }
    }
    public static Set<String> getSpriteNames() {
        return sprites.keySet(); // HashMap'teki tüm anahtarları (sprite isimlerini) döner
    }
}
