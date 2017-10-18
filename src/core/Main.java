package core;

import graphics.Renderer;
import input.Keyboard;
import input.Mouse;
import util.math.linearAlgebra.Matrix;
import util.math.linearAlgebra.Matrix4f;
import util.math.linearAlgebra.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Main extends Canvas implements Runnable {
    public static final int WIDTH = 600;
    public static final int HEIGHT = (int) (WIDTH * 9.0 / 16.0);
    public static final int SCALE = 2;
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
    }

    private void init() {
        Dimension size = new Dimension(SCALE * WIDTH, SCALE * HEIGHT);
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

