package core.graphics;

import core.Main;
import core.math.Matrix4f;
import core.math.Quaternion4f;
import core.math.Vector3f;

public class Camera {
    private Matrix4f projection;
    private Matrix4f transform;

    private Vector3f translation, scale;
    private Quaternion4f rotation;

    private float pitch = 0, yaw = 0;

    public Camera(Vector3f translation, Vector3f scale, Quaternion4f rotation) {
        projection = Matrix4f.initProjectionMatrix(100, 1, Main.WIDTH, Main.HEIGHT, 60);
        transform = Matrix4f.initTransformMatrix(translation, scale, rotation);
        this.scale = scale;
        this.translation = translation;
        this.rotation = rotation;
        Matrix4f fitToScreenMatrix = Matrix4f.initScreenFitMatrix(Main.WIDTH, Main.HEIGHT, Main.WIDTH, Main.HEIGHT);
        projection = fitToScreenMatrix.mul(projection);
    }

    public void setRotation(Quaternion4f rotation) {
        this.rotation = rotation;
    }

    public void update() {
        Vector3f fw = rotation.inverse().mul(new Vector3f(0,0,-1)).mul(0.1f);
        Vector3f up = rotation.inverse().mul(new Vector3f(0,1,0)).mul(0.1f);
        Vector3f side = rotation.inverse().mul(new Vector3f(1,0,0)).mul(0.1f);
        if (Main.getKey().w) {
            translation = translation.add(fw);
        }
        if (Main.getKey().s) {
            translation = translation.sub(fw);
        }
        if (Main.getKey().a) {
            translation = translation.sub(side);
        }
        if (Main.getKey().d) {
            translation = translation.add(side);
        }
        if (Main.getKey().space) {
            translation = translation.add(up);
        }
        if (Main.getKey().c) {
            translation = translation.sub(up);
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
        rotation = new Quaternion4f(new Vector3f(-1, 0, 0), pitch).mul(new Quaternion4f(new Vector3f(0, -1, 0), yaw));

        transform = Matrix4f.initTransformMatrix(translation, scale, rotation);
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public Matrix4f getProjection() {
        return projection;
    }
}
