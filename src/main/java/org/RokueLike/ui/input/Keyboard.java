package org.RokueLike.ui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.HashMap;

public class Keyboard implements KeyListener {

    private static boolean[] keys; // Tracks the pressed state of all keys

    // Maps action names to corresponding key codes
    private static final Map<String, Integer> keyBindings = new HashMap<>();
    static {
        keyBindings.put("UP", KeyEvent.VK_UP);
        keyBindings.put("DOWN", KeyEvent.VK_DOWN);
        keyBindings.put("LEFT", KeyEvent.VK_LEFT);
        keyBindings.put("RIGHT", KeyEvent.VK_RIGHT);
        keyBindings.put("USE_REVEAL", KeyEvent.VK_R);
        keyBindings.put("USE_PROTECTION", KeyEvent.VK_P);
        keyBindings.put("USE_LURING", KeyEvent.VK_B);
        keyBindings.put("LURE_UP", KeyEvent.VK_W);
        keyBindings.put("LURE_DOWN", KeyEvent.VK_S);
        keyBindings.put("LURE_LEFT", KeyEvent.VK_A);
        keyBindings.put("LURE_RIGHT", KeyEvent.VK_D);
    }

    //Classes like MouseBuild, MousePlay, and Keyboard bridge input events from the UI to the GameManager
    public Keyboard() {
        keys = new boolean[256];
    }

    // Checks if a specific action's key is currently pressed
    public static boolean isKeyPressed(String action) {
        Integer keyCode = keyBindings.get(action);
        return keyCode != null && keys[keyCode];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

}