package Utils;

import java.util.Random;

public class RandomUtils {
    private static Random rnd = new Random();
    public static int getRandomMilliseconds() {
        int min = 10000;
        int max = 15000;
        return rnd.nextInt(max - min) + min;
    }

    public static int getRandomMilliseconds(int minValue, int maxValue) {
        return rnd.nextInt(maxValue - minValue) + minValue;
    }

    public static int getRandomValue(int maximumValue) {
        return rnd.nextInt(maximumValue);
    }
}
