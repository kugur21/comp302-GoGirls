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
        Textures.addSprite("player", Textures.loadPNG("images/player.png")); // "player" sprite'ı eklendi
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int tileSize = 32; // Sprite'ların boyutları
        int padding = 10;  // Sprite'lar arası boşluk
        int xOffset = 20;  // Başlangıç noktası X
        int yOffset = 20;  // Başlangıç noktası Y

        int row = 0, col = 0; // Satır ve sütun başlangıcı

        // Yüklenen tüm sprite'ların isimlerini al
        for (String name : Textures.getSpriteNames()) {
            if (name.equals("player")) {
                // Player sprite'ı en sağ üstte göster
                continue; // Döngüde atla, özel konumlandırma aşağıda yapılacak
            }

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

        // Player sprite'ı özel olarak en sağ üst köşeye yerleştir
        Image playerSprite = Textures.getSprite("player");
        if (playerSprite != null) {
            int panelWidth = getWidth();
            int drawX = panelWidth - tileSize - xOffset; // Sağ kenardan tileSize kadar uzaklık bırak
            int drawY = yOffset; // Üst kenara yakınlık

            // Player'ı çiz
            g.drawImage(playerSprite, drawX, drawY, tileSize, tileSize, null);

            // İsmini yazdır
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString("player", drawX, drawY + tileSize + 12);
        }
    }

    public static void main(String[] args) {
        // Test penceresi oluştur
        JFrame frame = new JFrame("Sprite Test");
        SpriteTest spriteTest = new SpriteTest();
        frame.add(spriteTest);

        // Pencere boyutlarını ayarla
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
