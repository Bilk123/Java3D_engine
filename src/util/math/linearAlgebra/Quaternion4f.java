package util.math.linearAlgebra;

public class Quaternion4f {
    private float w;
    private Vector3f v;

    public Quaternion4f(Vector3f n, float a) {
        a = (float) Math.toRadians(a);
        w = (float) Math.cos(a / 2);
        v = n.mul((float) Math.sin(a / 2));
    }

    public Quaternion4f() {
        w = 0;
        v = new Vector3f();
    }

    public float len2() {
        return w * w + v.dot(v);
    }

    public float len() {
        return (float) Math.sqrt(len2());
    }

    public Quaternion4f normailise() {
        float len = len();
        w /= len;
        v = v.mul(1 / len);
        return this;
    }

    public Quaternion4f mul(Quaternion4f q) {
        /*(Wa*Wb - Vb*Va, Wb * Va + Wa * Vb + Va x Vb)*/
        Quaternion4f q1 = new Quaternion4f();
        q1.w = w*q.w-v.dot(q.v);
        q1.v = ((v.mul(q.w)).add(q.v.mul(w))).add(v.cross(q.v));
        return q1;
    }

    public Vector3f mul(Vector3f V) {
        Quaternion4f p = new Quaternion4f();
        p.w = 0;
        p.v = V;
        Vector3f vcV = v.cross(V);
        return (V.add((vcV.mul(2 * w)))).add(((v.cross(vcV)).mul(2)));
    }

    public Quaternion4f add(Quaternion4f q) {
        return this;
    }

    public Matrix4f toMatrix4F() {
        return new Matrix4f();
    }


    public Quaternion4f inverse() {
        Quaternion4f q = new Quaternion4f();
        q.w = w;
        q.v = v.mul(-1);
        return q;
    }

    @Override
    public String toString() {
        return "[" + w + ", " + v.x + ", " + v.y + ", " + v.z + "]";
    }
}
