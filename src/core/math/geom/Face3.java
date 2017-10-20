package core.math.geom;

import core.math.Vector3f;

public class Face3 {
    private Vector3f p1, p2, p3;
    private int color = 0xffffff;

    public Face3(Vector3f p1, Vector3f p2, Vector3f p3) {
        if ((p1.y <= p2.y && p1.y < p3.y) || (p1.y < p2.y && p1.y <= p3.y)) {
            this.p1 = p1;
            if (p2.y <= p3.y) {
                this.p2 = p2;
                this.p3 = p3;
            } else {
                this.p2 = p3;
                this.p3 = p2;
            }
        } else if ((p2.y <= p1.y && p2.y < p3.y) || (p2.y < p1.y && p2.y <= p3.y)) {
            this.p1 = p2;
            if (p1.y < p3.y) {
                this.p2 = p1;
                this.p3 = p3;
            } else {
                this.p2 = p3;
                this.p3 = p1;
            }
        } else {
            this.p1 = p3;
            if (p1.y < p2.y) {
                this.p2 = p1;
                this.p3 = p2;
            } else {
                this.p2 = p2;
                this.p3 = p1;
            }
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Vector3f calcuateNormal() {
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
}
