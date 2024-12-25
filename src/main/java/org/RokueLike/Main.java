package org.RokueLike;

import org.RokueLike.ui.Textures;
import org.RokueLike.ui.Window;

import java.util.Locale;

public class Main {


    // Note: There are MessageBox instance in our code which notifies the user on relevant game states.
    // However, there are sometimes delays on these messages due to the hardware issues of the computer that our program is executed.
    // We will come up with a better solution in the final submission deadline.
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
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

