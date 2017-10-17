package graphics;

import core.Main;
import util.math.linearAlgebra.Matrix4f;
import util.math.linearAlgebra.Quaternion4f;
import util.math.linearAlgebra.Vector3f;

public class Camera {
    private Matrix4f projection;
    private Matrix4f transform;
    private Matrix4f combined;

    private Vector3f translation, scale;
    private Quaternion4f rotation;

    public Camera(Vector3f translation, Vector3f scale, Quaternion4f rotation) {
        projection = Matrix4f.initProjectionMatrix(5, 1, Main.WIDTH, Main.HEIGHT, 90);
        transform = Matrix4f.initTransformMatrix(translation, scale, rotation);
        combined = projection.mul(transform);
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void update() {
        Vector3f sc = new Vector3f(0.2f,0.2f,0.2f);
        if (Main.getKey().up) {
            translation = translation.add(new Vector3f(0,0,-sc.z));
        }
        if (Main.getKey().down) {
            translation = translation.add(new Vector3f(0, 0, sc.z));
        }
        if (Main.getKey().left) {
            translation = translation.add(new Vector3f(-sc.x, 0, 0));
        }
        if (Main.getKey().right) {
            translation = translation.add(new Vector3f(sc.x, 0, 0));
        }
        if (Main.getKey().space) {
            translation = translation.add(new Vector3f(0, sc.y, 0));
        }
        if (Main.getKey().c) {
            translation = translation.add(new Vector3f(0, -sc.y, 0));
        }
        transform = Matrix4f.initTransformMatrix(translation, scale, rotation);
        combined = projection.mul(transform);
    }

    public Matrix4f getCombined() {
        return combined;
    }

    public Matrix4f getTransform() {
        return transform;
    }
}
