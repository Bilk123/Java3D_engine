package util.math.linearAlgebra;

import util.exceptions.MatrixMismatchException;

public class Matrix2f extends SquareMatrix {
    public static final Matrix2f IDENTITY2 = new Matrix2f();

    public Matrix2f() {
        super(2);
    }

    public Matrix2f(float[][] mat) {
        super(mat);
    }

    public Matrix2f add(Matrix2f m) {
        try {
            return new Matrix2f(SquareMatrix.add(this, m));
        } catch (MatrixMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Matrix2f mul(Matrix2f m) {
        try {
            return new Matrix2f(SquareMatrix.multiply(this, m));
        } catch (MatrixMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Matrix2f transpose() {
        return new Matrix2f(SquareMatrix.transpose(this));
    }

    public Matrix2f inverse() {
        return new Matrix2f(SquareMatrix.inverse(mat));
    }

    public float determinant() {
        return SquareMatrix.determinant(mat);
    }

}
