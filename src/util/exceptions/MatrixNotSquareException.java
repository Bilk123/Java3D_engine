package util.exceptions;

import util.math.linearAlgebra.SquareMatrix;

public class MatrixNotSquareException extends MatrixInvalidException{

    public MatrixNotSquareException(float[][] a){
        super(a);
    }

    @Override
    public String toString() {
        return String.format("SquareMatrix is not square:\n%s\nMatrixNf must be NxN.", SquareMatrix.writeMatrix(a));
    }
}
