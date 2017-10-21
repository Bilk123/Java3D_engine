package core;

/**
 * Created by Blake on 14/09/2017.
 */
public class Time {
    public static final int SECOND = 1000000000;//1 second in nano-seconds
    private static double deltaTime = 0;//the time to update and render a frame

    public static long getTime() {
        return System.nanoTime();
    }

    public static double getDeltaTime() {
        return deltaTime;
    }

    public static void setDeltaTime(double deltaTime) {
        Time.deltaTime = deltaTime;
        //System.out.println("core.Time.setDeltaTime: " + deltaTime);
    }
}
