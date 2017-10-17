package graphics;

import core.Main;
import util.math.geom.Face3;
import util.math.linearAlgebra.*;

import java.util.ArrayList;

public class Renderer {
    public static int clearColor = 0x0;
    private int width, height;
    public int[] pixels;
    public float[] zBuffer;
    private Camera cam;
    private Matrix3f fitToScreenMatrix;
    public Vector3f[] vertices;
    private int[] indices;
    private ArrayList<Face3> faces;

    public int nVertices=0;

    public static final int MAX_VERTICES = 1000;

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
        fitToScreenMatrix = Matrix3f.initScreenFitMatrix(Main.WIDTH/2,Main.HEIGHT/2, Main.WIDTH/2,Main.HEIGHT/2);
        vertices[0] = new Vector3f(0,0,0);
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
        Vector2f screen  = fitToScreenMatrix.mul(new Vector2f(vec.x, vec.y));
        setPixel((int) screen.x, (int) screen.y, color, vec.z);
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
        float step = 1 / 300f;
        for (float i = 0; i < len; i += step) {
            setPixel(c.mul(i).add(vec1), 0xffffff);
        }
    }

    public void fillFace(){

    }

    public void update() {
        cam.update();
        vertices[0] = cam.getCombined().mul(new Vector3f(0,0,1));
        System.out.println(vertices[0].z);
        for(int i = 0;i<MAX_VERTICES;i++){
            if(vertices[i]!=null)
               if(vertices[i].z <= 0){
                vertices[i]=null;
               }
        }

    }
    public void render(){
        for(int i = 0;i<MAX_VERTICES;i++){
            if(vertices[i]!=null)
            setPixel(vertices[i],0xffffff);
        }
    }

    public void addFace(Face3 f){
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

    private void fillBottomFlatTriangle(Vector3f v1, Vector3f v2, Vector3f v3)
    {
        float invslope1 = (v2.x - v1.x) / (v2.y - v1.y);
        float invslope2 = (v3.x - v1.x) / (v3.y - v1.y);

        float curx1 = v1.x;
        float curx2 = v1.x;

        for (float scanlineY = v1.y; scanlineY <= v2.y; scanlineY++)
        {
            //drawLine((int)curx1, scanlineY, (int)curx2, scanlineY);
            curx1 += invslope1;
            curx2 += invslope2;
        }
    }

}