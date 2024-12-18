package org.RokueLike;

import javax.swing.*;
import java.awt.*;
import org.RokueLike.ui.Textures ;



public class SpriteTest extends JPanel {

    public SpriteTest() {
        Textures.init(); // JSON ve sprite'ları yükle
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int tileSize = 32; // Sprite'ları büyüterek ekranda daha net görünmesini sağlıyoruz
        int padding = 10;  // Sprite'lar arasında boşluk
        int xOffset = 20;  // Ekranın başlangıç noktası X
        int yOffset = 20;  // Ekranın başlangıç noktası Y

        int row = 0, col = 0;

        // JSON'dan yüklenen sprite'ların isimlerini al
        for (String name : Textures.getSpriteNames()) { // Textures sınıfında sprite isimlerini döndüren bir metod ekleyeceğiz
            Image sprite = Textures.getSprite(name);
            int drawX = xOffset + col * (tileSize + padding);
            int drawY = yOffset + row * (tileSize + padding);

            // Sprite'ı çiz
            g.drawImage(sprite, drawX, drawY, tileSize, tileSize, null);

            // Sprite adını yaz
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString(name, drawX, drawY + tileSize + 12);

            col++;
            if (col >= 10) { // 10 sütundan sonra bir alt satıra geç
                col = 0;
                row++;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sprite Test");
        SpriteTest spriteTest = new SpriteTest();
        frame.add(spriteTest);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
