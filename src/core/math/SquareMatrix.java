package core.math;

import util.exceptions.MatrixNotSquareException;

@SuppressWarnings({"WeakerAccess"})
public abstract class SquareMatrix extends RectangularMatrix {

    protected SquareMatrix(int size) {
        super(size,size);
        mat = initIdentity(size);
    }

    protected SquareMatrix(float[][] mat) {
        super(mat);
        if(columns!=rows) try {
            throw new MatrixNotSquareException(mat);
        } catch (MatrixNotSquareException e) {
            e.printStackTrace();
        }

    }

    public static float[][] initIdentity(int n) {
        float[][] mat = new float[n][n];
        for (int i = 0; i < n; i++) {
            mat[i][i] = 1;
        }
        return mat;
    }

    protected static float[][] transpose(SquareMatrix a) {
        int n = a.getRows();
        float[][] newMatrix = new float[n][n];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                newMatrix[x][y] = a.mat[y][x];
            }
        }
        return newMatrix;
    }

    protected static float determinant(float[][] a){

        if (a.length == 2)
            return a[0][0] * a[1][1] - a[0][1] * a[1][0];
        float det = 0;
        for (int i = 0; i < a[0].length; i++)
            det += Math.pow(-1, i) * a[0][i]
                    * determinant(minor(a, 0, i));
        return det;
    }

    protected static float[][] minor(float[][] a, int row, int column) {
        float[][] minor = new float[a.length - 1][a.length - 1];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; i != row && j < a[i].length; j++)
                if (j != column)
                    minor[i < row ? i : i - 1][j < column ? j : j - 1] = a[i][j];
        return minor;
    }

    public static float[][] inverse(float[][] a)  {
        float[][] inverse = new float[a.length][a.length];

        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[i].length; j++)
                inverse[i][j] = (float) Math.pow(-1, i + j)
                        * determinant(minor(a, i, j));

        float det = 1.0f / determinant(a);
        for (int i = 0; i < inverse.length; i++) {
            for (int j = 0; j <= i; j++) {
                float temp = inverse[i][j];
                inverse[i][j] = inverse[j][i] * det;
                inverse[j][i] = temp * det;
            }
        }
        return inverse;
    }

}
