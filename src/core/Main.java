package core;

import graphics.Camera;
import graphics.Renderer;
import input.Keyboard;
import input.Mouse;
import util.math.geom.Face3;
import util.math.linearAlgebra.Quaternion4f;
import util.math.linearAlgebra.Vector2f;
import util.math.linearAlgebra.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Main extends Canvas implements Runnable {
    public static final int WIDTH = 600;
    public static final int HEIGHT = (int) (WIDTH * 9.0 / 16.0);
    public static final float SCALE = 2.0f;
    public static final String TITLE = "3D Engine";
    public static Graphics2D g2d;

    private JFrame frame;
    private boolean running = false;
    private Thread gameThread;
    private BufferedImage toRender = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) toRender.getRaster().getDataBuffer()).getData();
    private Renderer renderer;
    private static Keyboard key;
    private static Mouse mouse;

    public Main() {
        init();
        Face3 f = new Face3(new Vector3f(1, 2, 3), new Vector3f(3, 1, 2), new Vector3f(2, 3, 1));
        Vector3f v1 = f.getP1();
        Vector3f v2 = f.getP2();
        Vector3f v3 = f.getP3();
        float l = (v2.y - v1.y) / (v3.y - v1.y);
        float x = (v1.x) + l * (v3.x - v1.x);;
        float z = (v1.z) + l * (v3.z - v1.z);
        Vector3f v4 = new Vector3f(x, v2.y, z);

        System.out.println(v1);
        System.out.println(v2);
        System.out.println(v3);
        System.out.println(v4);

    }

    private void init() {
        Dimension size = new Dimension((int) (SCALE * WIDTH), (int) (SCALE * HEIGHT));
        setPreferredSize(size);
        key = new Keyboard();
        mouse = new Mouse();
        addKeyListener(key);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        renderer = new graphics.Renderer(WIDTH, HEIGHT);
        initFrame();

    }

    private void initFrame() {
        frame = new JFrame(TITLE);
        frame.setResizable(false);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        System.out.println("Window loaded: " + TITLE);
        System.out.println("Buffer size: " + WIDTH + " : " + HEIGHT);
        System.out.println("graphics.Renderer size: " + getWidth() + " : " + getHeight());
    }

    public synchronized void start() {
        running = true;
        gameThread = new Thread(this, TITLE);
        gameThread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = Time.getTime();
        final double ns = Time.SECOND / 60.0;
        double delta = 0;
        int frames = 0;
        int updates = 0;
        long timer = System.currentTimeMillis();
        requestFocus();
        while (running) {
            long now = Time.getTime();
            delta += (now - lastTime) / ns;
            Time.setDeltaTime(delta / 60.0);
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
                updates++;
            }
            render();
            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frame.setTitle(TITLE + " | " + updates + " ups, " + frames + " fps");
                frames = 0;
                updates = 0;
            }
        }
        stop();
    }

    private void update() {
        renderer.update();
        key.update();
    }

    private void render() {

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        renderer.clear();
        renderer.render();
        System.arraycopy(renderer.pixels, 0, pixels, 0, pixels.length);
        g2d = (Graphics2D) bs.getDrawGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.drawImage(toRender, 0, 0, getWidth(), getHeight(), null);
        g2d.dispose();
        bs.show();
    }

    public static Keyboard getKey() {
        return key;
    }

    public static Mouse getMouse() {
        return mouse;
    }

    public static void main(String[] args) {
        new Main().start();
    }


}

