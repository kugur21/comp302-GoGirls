package org.RokueLike.ui;

import java.awt.*;
import java.io.InputStream;

public class FontLoader {

    /**
     * Yüklenen fontu döndüren metot.
     *
     * @param fontPath Font dosyasının yolunu belirtir.
     * @param size     Font boyutunu belirtir.
     * @return Yüklenen font.
     */
    public static Font loadFont(String fontPath, float size) {
        try {
            InputStream fontStream = FontLoader.class.getClassLoader().getResourceAsStream(fontPath);
            if (fontStream == null) {
                throw new IllegalArgumentException("Font file not found: " + fontPath);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return font.deriveFont(size); // Belirtilen boyutta font döndür
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int) size); // Yedek font
        }
    }
}
