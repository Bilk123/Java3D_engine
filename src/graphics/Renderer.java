package graphics;

import core.Main;
import util.Util;
import util.math.geom.Face3;
import util.math.linearAlgebra.*;

import java.util.ArrayList;
import java.util.Random;

public class Renderer {
    public static final int MAX_VERTICES = 100000;
    public static int clearColor = 0x0;
    private int width, height;
    public int[] pixels;
    public float[] zBuffer;
    private Camera cam;

    public Vector3f[] vertices;
    private int[] indices;
    private ArrayList<Face3> faces;


    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        vertices = new Vector3f[MAX_VERTICES];
        indices = new int[3 * MAX_VERTICES];
        faces = new ArrayList<>();
        pixels = new int[width * height];
        zBuffer = new float[width * height];
        clear();
        cam = new Camera(new Vector3f(0, 3, 10), new Vector3f(1, 1, 1), new Quaternion4f(new Vector3f(0, 1, 0), 0));
        processPoints();
    }

    private Face3 sz1, sz2, sx1, sx2, sy1, sy2;

    public void processPoints() {
        for (int x = 0; x < 10; x++)
            for (int y = 0; y < 10; y++)
                for (int z = 0; z < 10; z++) {
                    vertices[6 + x + y * 10 + z * 100] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(x - 5, y - 5, z)));
                }

        vertices[0] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(-5, -5, 5)));
        vertices[1] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(5, -5, 5)));
        vertices[2] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(5, 5, 5)));
        vertices[3] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(-5, 5, 5)));
        sz1 = new Face3(vertices[0], vertices[1], vertices[2]);
        sz2 = new Face3(vertices[2], vertices[3], vertices[0]);
        sz1.setColor(0xff);
        sz2.setColor(0xff);

        vertices[4] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(0, -5, 0)));
        vertices[5] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(0, -5, 10)));
        vertices[6] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(0, 5, 10)));
        vertices[7] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(0, 5, 0)));
        sx1 = new Face3(vertices[4], vertices[5], vertices[6]);
        sx2 = new Face3(vertices[6], vertices[7], vertices[4]);
        sx1.setColor(0xff00);
        sx2.setColor(0xff00);

        vertices[8] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(-5, 0, 0)));
        vertices[9] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(-5, 0, 10)));
        vertices[10] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(5, 0, 10)));
        vertices[11] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(5, 0, 0)));
        sy1 = new Face3(vertices[8], vertices[9], vertices[10]);
        sy2 = new Face3(vertices[10], vertices[11], vertices[8]);
        sy1.setColor(0xff0000);
        sy2.setColor(0xff0000);
    }

    public void clear() {
        for (int i = 0; i < width * height; i++) {
            pixels[i] = clearColor;
            zBuffer[i] = 1.0f;
        }
    }

    public void setPixel(int x, int y, int color, float depth) {
        if ((x) >= 0 && (x) < width && (y) >= 0 && (y) < height)
            if (getPixelDepth(x, y) > depth) {
                pixels[(x) + (y) * width] = color;
                setPixelDepth(x, y, depth);
            }
    }

    public void setPixel(Vector3f vec, int color) {
        setPixel((int) vec.x, (int) vec.y, color, vec.z);
    }

    public void setPixelDepth(int x, int y, float depth) {
        if (x >= 0 && x < width && y >= 0 && y < height)
            zBuffer[x + y * width] = depth;

    }

    public float getPixelDepth(int x, int y) {
        return zBuffer[x + y * width];
    }

    public void drawLine(Vector3f vec1, Vector3f vec2) {
        Vector3f c = vec2.sub(vec1);
        float len = c.len();
        c.normalise();
        for (int i = 0; i < len; i++) {
            setPixel(c.mul(i).add(vec1), 0xff0000);
        }
    }

    Face3 f, f1;

    public void update() {
        cam.update();
        processPoints();

    }

    public void render() {
        drawTriangle(sz1);
        drawTriangle(sz2);
        drawTriangle(sx1);
        drawTriangle(sx2);
        drawTriangle(sy1);
        drawTriangle(sy2);
        for (int i = 0; i < MAX_VERTICES; i++) {
            if (vertices[i] != null)
                setPixel(vertices[i], 0xffffff);
        }
    }

    public void addFace(Face3 f) {
        faces.add(f);

    }

    public static void setClearColor(int color) {
        clearColor = color;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //v1.y <v2.y &&v1.y<v3.y
    private void fillBottomFlatTriangle(Vector3f v1, Vector3f v2, Vector3f v3, int color) {

        for (int scanLine = (int) v1.y; scanLine < v2.y; scanLine++) {
            float l = (scanLine - v1.y) / (v2.y - v1.y);
            if (l < 0) continue;
            int r1x = (int)(v1.x + (v2.x - v1.x) * l);
            int r2x = (int)(v1.x + (v3.x - v1.x) * l);
            int r1z = (int)(v1.z + (v2.z - v1.z) * l);
            int r2z = (int)(v1.z + (v3.z - v1.z) * l);
            if (r2x < r1x) {
                int temp = r1x;
                r1x = r2x;
                r2x = temp;
                temp = r1z;
                r1z = r2z;
                r2z = temp;
            }
            float m = (float)Math.tan(Math.atan2(r2z-r1z,r2x-r1x));
            for (int x = r1x; x < r2x; x++) {
                setPixel(x, scanLine, color, r1z+x*m);
            }
        }

    }

    private void fillTopFlatTriangle(Vector3f v1, Vector3f v2, Vector3f v3, int color) {
        for (int scanLine = (int) v3.y; scanLine >= (int) (v1.y); scanLine--) {
            float l = (scanLine - v1.y) / (v3.y - v1.y);
            if (l < 0) continue;
            int r1x = (int) (v1.x + (v3.x - v1.x) * l);
            int r2x = (int) (v2.x + (v3.x - v2.x) * l);
            int r1z = (int) (v1.z + (v3.z - v1.z) * l);
            int r2z = (int) (v2.z + (v3.z - v2.z) * l);
            if (r2x < r1x) {
                int temp = r1x;
                r1x = r2x;
                r2x = temp;
                temp = r1z;
                r1z = r2z;
                r2z = temp;
            }
            float m = (float)Math.tan(Math.atan2(r2z-r1z,r2x-r1x));
            for (int x = r1x; x < r2x; x++) {
                setPixel(x, scanLine, color, r1z+m*x);
            }
        }
    }

    public void drawTriangle(Face3 f) {
        Vector3f v1, v2, v3;
        v1 = f.getP1();
        v2 = f.getP2();
        v3 = f.getP3();


        if ((int) v2.y == (int) v3.y) {
            fillBottomFlatTriangle(v1, v2, v3, f.getColor());
        } else if ((int) v1.y == (int) v2.y) {
            fillTopFlatTriangle(v1, v2, v3, f.getColor());
        } else {
            float l = (v2.y - v1.y) / (v3.y - v1.y);
            float x = (v1.x) + l * (v3.x - v1.x);
            float z = (v1.z) + l * (v3.z - v1.z);
            Vector3f v4 = new Vector3f(x, v2.y, z);
            setPixel(v4, 0xffffff);
            fillBottomFlatTriangle(v1, v2, v4, f.getColor());
            fillTopFlatTriangle(v2, v4, v3, f.getColor());

        }


    }


}