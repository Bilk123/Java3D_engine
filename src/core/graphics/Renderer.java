package core.graphics;

import core.math.Quaternion4f;
import core.math.Vector2f;
import core.math.Vector3f;
import core.math.geom.Face3;
import core.math.geom.Model;
import util.Util;

import java.util.ArrayList;
import java.util.Arrays;

public class Renderer {
    public static final float INFINITY = Float.MAX_VALUE;
    public static final int MAX_VERTICES = 100000;
    public static int clearColor = 0x0;
    private int width, height;
    public int[] pixels;
    public float[] zBuffer;
    private Camera cam;

    public Vector3f[] vertices;
    private Vector3f[] colorAttributes;
    private int[] indices;
    private ArrayList<Face3> faces;
    private int vOffset = 0;
    private int iOffset = 0;
    private Model test;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        vertices = new Vector3f[MAX_VERTICES];
        indices = new int[3 * MAX_VERTICES];
        faces = new ArrayList<>();
        pixels = new int[width * height];
        zBuffer = new float[width * height];
        clear();
        cam = new Camera(new Vector3f(0, 0, 10), new Vector3f(1, 1, 1), new Quaternion4f(new Vector3f(0, 1, 0), 0));
        test = new Model("teapot.obj", new Vector3f(0, 0, 0), new Vector3f(1, -1, 1), new Quaternion4f(new Vector3f(), 0));
        processPoints();


    }


    public void processPoints() {
        iOffset=0;
        vOffset =0;
        for (int i = 0; i < test.getVertices().length; i++) {
            vertices[i] = cam.getProjection().mul(cam.getTransform().mul(test.getVertices()[i]));
            vOffset++;
        }
        for (int i = 0; i < test.getIndices().length; i++) {
            indices[i] = test.getIndices()[i];
            iOffset++;
        }
    }

    public void clear() {
        for (int i = 0; i < width * height; i++) {
            pixels[i] = clearColor;
            zBuffer[i] = INFINITY;
        }
    }

    public void setPixel(int x, int y, int color, float depth) {
        if ((x) >= 0 && (x) < width && (y) >= 0 && (y) < height)
            if (getPixelDepth(x, y) > -depth) {
                pixels[(x) + (y) * width] = color;
                setPixelDepth(x, y, -depth);
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


    public void update(double dt) {
        processPoints();
        cam.update(dt);

    }

    public void render() {
        for(int i =0 ;i<iOffset;i+=3){
            rasterize(new Face3(vertices[indices[i]], vertices[indices[1+i]], vertices[indices[2+i]]));
        }
    }

    public void rasterize(Face3 f) {
        for (float y = f.getMinY() - 1; y < f.getMaxY() + 1; y++) {
            for (float x = f.getMinX() - 1; x < f.getMaxX() + 1; x++) {
                Vector2f p = new Vector2f(x + 0.5f, y + 0.5f);
                float area = edgeFunction(f.getP1().toVector2f(), f.getP2().toVector2f(), f.getP3().toVector2f());
                float w1 = edgeFunction(f.getP2().toVector2f(), f.getP3().toVector2f(), p);
                float w2 = edgeFunction(f.getP3().toVector2f(), f.getP1().toVector2f(), p);
                float w3 = edgeFunction(f.getP1().toVector2f(), f.getP2().toVector2f(), p);
                if ((w1 >= 0 && w2 >= 0 && w3 >= 0)) {
                    w1 /= area;
                    w2 /= area;
                    w3 /= area;
                    float r = 255 * (f.getC1().x * w1 + f.getC2().x * w2 + f.getC3().x * w3);
                    float g = 255 * (f.getC1().y * w1 + f.getC2().y * w2 + f.getC3().y * w3);
                    float b = 255 * (f.getC1().z * w1 + f.getC2().z * w2 + f.getC3().z * w3);
                    int color = Util.getHashColor((int) (r), (int) (g), (int) (b));
                    float z = f.getP1().z * w1 + f.getP2().z * w2 + f.getP3().z * w3;
                    setPixel((int) (x), (int) (y), color, z);
                }
            }
        }
    }

    public static float edgeFunction(Vector2f a, Vector2f b, Vector2f c) {
        return ((c.x - a.x) * (b.y - a.y) - (c.y - a.y) * (b.x - a.x));
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
}