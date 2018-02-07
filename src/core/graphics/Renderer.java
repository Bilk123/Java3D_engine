package core.graphics;

import core.Main;
import core.math.Quaternion4f;
import core.math.Vector2f;
import core.math.Vector3f;
import core.math.geom.Face3;
import core.math.geom.Model;
import util.Util;

import java.util.ArrayList;

public class Renderer {
    public static final float INFINITY = Float.MAX_VALUE;
    public static final int MAX_VERTICES = 100000;
    public static int clearColor = 0x0079aa;

    public int[] pixels;
    public float[] zBuffer;
    public Vector3f[] vertices;
    private int[] indices;
    private Vector3f[] textureCoordinates;
    private ArrayList<Face3> faces;

    private final boolean CULL_BACK_FACES = false;
    private final boolean WIND_CLOCKWISE = true;
    private int width, height;
    private Camera cam;
    private int vOffset = 0;
    private int iOffset = 0;
    private Model test;
    private Texture t;

    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        vertices = new Vector3f[MAX_VERTICES];
        textureCoordinates = new Vector3f[MAX_VERTICES];
        indices = new int[3 * MAX_VERTICES];
        faces = new ArrayList<>();
        pixels = new int[width * height];
        zBuffer = new float[width * height];
        clear();
        cam = new Camera(new Vector3f(0, 0, 10), new Vector3f(1, 1, 1), new Quaternion4f(new Vector3f(0, 1, 0), 0));
        test = new Model("teapot.obj", new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Quaternion4f(new Vector3f(), 0));
        t = new Texture("download.png");
        for (int i = 0; i < test.getVertices().length; i++) {
            textureCoordinates[i]= new Vector3f((float)i/test.getVertices().length, (float)i/test.getVertices().length,(float)i/test.getVertices().length);
        }
        processPoints();

    }

    public void processPoints() {
        iOffset = 0;
        vOffset = 0;
        faces.clear();
        for (int i = 0; i < test.getVertices().length; i++) {
            vertices[i] = cam.getCombined().mul(test.getVertices()[i]);
            vOffset++;
        }
        System.arraycopy(test.getIndices(), 0, indices, 0, test.getIndices().length);
        iOffset += test.getIndices().length;
        for (int i = 0; i < iOffset; i += 3) {
            Face3 f = new Face3(vertices[indices[i]], vertices[indices[i + 1]], vertices[indices[i + 2]]);
            f.setC1(textureCoordinates[indices[i]]);
            f.setC2(textureCoordinates[indices[i + 1]]);
            f.setC3(textureCoordinates[indices[i + 2]]);
            if (!(CULL_BACK_FACES))
                addFace(f);
            else {
                float area = f.getArea();
                if (WIND_CLOCKWISE) {
                    if (area >= 0) addFace(f);
                } else if (area <= 0) addFace(f);
            }
        }
    }

    public void addFace(Face3 f) {
        faces.add(f);
    }


    public Face3[] clipFace(Face3 f) {


        Face3[] clippedFace;
        return null;
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
        for (Face3 f : faces) {
            rasterize(f);
        }
    }

    public void rasterize(Face3 f) {
        int minX = Math.max((int) f.getMinX(), 0);
        int minY = Math.max((int) f.getMinY(), 0);

        int maxX = Math.min((int) f.getMaxX(), Main.WIDTH - 1);
        int maxY = Math.min((int) f.getMaxY(), Main.HEIGHT - 1);
        float area = edgeFunction(f.getP1(), f.getP2(), f.getP3());


        float bias = 8f;
        float bias1 = isTopLeft(f.getP2().toVector2f(), f.getP3().toVector2f()) ? 0 : (area < 0 ? -bias : bias);
        float bias2 = isTopLeft(f.getP3().toVector2f(), f.getP1().toVector2f()) ? 0 : (area < 0 ? -bias : bias);
        float bias3 = isTopLeft(f.getP1().toVector2f(), f.getP2().toVector2f()) ? 0 : (area < 0 ? -bias : bias);

        Vector3f va1 = f.getC1();
        Vector3f va2 = f.getC2();
        Vector3f va3 = f.getC3();
        Vector3f v1 = f.getP1();
        Vector3f v2 = f.getP2();
        Vector3f v3 = f.getP3();


        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                Vector3f p = new Vector3f(x + 0.5f, y + 0.5f, 0);

                float w1 = edgeFunction(v2, v3, p) + bias1;
                float w2 = edgeFunction(v3, v1, p) + bias2;
                float w3 = edgeFunction(v1, v2, p) + bias3;
                if (!CULL_BACK_FACES ?
                        (
                                (w1 >= 0 && w2 >= 0 && w3 >= 0) ||
                                        (w1 <= 0 && w2 <= 0 && w3 <= 0)) :
                        (WIND_CLOCKWISE ?
                                (w1 >= 0 && w2 >= 0 && w3 >= 0) :
                                (w1 <= 0 && w2 <= 0 && w3 <= 0))) {
                    w1 = (w1 - bias1) / area;
                    w2 = (w2 - bias2) / area;
                    w3 = (w3 - bias3) / area;
                    float z = (v1.z * w1 + v2.z * w2 + v3.z * w3);
                    float r = 255 * (va1.x * w1 + va2.x * w2 + va3.x * w3);
                    float g = 255 * (va1.y * w1 + va2.y * w2 + va3.y * w3);
                    float b = 255 * (va1.z * w1 + va2.z * w2 + va3.z * w3);

                    int color = Util.getHashColor((int) r, (int) g, (int) b);
                    setPixel((x), (y), color, z);
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static float edgeFunction(Vector3f a, Vector3f b, Vector3f c) {
        return ((c.x - a.x) * (b.y - a.y) - (c.y - a.y) * (b.x - a.x));
    }

    public static void setClearColor(int color) {
        clearColor = color;
    }

    public static boolean isTopLeft(Vector2f v1, Vector2f v2) {
        return (v2.y == v1.y) && (v2.x - v1.x > 0);
    }
}