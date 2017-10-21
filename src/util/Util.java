package util;

public class Util {
    private Util() {
    }

    public static int getHashColor(int r, int g, int b) {
        r = clamp(0, 255, r);
        g = clamp(0, 255, g);
        b = clamp(0, 255, b);
        int color;
        color = r << 16;
        color |= g << 8;
        color |= b;
        return color;
    }

    public static int clamp(int lower, int upper, int val) {
        if (val < lower) return lower;
        if (val > upper) return upper;
        return val;
    }

    public static float clamp(float lower, float upper, float val) {
        if (val < lower) return lower;
        if (val > upper) return upper;
        return val;
    }

    public static int returnMiddle(int i1, int i2, int i3) {

        if (i1 > i2) {
            if (i1 < i3) {
                return i1;
            }
        } else if (i2 < i3) return i2;
        return i3;
    }
}
