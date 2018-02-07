package core.graphics;

import core.Main;
import core.math.Matrix;
import core.math.Matrix4f;
import core.math.Quaternion4f;
import core.math.Vector3f;
import core.math.geom.Plane;

public class Camera {
    private Matrix4f projection;
    private Matrix4f transform;
    private Matrix4f combined;

    private Vector3f translation, scale;
    private Quaternion4f rotation;

    private float pitch = 0, yaw = 0;

    private Plane[] frustrum;

    public Camera(Vector3f translation, Vector3f scale, Quaternion4f rotation) {
        float sx = Main.WIDTH;
        float sy = Main.HEIGHT;
        projection = Matrix4f.initProjectionMatrix(100, 1, sx, sy, 60);
        transform = Matrix4f.initTransformMatrix(translation, scale, rotation);
        this.scale = scale;
        this.translation = translation;
        this.rotation = rotation;
        Matrix4f rasterMatrix = Matrix4f.initRasterMatrix(sx, sy, sx, sy);
        projection = rasterMatrix.mul(projection);
        combined = projection.mul(transform);
        frustrum = new Plane[5];
        float angleHorizontal = (float) Math.atan(sx / 2) - 0.0001f;
        float angleVertical = (float) Math.atan(sy / 2) - 0.0001f;
        float sh = (float) Math.sin(angleHorizontal);
        float ch = (float) Math.cos(angleHorizontal);
        float sv = (float) Math.sin(angleVertical);
        float cv = (float) Math.cos(angleVertical);
        //left
        frustrum[0] = new Plane(new Vector3f(ch, 0, sh), 0);

        //right
        frustrum[1] = new Plane(new Vector3f(-ch, 0, sh), 0);

        //top
        frustrum[2] = new Plane(new Vector3f(0, cv, sv), 0);

        //bottom
        frustrum[3] = new Plane(new Vector3f(0, -cv, sv), 0);

        //near
        frustrum[4] = new Plane(new Vector3f(0, 0, 1), -10);

    }

    public void update(double dt) {
        double speed = 0.3;
        Vector3f fw = rotation.inverse().mul(new Vector3f(0, 0, -1)).mul(speed * 1 / dt);
        Vector3f up = rotation.inverse().mul(new Vector3f(0, -1, 0)).mul(speed * 1 / dt);
        Vector3f side = rotation.inverse().mul(new Vector3f(1, 0, 0)).mul(speed * 1 / dt);
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
            yaw -= dt;
        }
        if (Main.getKey().right) {
            yaw += dt;
        }
        if (Main.getKey().up) {
            pitch += dt;
        }
        if (Main.getKey().down) {
            pitch -= dt;
        }
        rotation = new Quaternion4f(new Vector3f(1, 0, 0), pitch).mul(new Quaternion4f(new Vector3f(0, 1, 0), yaw));

        transform = Matrix4f.initTransformMatrix(translation, scale, rotation);
        combined = projection.mul(transform);
    }

    public Matrix4f getCombined() {
        return combined;
    }
}
