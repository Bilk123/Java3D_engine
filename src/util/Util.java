package util;

public class Util {
    public static int getHash(int r, int g, int b) {
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
}
