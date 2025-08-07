package com.beveled.fuckassLag;

import java.util.Random;

public class Utils {
    public static int randomInt(int start, int end) {
        Random random = new Random();
        return random.nextInt(start, end);
    }

    public static boolean randomBoolean(int chance) {
        return randomInt(0, 100) <= chance;
    }

    public static int msToTicks(int ms) {
        if (ms < 1) return ms;
        return ms / 50;
    }
}
