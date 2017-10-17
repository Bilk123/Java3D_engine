package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.event.KeyEvent.*;

/**
 * Created by Blake on 3/6/2017.
 */
public class Keyboard implements KeyListener {
    public static final int KEYS = 256;

    private boolean[] keys = new boolean[KEYS];
    public boolean up, down, left, right, space, c;

    public void update() {
        up = keys[VK_UP] || keys[VK_W];
        down = keys[VK_DOWN] || keys[VK_S];
        right = keys[VK_RIGHT] || keys[VK_D];
        left = keys[VK_LEFT] || keys[VK_A];
        space = keys[VK_SPACE];
        c = keys[VK_C];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
