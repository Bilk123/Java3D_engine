package core.math.geom;

import core.graphics.Renderer;
import core.math.Vector2f;
import core.math.Vector3f;

public class Face3 {
    private Vector3f p1, p2, p3;
    private Vector3f c1, c2, c3;
    private int color = 0xffffff;

    public Face3(Vector3f p1, Vector3f p2, Vector3f p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        c1 = new Vector3f(1,0,0);
        c2 = new Vector3f(0,1,0);
        c3 = new Vector3f(0,0,1);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Vector3f normal() {
        return p2.sub(p1).cross(p3.sub(p1));
    }

    @Override
    public String toString() {
        return p1.toString() + ";" + p2.toString() + ";" + p3.toString();
    }

    public int getColor() {
        return color;
    }

    public Vector3f getP1() {
        return p1;
    }

    public Vector3f getP2() {
        return p2;
    }

    public Vector3f getP3() {
        return p3;
    }

    public float getMinX() {
        float minX = p1.x;
        if (minX > p2.x) {
            minX = p2.x;
        }
        if (minX > p3.x) {
            minX = p3.x;
        }
        return minX;
    }

    public float getMaxX() {
        float maxX = p1.x;
        if (maxX < p2.x) {
            maxX = p2.x;
        }
        if (maxX < p3.x) {
            maxX = p3.x;
        }
        return maxX;
    }

    public float getMinY() {
        float minY = p1.y;
        if (minY > p2.y) {
            minY = p2.y;
        }
        if (minY > p3.y) {
            minY = p3.y;
        }
        return minY;
    }

    public float getMaxY() {
        float maxY = p1.y;
        if (maxY < p2.y) {
            maxY = p2.y;
        }
        if (maxY < p3.y) {
            maxY = p3.y;
        }
        return maxY;
    }

    public Vector3f getC1() {
        return c1;
    }

    public Vector3f getC2() {
        return c2;
    }

    public Vector3f getC3() {
        return c3;
    }

    public void setC1(Vector3f c1) {
        this.c1 = c1;
    }

    public void setC2(Vector3f c2) {
        this.c2 = c2;
    }

    public void setC3(Vector3f c3) {
        this.c3 = c3;
    }

    public float getArea() {
        return Renderer.edgeFunction(p1, p2, p3);
    }
}
