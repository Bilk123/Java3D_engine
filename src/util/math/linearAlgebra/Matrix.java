package util.math.linearAlgebra;

import util.exceptions.MatrixMismatchException;

public class Matrix extends RectangularMatrix{
    public Matrix(int columns, int rows) {
        super(columns, rows);
    }

    public Matrix(float[][] mat){
        super(mat);
    }

    public Matrix add(Matrix m){
        try {
            return new Matrix(RectangularMatrix.add(this,m));
        } catch (MatrixMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Matrix mul(Matrix m){
        try {
            return new Matrix(RectangularMatrix.multiply(this, m));
        } catch (MatrixMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Vector3f mul(Vector3f vec){
        try {
            return RectangularMatrix.multiply(vec,this);
        } catch (MatrixMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

}
