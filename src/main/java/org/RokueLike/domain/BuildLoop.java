package org.RokueLike.domain;

import org.RokueLike.ui.input.MouseBuild;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuildLoop implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if a mouse click has been registered
        if (MouseBuild.isMouseClicked()) {
            // Get the last click coordinates
            int[] coords = MouseBuild.getLastClickCoordinates();

            int x = coords[0];
            int y = coords[1];
            String hallName = MouseBuild.getLastClickedHall();

            System.out.println("[BuildLoop]: Processing click at (" + x + ", " + y + ") in " + hallName);

            // Place the object in the BuildManager
            BuildManager.placeObjectManually(hallName, x, y);

            // Reset the mouse click state to prevent repeated processing
            MouseBuild.resetMouseClick();
        }
    }
}

