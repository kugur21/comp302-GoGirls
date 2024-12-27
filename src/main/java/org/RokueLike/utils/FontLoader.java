package org.RokueLike.utils;

import java.awt.*;
import java.io.InputStream;

public class FontLoader {

    // Loads a custom font from the given file path and resizes it.
    public static Font loadFont(String fontPath, float size) {
        try {
            InputStream fontStream = FontLoader.class.getClassLoader().getResourceAsStream(fontPath);
            if (fontStream == null) {
                throw new IllegalArgumentException("Font file not found: " + fontPath);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return font.deriveFont(size);
        } catch (Exception e) {
            System.err.println("Failed to load font " + fontPath + ": " + e.getMessage());
            return new Font("Arial", Font.PLAIN, (int) size);
        }
    }

}