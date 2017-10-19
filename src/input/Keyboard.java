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
    public boolean up, down, left, right, space, c,e,q,w,s,a,d;

    public void update() {
        up = keys[VK_UP];
        down = keys[VK_DOWN];
        right = keys[VK_RIGHT];
        left = keys[VK_LEFT];
        space = keys[VK_SPACE];
        c = keys[VK_C];
        e=keys[VK_E];
        q = keys[VK_Q];
        w=keys[VK_W];
        s = keys[VK_S];
        a = keys[VK_A];
        d = keys[VK_D];
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
