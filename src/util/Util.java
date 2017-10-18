package util;

import util.math.linearAlgebra.Vector3f;

public class Util {
    private Util() {
    }

    public static int getHashColor(int r, int g, int b) {
        r = clamp(0, 255, r);
        g = clamp(0, 255, g);
        b = clamp(0, 255, b);
        return (r << 8) | (g << 4) | (b);
    }

    public static int clamp(int upper, int lower, int val) {
        if (val < lower) return lower;
        if (val > upper) return upper;
        return val;
    }

    public static float clamp(float upper, float lower, float val) {
        if (val < lower) return lower;
        if (val > upper) return upper;
        return val;
    }

    public static int returnMiddle(int i1, int i2, int i3) {

        if (i1 >i2){
            if (i1 < i3) {
                return i1;
            }
        } else if (i2 < i3) return i2;
        return i3;
    }
}
