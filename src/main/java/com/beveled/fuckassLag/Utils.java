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

    public static int getDelay() {
        boolean delayed = false;

        if (Settings.getInstance().getDelayPacketMode() == Settings.DelayPacketMode.ALL) {
            delayed = true;
        } else if (Settings.getInstance().getDelayPacketMode() == Settings.DelayPacketMode.RANDOM) {
            delayed = randomBoolean(Settings.getInstance().getDelayChance());
        } else {
            if (!Settings.getInstance().getDisableWarnings())
                FuckassLag.getInstance().getLogger().warning("DelayPacketMode has a value that isn't being handled. Please notify the author about this. Set disable-warnings to true in the config to hide this message.");
        }

        if (delayed) {
            int delayMs = -1;

            if (Settings.getInstance().getDelayMode() == Settings.DelayMode.SET) {
                delayMs = Settings.getInstance().getDelayMs();
            } else if (Settings.getInstance().getDelayMode() == Settings.DelayMode.RANDOMSET) {
                delayMs = Settings.getInstance().getDelayMs() + randomInt(Settings.getInstance().getMinDelayMs(), Settings.getInstance().getMaxDelayMs());
            } else if (Settings.getInstance().getDelayMode() == Settings.DelayMode.RANDOM) {
                delayMs = randomInt(Settings.getInstance().getMinDelayMs(), Settings.getInstance().getMaxDelayMs());
            } else {
                if (!Settings.getInstance().getDisableWarnings())
                    FuckassLag.getInstance().getLogger().warning("DelayMode has a value that isn't being handled. Please notify the author about this. Set disable-warnings to true in the config to hide this message.");
            }

            return msToTicks(delayMs);
        } else {
            return -1;
        }
    }
}
