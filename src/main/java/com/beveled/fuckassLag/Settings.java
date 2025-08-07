package com.beveled.fuckassLag;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Settings {
    private final static Settings instance = new Settings();

    // File shit
    private File file;
    private YamlConfiguration config;

    // Other shit
    private boolean disableWarnings;

    // Cancel packet shit
    private boolean cancelS2CPackets;
    private boolean cancelC2SPackets;
    private CancelPacketMode cancelPacketMode;
    private int cancelChance;

    public enum CancelPacketMode {RANDOM}

    // Delay packets shit
    private boolean delayS2CPackets;
    private boolean delayC2SPackets;
    private DelayPacketMode delayPacketMode;
    private int packetDelayChance;
    private DelayMode delayMode;
    private int delayMs;
    private int maxDelayMs;
    private int minDelayMs;

    public enum DelayPacketMode {ALL, RANDOM}

    public enum DelayMode {SET, RANDOMSET, RANDOM}

    public void load() {
        file = new File(FuckassLag.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) FuckassLag.getInstance().saveResource("config.yml", false);

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        disableWarnings = Boolean.parseBoolean(config.getString("disable-warnings"));

        cancelS2CPackets = Boolean.parseBoolean(config.getString("cancel-packets.S2C"));
        cancelC2SPackets = Boolean.parseBoolean(config.getString("cancel-packets.C2S"));
        cancelPacketMode = parseCancelPacketMode(config.getString("cancel-packets.packet-mode"));
        cancelChance = config.getInt("cancel-packets.cancel-chance");

        delayS2CPackets = Boolean.parseBoolean(config.getString("delay-packets.S2C"));
        delayC2SPackets = Boolean.parseBoolean(config.getString("delay-packets.C2S"));
        delayPacketMode = parseDelayPacketMode(config.getString("delay-packets.packet-mode"));
        packetDelayChance = config.getInt("delay-packets.delay-chance");
        delayMode = parseDelayMode(config.getString("delay-packets.delay-mode"));
        delayMs = config.getInt("delay-packets.delay-ms");
        maxDelayMs = config.getInt("delay-packets.delay-max");
        minDelayMs = config.getInt("delay-packets.delay-min");
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);

        save();
    }

    // Other
    public boolean getDisableWarnings() {
        return disableWarnings;
    }

    // Cancel shit
    public boolean getCancelS2CPackets() {
        return cancelS2CPackets;
    }

    public boolean getCancelC2SPackets() {
        return cancelC2SPackets;
    }

    public CancelPacketMode getCancelPacketMode() {
        return cancelPacketMode;
    }

    public int getCancelChance() {
        return cancelChance;
    }

    // Delay shit
    public boolean getDelayC2SPackets() {
        return delayC2SPackets;
    }

    public boolean getDelayS2CPackets() {
        return delayS2CPackets;
    }

    public DelayPacketMode getDelayPacketMode() {
        return delayPacketMode;
    }

    public int getPacketDelayChance() {
        return packetDelayChance;
    }

    public int getDelayMs() {
        return delayMs;
    }

    public int getMaxDelayMs() {
        return maxDelayMs;
    }

    public int getMinDelayMs() {
        return minDelayMs;
    }

    public DelayMode getDelayMode() {
        return delayMode;
    }

    // Parsers
    private CancelPacketMode parseCancelPacketMode(String mode) {
        try {
            return CancelPacketMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            FuckassLag.getInstance().getLogger().warning("Invalid cancel packet mode in config: " + mode + ". Defaulting to RANDOM.");
            return CancelPacketMode.RANDOM;
        }
    }

    private DelayPacketMode parseDelayPacketMode(String mode) {
        try {
            return DelayPacketMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            FuckassLag.getInstance().getLogger().warning("Invalid delay packet mode in config: " + mode + ". Defaulting to RANDOM.");
            return DelayPacketMode.RANDOM;
        }
    }

    private DelayMode parseDelayMode(String mode) {
        try {
            return DelayMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            FuckassLag.getInstance().getLogger().warning("Invalid delay packet mode in config: " + mode + ". Defaulting to RANDOM.");
            return DelayMode.RANDOM;
        }
    }

    private Settings() {

    }

    public static Settings getInstance() {
        return instance;
    }
}
