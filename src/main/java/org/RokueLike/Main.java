package org.RokueLike;

import org.RokueLike.ui.Textures;
import org.RokueLike.ui.Window;

public class Main {


    public static void main(String[] args) {
        try {
            System.out.println("[Main]: Starting...");
            Textures.init();
            Window.create();
            System.out.println("[Main]: Started!");
        } catch(Exception e) {
            System.err.println("\n[Main]: Uncaught exception in initialization!\n");
            System.exit(-1);

        }
    }



}

