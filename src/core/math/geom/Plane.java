package core.math.geom;

import core.math.Vector3f;

public class Plane {
    public float distance;
    public Vector3f normal;

    public Plane(){
        distance=0;
        normal = new Vector3f();
    }

    public Plane(Vector3f normal, float distance) {
        this.distance = distance;
        this.normal = normal.normalise();
    }

    public Plane(Vector3f a, Vector3f b) {
        normal = a.cross(b).normalise();
        distance = normal.dot(a);
    }

    public Plane(Vector3f a, Vector3f b, Vector3f c) {
        normal = (a.sub(c).cross(b.sub(c))).normalise();
        distance = normal.dot(a);
    }
}
