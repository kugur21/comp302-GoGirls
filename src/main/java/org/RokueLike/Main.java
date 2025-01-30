package org.RokueLike;

import org.RokueLike.utils.Textures;
import org.RokueLike.ui.Window;

import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        // Set the default locale to English to ensure there are no problems extracting the Textures
        Locale.setDefault(Locale.ENGLISH);
        try {
            System.out.println("[Main]: Starting...");

            // Initialize textures required for the game
            Textures.init();
            // Create the main application window
            Window.create();

        } catch(Exception e) {

            System.err.println("\n[Main]: Uncaught exception in initialization!\n");
            System.exit(-1);

        }
    }

}