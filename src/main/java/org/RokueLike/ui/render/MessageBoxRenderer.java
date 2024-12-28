package org.RokueLike.ui.render;

import org.RokueLike.utils.FontLoader;
import org.RokueLike.utils.MessageBox;

import java.awt.*;

public class MessageBoxRenderer {

    private final Font customFont;
    public Rectangle messageBox;

    public MessageBoxRenderer(float size) {
        customFont = FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", size);
        messageBox = new Rectangle(100, 600, 600, 50);
    }

    // Renders the message box with the current message.
    public void renderMessageBox(Graphics2D g, MessageBox message) {
        String currentMessage = message.getMessage();

        // Check if there's a message to render
        if (currentMessage == null || message.getTime() == null || message.getTime() <= 0) {
            return;
        }

        // Set font and calculate the required width
        g.setFont(customFont);
        int textWidth = g.getFontMetrics().stringWidth(currentMessage);
        int padding = 20; // Padding around the text
        int adjustedWidth = textWidth + padding * 2;

        // Dynamically adjust the width of the message box if needed
        messageBox.width = Math.max(adjustedWidth, 200); // Ensure a minimum width

        // Draw the background of the message box
        g.setColor(new Color(80, 80, 100));
        g.fillRoundRect(messageBox.x, messageBox.y, messageBox.width, messageBox.height, 10, 10);

        // Draw the border of the message box
        g.setColor(Color.WHITE);
        g.drawRoundRect(messageBox.x, messageBox.y, messageBox.width, messageBox.height, 10, 10);

        // Calculate the text position
        int textPosX = messageBox.x + (messageBox.width - textWidth) / 2;
        int textPosY = messageBox.y + ((messageBox.height - g.getFontMetrics().getHeight()) / 2) + g.getFontMetrics().getAscent();

        // Draw the message text
        try {
            g.drawString(currentMessage, textPosX, textPosY);
        } catch (Exception e) {
            System.err.println("Error Rendering MessageBox: " + e.getMessage());
        }
    }

}