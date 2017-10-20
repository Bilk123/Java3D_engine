package util.exceptions;

import core.math.SquareMatrix;

public class MatrixInvalidException extends Exception{

    protected float[][]a;

    public MatrixInvalidException(float[][] a){
        this.a=a;
    }

    @Override
    public String toString() {
        return String.format("RectangularMatrix is not rectangular:\n%s\nSquareMatrix must be NxM.", SquareMatrix.writeMatrix(a));
    }
}
