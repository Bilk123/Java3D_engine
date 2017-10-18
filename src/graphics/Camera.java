package graphics;

import core.Main;
import util.math.linearAlgebra.*;

public class Camera {
    private Matrix4f projection;
    private Matrix4f transform;

    private static final Matrix scaleFix = new Matrix(new float[][]{{10,10,10f}});
    private Vector3f translation, scale;
    private Quaternion4f rotation;

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

    float a=0;
    public void update() {
        Vector3f sc = (scaleFix.mul(new Vector3f(1f,1f,1f).mul(0.01f)));
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

        if(Main.getKey().e){
            a++;
            rotation = new Quaternion4f(new Vector3f(0,1,0),a);
        }

        if(Main.getKey().q){
            a--;
            rotation = new Quaternion4f(new Vector3f(0,1,0),a);
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
