package core.math;

import util.exceptions.MatrixMismatchException;

public class Matrix4f extends SquareMatrix {

    public static final Matrix4f IDENTITY4 = new Matrix4f();

    public Matrix4f() {
        super(4);
    }

    public Matrix4f(float[][] mat) {
        super(mat);
    }

    public Matrix4f add(Matrix4f m) {
        try {
            return new Matrix4f(SquareMatrix.add(this, m));
        } catch (MatrixMismatchException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Matrix4f mul(Matrix4f m) {
        try {
            return new Matrix4f(SquareMatrix.multiply(this, m));
        } catch (MatrixMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Vector3f mul(Vector3f vec) {
        float a, b, c, w;
        a = vec.x * mat[0][0] + vec.y * mat[0][1] + vec.z * mat[0][2] + mat[0][3];
        b = vec.x * mat[1][0] + vec.y * mat[1][1] + vec.z * mat[1][2] + mat[1][3];
        c = vec.x * mat[2][0] + vec.y * mat[2][1] + vec.z * mat[2][2] + mat[2][3];
        w = vec.x * mat[3][0] + vec.y * mat[3][1] + vec.z * mat[3][2] + mat[3][3];
        return new Vector3f(a / w, b / w, c / w);
    }

    public Vector3f clipMul(Vector3f vec) {
        float a, b, c, w, z;
        z = vec.z < 0 ? 0 : vec.z;
        a = vec.x * mat[0][0] + vec.y * mat[0][1] + z * mat[0][2] + mat[0][3];
        b = vec.x * mat[1][0] + vec.y * mat[1][1] + z * mat[1][2] + mat[1][3];
        c = vec.x * mat[2][0] + vec.y * mat[2][1] + z * mat[2][2] + mat[2][3];
        w = vec.x * mat[3][0] + vec.y * mat[3][1] + z * mat[3][2] + mat[3][3];
        return new Vector3f(a / w, b / w, c / w);
    }

    public Matrix4f transpose() {
        return new Matrix4f(SquareMatrix.transpose(this));
    }

    public float determinant() {

        return SquareMatrix.determinant(mat);

    }

    public Matrix4f inverse() {

        return new Matrix4f(SquareMatrix.inverse(mat));

    }

    public static Matrix4f initProjectionMatrix(float f, float n, float width, float height, float fov) {
        float t = (float) Math.tan(Math.toRadians(fov / 2)) * n;
        float b = -t;
        float ar = width / height;
        float r = t * (ar);
        float l = -t * (ar);
        return new Matrix4f(new float[][]
                {
                        {2 * n / (r - l), 0, 0, 0},
                        {0, -2 * n / (t - b), 0, 0},
                        {(r + l) / (r - l), (t + b) / (t - b), -(f + n) / (f - n),-1},
                        {0, 0, -2 * f * n / (f - n), 0}
                });
    }

    public static Matrix4f initTranslationMatrix(Vector3f translation) {
        return new Matrix4f(new float[][]
                {
                        {1, 0, 0, translation.x},
                        {0, 1, 0, translation.y},
                        {0, 0, 1, translation.z},
                        {0, 0, 0, 1}
                });
    }

    public static Matrix4f initScaleMatrix(Vector3f scaleVector) {
        return new Matrix4f(new float[][]
                {{scaleVector.x, 0, 0, 0},
                        {0, scaleVector.y, 0, 0},
                        {0, 0, scaleVector.z, 0},
                        {0, 0, 0, 1}});
    }

    public static Matrix4f initRotationMatrix(Quaternion4f rotation) {
        return rotation.toMatrix4F();
    }

    public static Matrix4f initTransformMatrix(Vector3f translation, Vector3f scale, Quaternion4f rotation) {
        Matrix4f S = initScaleMatrix(scale);
        Matrix4f R = initRotationMatrix(rotation);
        Matrix4f T = initTranslationMatrix(translation);
        return S.mul(R).mul(T);
    }

    public static Matrix4f initRasterMatrix(float sx, float sy) {
        return new Matrix4f(new float[][]{
                {sx, 0, sx, 0},
                {0, sy, sy, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }
}
