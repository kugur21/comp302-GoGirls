package org.RokueLike.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
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

            // Dinamik olarak tüm PNG dosyalarını yükle
            loadAllPNGs("imagesekstra"); // "images" klasöründeki tüm PNG dosyalarını yükle

        } catch (Exception e) {
            System.err.println("[Textures]: Failed to load sprites!");
            e.printStackTrace();
        }
    }

    // Tüm PNG dosyalarını yükleme
    private static void loadAllPNGs(String folderPath) {
        try {
            // Klasör altındaki tüm dosyaları oku
            File folder = new File(Objects.requireNonNull(Textures.class.getClassLoader().getResource(folderPath)).getFile());
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.getName().endsWith(".png")) { // Sadece PNG dosyalarını işleme al
                    String name = file.getName().replace(".png", ""); // Dosya adını al
                    BufferedImage image = ImageIO.read(file); // Dosyayı BufferedImage olarak oku
                    sprites.put(name, image); // HashMap'e ekle
                    System.out.println("[Textures]: Loaded " + name);
                }
            }
        } catch (Exception e) {
            System.err.println("[Textures]: Failed to load PNGs from folder: " + folderPath);
            e.printStackTrace();
        }
    }

    // Sprite'ı adından getiren metod
    public static BufferedImage getSprite(String name) {
        BufferedImage sprite = sprites.get(name);
        if (sprite != null) return sprite;
        else {
            System.err.println("[Textures]: Sprite '" + name + "' not found!");
            return null;
        }
    }

    public static void addSprite(String name, BufferedImage image) {
        if (sprites == null) {
            sprites = new HashMap<>();
        }
        sprites.put(name, image);
        System.out.println("[Textures]: Added sprite -> " + name);
    }

    // Tüm sprite isimlerini döndüren metod
    public static Set<String> getSpriteNames() {
        return sprites.keySet();
    }

    // Yeni Eklenen Metod: PNG Dosyasını BufferedImage Olarak Yükler
    public static BufferedImage loadPNG(String filePath) {
        try {
            InputStream stream = Textures.class.getClassLoader().getResourceAsStream(filePath);
            if (stream == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            return ImageIO.read(stream);
        } catch (Exception e) {
            System.err.println("[Textures]: Failed to load PNG file: " + filePath);
            e.printStackTrace();
            return null;
        }
    }

}