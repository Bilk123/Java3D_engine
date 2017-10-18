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
        cam = new Camera(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Quaternion4f(new Vector3f(0, 1, 0), 0));
        vertices[0] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(1, 0, 4)));
        vertices[1] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(0, 1, 4)));
        vertices[2] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(-1, 0, 4)));
        f = new Face3(vertices[0], vertices[1], vertices[2]);
        f.setColor(0xff0000);
        vertices[3] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(2, 0, 5)));
        vertices[4] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(0, 2, 5)));
        vertices[5] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(-2, 0, 5)));
        f1 = new Face3(vertices[3], vertices[4], vertices[5]);
        f1.setColor(0xff);


        for(int x=0;x<10;x++)
            for(int y=0;y<10;y++)
                for(int z=0;z<10;z++){
                    vertices[6+x+y*10+z*100] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(x-5,y-5,z-5)));
                }
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
            if (depth > 0.0f && depth <= 1.0f)
                zBuffer[x + y * width] = depth;
            else
                zBuffer[x + y * width] = 1.0f;
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
        vertices[0] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(3, 0, 2)));
        vertices[1] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(2, 1, 2)));
        vertices[2] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(1, 0, 2)));
        f = new Face3(vertices[0], vertices[1], vertices[2]);
        f.setColor(0xff0000);
        vertices[3] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(2, 0, 1)));
        vertices[4] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(0, 2, 2)));
        vertices[5] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(-2, 0, 3)));
        System.out.println(vertices[2]+" : "+vertices[5]);
        f1 = new Face3(vertices[3], vertices[4], vertices[5]);
        f1.setColor(0xff);

        for(int x=0;x<10;x++)
            for(int y=0;y<10;y++)
                for(int z=0;z<10;z++){
                    vertices[6+x+y*10+z*100] = cam.getProjection().mul(cam.getTransform().mul(new Vector3f(x-5,y-5,z)));
                }

    }

    public void render() {
        drawTriangle(f1);
        drawTriangle(f);

        for(int x=0;x<10;x++)
            for(int y=0;y<10;y++)
                for(int z=0;z<10;z++){
                    setPixel(vertices[6+x+y*10+z*100],0xffffff);
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
            float r1x = (v1.x + (v2.x - v1.x) * l);
            float r2x = (v1.x + (v3.x - v1.x) * l);
            float r1z = (v1.z + (v2.z - v1.z) * l);
            float r2z = (v1.z + (v3.z - v1.z) * l);
            float grad;
            grad = (float) Math.tan(Math.atan2((r2z - r1z), (r2x - r1x)));
            for (int x = (int) Math.min(r1x, r2x); x < (int) Math.max(r1x, r2x); x++) {
                setPixel(x, scanLine, color, grad * x);
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
            float grad;
            grad = (float) Math.tan(Math.atan2((r2z - r1z), (r2x - r1x)));
            for (int x = Math.min(r1x, r2x); x < Math.max(r1x, r2x); x++) {
                setPixel(x, scanLine, color, x * grad);
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
            fillBottomFlatTriangle(v1, v2, v4, f.getColor());
            fillTopFlatTriangle(v2, v4, v3, f.getColor());

        }


    }


}