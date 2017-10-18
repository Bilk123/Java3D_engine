package graphics;

import core.Main;
import util.math.linearAlgebra.Matrix;
import util.math.linearAlgebra.Matrix4f;
import util.math.linearAlgebra.Quaternion4f;
import util.math.linearAlgebra.Vector3f;

public class Camera {
    private Matrix4f projection;
    private Matrix4f transform;
    private static final Matrix scaleFix = new Matrix(new float[][]{{10,10,2.5f}});
    private Vector3f translation, scale;
    private Quaternion4f rotation;

    public Camera(Vector3f translation, Vector3f scale, Quaternion4f rotation) {
        projection = Matrix4f.initProjectionMatrix(5, 1, Main.WIDTH, Main.HEIGHT, 90);
        transform = Matrix4f.initTransformMatrix(translation, scale, rotation);
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scaleFix.mul(scale);
    }

    public void setRotation(Quaternion4f rotation) {
        this.rotation = rotation;
    }

    public void update() {
        Vector3f sc = scaleFix.mul(new Vector3f(1f,1f,1f).mul(0.1f)).normalise();
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
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public Matrix4f getProjection() {
        return projection;
    }
}
