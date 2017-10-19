package graphics;

import core.Main;
import util.math.linearAlgebra.*;

public class Camera {
    private Matrix4f projection;
    private Matrix4f transform;

    private static final Matrix scaleFix = new Matrix(new float[][]{{10, 10, 10f}});
    private Vector3f translation, scale;
    private Quaternion4f rotation;

    private float pitch=0, yaw=0;

    public Camera(Vector3f translation, Vector3f scale, Quaternion4f rotation) {
        projection = Matrix4f.initProjectionMatrix(100, 1, Main.WIDTH, Main.HEIGHT, 60);
        transform = Matrix4f.initTransformMatrix(translation, scale, rotation);
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scaleFix.mul(scale);
        Matrix4f fitToScreenMatrix = Matrix4f.initScreenFitMatrix(Main.WIDTH, Main.HEIGHT, Main.WIDTH, Main.HEIGHT);
        projection = fitToScreenMatrix.mul(projection);
    }

    public void setRotation(Quaternion4f rotation) {
        this.rotation = rotation;
    }

    public void update() {
        Vector3f sc = (scaleFix.mul(new Vector3f(1f, 1f, 1f).mul(0.01f)));
        if (Main.getKey().w) {
            translation = translation.add(new Vector3f(0, 0, -sc.z));
        }
        if (Main.getKey().s) {
            translation = translation.add(new Vector3f(0, 0, sc.z));
        }
        if (Main.getKey().a) {
            translation = translation.add(new Vector3f(-sc.x, 0, 0));
        }
        if (Main.getKey().d) {
            translation = translation.add(new Vector3f(sc.x, 0, 0));
        }
        if (Main.getKey().space) {
            translation = translation.add(new Vector3f(0, sc.y, 0));
        }
        if (Main.getKey().c) {
            translation = translation.add(new Vector3f(0, -sc.y, 0));
        }
        if (Main.getKey().left) {
            yaw++;
        }
        if (Main.getKey().right) {
           yaw--;
        }
        if (Main.getKey().up) {
            pitch++;
        }

        if (Main.getKey().down) {
           pitch--;
        }
        rotation = new Quaternion4f(new Vector3f(-1,0,0),pitch).mul(new Quaternion4f(new Vector3f(0,-1,0),yaw));

        transform = Matrix4f.initTransformMatrix(translation, scale, rotation);
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public Matrix4f getProjection() {
        return projection;
    }
}
