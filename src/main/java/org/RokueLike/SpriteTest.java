package org.RokueLike;

import javax.swing.*;
import java.awt.*;
import org.RokueLike.ui.Textures;

public class SpriteTest extends JPanel {

    public SpriteTest() {
        // Textures sınıfını başlat (JSON ve PNG'leri yükle)
        Textures.init();

        // Manuel olarak yüklenen PNG'leri ekle
        addCustomSprites();
    }

    private void addCustomSprites() {
        // player.png gibi manuel olarak ekleyeceğin görselleri burada yükle
        Textures.addSprite("player", Textures.loadPNG("imagesekstra/player.png")); // "player" sprite'ı eklendi
        Textures.addSprite("enemy", Textures.loadPNG("imagesekstra/wizard.png"));  // Örnek başka bir sprite
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int tileSize = 32; // Sprite'ların boyutları
        int padding = 10;  // Sprite'lar arası boşluk
        int xOffset = 20;  // Başlangıç noktası X
        int yOffset = 20;  // Başlangıç noktası Y

        int row = 0, col = 0; // Satır ve sütun başlangıcı

        // Manuel eklenen PNG sprite'ların adlarını döndürmek için
        String[] manualSprites = {"player", "enemy"}; // Yalnızca manuel eklenen sprite isimlerini belirtin

        for (String name : manualSprites) {
            Image sprite = Textures.getSprite(name);
            if (sprite == null) {
                System.err.println("[SpriteTest]: Missing sprite -> " + name);
                continue; // Eksik sprite'ları atla
            }

            // Çizim koordinatlarını hesapla
            int drawX = xOffset + col * (tileSize + padding);
            int drawY = yOffset + row * (tileSize + padding);

            // Sprite'ı ekrana çiz
            g.drawImage(sprite, drawX, drawY, tileSize, tileSize, null);

            // Sprite adını yazdır
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString(name, drawX, drawY + tileSize + 12);

            col++;
            if (col >= 10) { // 10 sütundan sonra yeni satıra geç
                col = 0;
                row++;
            }
        }
    }

    public static void main(String[] args) {
        // Test penceresi oluştur
        JFrame frame = new JFrame("Manual Sprite Test");
        SpriteTest spriteTest = new SpriteTest();
        frame.add(spriteTest);

        // Pencere boyutlarını ayarla
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
