package util.exceptions;


public class MatrixMismatchException extends Exception {
    public enum Function {
        addition("A<nxm> : B<nxm>"), multiplication("A<pxn> : B<nxq>");
        String requirement;

        Function(String requirement){
            this.requirement =requirement;
        }

        public String getRequirement() {
            return requirement;
        }
    }
    private Function f;
    public MatrixMismatchException(Function f) {
        this.f=f;
    }

    @Override
    public String toString() {
        return String.format("RectangularMatrix A's and RectangularMatrix B's dimensions are invalid for %s: %s needed", f, f.getRequirement());
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
