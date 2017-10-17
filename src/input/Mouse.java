package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Blake on 3/6/2017.
 */
public class Mouse implements MouseListener, MouseMotionListener {
    public static int mx = -1, my = -1, mb = -1;

    public static int getMx() {
        return mx;
    }

    public static int getMy() {
        return my;
    }

    public static int getMb() {
        return mb;
    }

    public void update() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mb = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mb = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
    }
}
