package util.math.linearAlgebra;

import util.exceptions.MatrixMismatchException;

public class Matrix3f extends SquareMatrix {

    static {
        size = 3;
    }

    public Matrix3f() {
        super();
    }

    public Matrix3f(float[][] mat) {
        super(mat);
    }

    public Matrix3f add(Matrix3f m) {
        try {
            return new Matrix3f(SquareMatrix.add(this, m));
        } catch (MatrixMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Matrix3f mul(Matrix3f m) {
        try {
            return new Matrix3f(SquareMatrix.multiply(this, m));
        } catch (MatrixMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Matrix3f transpose() {
        return new Matrix3f(SquareMatrix.transpose(this));
    }

    public float determinant() {

        return SquareMatrix.determinant(mat);

    }

    public Matrix3f inverse() {

        return new Matrix3f(SquareMatrix.inverse(mat));

    }

    public static Matrix3f initScreenFitMatrix(float sx, float sy, float tx, float ty) {
        return new Matrix3f(new float[][]{
                {sx, 0, tx},
                {0, sy, ty},
                {0, 0, 1}
        });
    }

    public Vector2f mul(Vector2f v) {
        float x = mat[0][0] * v.x + mat[0][1] * v.y + mat[0][2];
        float y = mat[1][0] * v.x + mat[1][1] * v.y + mat[1][2];
        float z = mat[2][0] * v.x + mat[2][1] * v.y + mat[2][2];
        return new Vector2f(x/z, y/z);
    }
}
