package org.RokueLike.ui.screen;

import org.RokueLike.domain.GameManager;
import org.RokueLike.ui.render.MessageBoxRenderer;
import org.RokueLike.utils.FontLoader;
import org.RokueLike.ui.Window;
import org.RokueLike.utils.MessageBox;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class LoadGameScreen extends JPanel {

    private final JList<String> fileList;
    private final DefaultListModel<String> listModel;
    private final Font customFont;
    private final MessageBoxRenderer messageBoxRenderer;
    private MessageBox messageBox;

    public LoadGameScreen() {
        customFont = FontLoader.loadFont("fonts/PressStart2P-Regular.ttf", 18f);
        messageBoxRenderer = new MessageBoxRenderer(18f);

        this.setLayout(null);
        this.setBackground(new Color(66,40,53));

        JLabel titleLabel = new JLabel("Select a Save File");
        titleLabel.setBounds(200, 50, 400, 50);
        titleLabel.setFont(customFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleLabel);

        // File list
        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setBounds(200, 150, 400, 300);
        fileList.setFont(customFont);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(fileList);

        // Populate list with save files
        if (!loadSaveFiles()) {
            fileList.setVisible(false);
            JLabel noFilesLabel = new JLabel("No Saved Files Available");
            noFilesLabel.setBounds(200, 200, 400, 50);
            noFilesLabel.setFont(customFont.deriveFont(12f));
            noFilesLabel.setForeground(Color.RED);
            noFilesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(noFilesLabel);
        }

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(200, 500, 150, 50);
        backButton.setFont(customFont);
        backButton.addActionListener(e -> Window.showScreen("LaunchScreen"));
        this.add(backButton);

        // Play Button
        JButton playButton = new JButton("Play");
        playButton.setBounds(450, 500, 150, 50);
        playButton.setFont(customFont);
        playButton.addActionListener(e -> {
            String selectedFile = fileList.getSelectedValue();
            if (selectedFile != null) {
                GameManager.loadGame("src/main/resources/saves/" + selectedFile);
                Window.addScreen(new PlayModeScreen(selectedFile), "PlayModeScreen", true);
            } else {
                messageBox = new MessageBox();
                messageBox.addMessage("Please select a save file to load.", 1);
                repaint();
            }
        });
        this.add(playButton);
    }

    // Loads the saved files and returns true if successful.
    private boolean loadSaveFiles() {
        File saveDir = new File("src/main/resources/saves/");
        if (!saveDir.exists() || Objects.requireNonNull(saveDir.listFiles()).length == 0) {
            return false;
        }
        File[] saveFiles = saveDir.listFiles((dir, name) -> name.endsWith(".dat"));
        if (saveFiles != null) {
            for (File file : saveFiles) {
                listModel.addElement(file.getName());
            }
        }
        return true;
    }

    // Renders the build mode visuals using the BuildModeRenderer.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (messageBox != null) {
            messageBoxRenderer.renderMessageBox((Graphics2D) g, messageBox); // Render custom graphics for build mode
        }
    }
}
