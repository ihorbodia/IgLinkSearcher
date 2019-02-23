package Utils;

import java.util.Random;

public class RandomUtils {
    private static Random rnd = new Random();
    public static int getRandomMilliseconds() {
        return rnd.nextInt(50000-20000) + 20000;
    }

    public static int getRandomValue(int maximumValue) {
        return rnd.nextInt(maximumValue);
    }
}
