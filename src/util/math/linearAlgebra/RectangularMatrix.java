package util.math.linearAlgebra;

import util.exceptions.MatrixInvalidException;
import util.exceptions.MatrixMismatchException;

import java.util.Arrays;

public abstract class RectangularMatrix {
    protected float[][] mat;

    protected int columns, rows;

    public RectangularMatrix(int columns, int rows) {
        mat = initZero(columns, rows);
        this.columns =columns;
        this.rows=rows;
    }

    public RectangularMatrix(float[][] mat) {
        rows = mat.length;
        columns = mat[0].length;
        for (int i = 1; i < rows; i++) {
            if (columns != mat[i].length)
                try {
                    throw new MatrixInvalidException(mat);
                } catch (MatrixInvalidException e) {
                    e.printStackTrace();
                }
        }
        this.mat = mat;
    }

    public static float[][] initZero(int columns, int rows) {
        float[][] mat = new float[columns][rows];
        return mat;
    }

    public static float[][] add(RectangularMatrix a, RectangularMatrix b) throws MatrixMismatchException {
        if(a.columns != b.columns && a.rows != b.rows){
            throw new MatrixMismatchException(MatrixMismatchException.Function.addition);
        }
        float[][] newMat = new float[a.columns][a.rows];
        for (int y = 0; y < a.rows; a.rows++) {
            for (int x = 0; x < a.columns; x++) {
                newMat[x][y] = a.mat[x][y] + b.mat[x][y];
            }
        }
        return newMat;
    }

    public static float[][] multiply(RectangularMatrix a, RectangularMatrix b) throws MatrixMismatchException {
        if (a.columns != b.rows)
            throw new MatrixMismatchException(MatrixMismatchException.Function.multiplication);

        float[][] matrix = new float[a.rows][b.columns];
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < b.columns; j++) {
                float sum = 0;
                for (int k = 0; k < a.columns; k++)
                    sum += a.mat[i][k] * b.mat[k][j];
                matrix[i][j] = sum;
            }
        }

        return matrix;
    }

    public void setElement(int x, int y, float val) {
        if (!(x >= 0 && x < columns && y >= 0 && y < rows))
            System.out.println("element out of bounds");
        else
            mat[x][y] = val;
    }

    public void setMat(float[][] mat) {
        this.mat = mat;
    }

    public static String writeMatrix(float[][] mat) {
        String string = "";
        for (float[] aMat : mat) {
            string += Arrays.toString(aMat) + "\n";
        }
        return string;
    }

    @Override
    public String toString() {
        return writeMatrix(mat);
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}
