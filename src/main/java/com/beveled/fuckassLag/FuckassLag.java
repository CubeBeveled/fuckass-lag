package com.beveled.fuckassLag;

import com.beveled.fuckassLag.Commands.FuckassCommand;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;

public final class FuckassLag extends JavaPlugin {
    private ProtocolManager protocolManager;

    @Override
    public void onLoad() {
        Settings.getInstance().load();
        protocolManager = ProtocolLibrary.getProtocolManager();

        if (Settings.getInstance().getCancelS2CPackets() || Settings.getInstance().getDelayS2CPackets()) {
            protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.LOWEST, PacketType.Play.Server.getInstance().values()) {
                @Override
                public void onPacketSending(PacketEvent event) {
                    if (Settings.getInstance().getCancelS2CPackets()) {
                        if (Settings.getInstance().getCancelPacketMode() == Settings.CancelPacketMode.RANDOM) {
                            event.setCancelled(randomBoolean(Settings.getInstance().getCancelChance()));
                        }
                    }

                    if (!event.isCancelled() && Settings.getInstance().getDelayS2CPackets()) {
                        int delay = getDelay();

                        PacketContainer cloned = event.getPacket().shallowClone();

                        event.setCancelled(true);

                        Bukkit.getScheduler().runTaskLater(FuckassLag.this, () -> {
                            protocolManager.sendServerPacket(event.getPlayer(), cloned);
                        }, delay);
                    }

                    if (!event.isCancelled()) super.onPacketSending(event);
                }
            });
        }

        if (Settings.getInstance().getCancelC2SPackets() || Settings.getInstance().getDelayC2SPackets()) {
            protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.LOWEST, PacketType.Play.Client.getInstance().values()) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    if (Settings.getInstance().getCancelC2SPackets()) {
                        if (Settings.getInstance().getCancelPacketMode() == Settings.CancelPacketMode.RANDOM) {
                            event.setCancelled(randomBoolean(Settings.getInstance().getCancelChance()));
                        }
                    }

                    if (!event.isCancelled() && Settings.getInstance().getDelayC2SPackets()) {
                        int delay = getDelay();
                        PacketContainer cloned = event.getPacket().shallowClone();

                        event.setCancelled(true);

                        Bukkit.getScheduler().runTaskLater(FuckassLag.this, () -> {
                            protocolManager.receiveClientPacket(event.getPlayer(), cloned);
                        }, delay);
                    }

                    if (!event.isCancelled()) super.onPacketReceiving(event);
                }
            });
        }
    }

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("fuckasslag")).setExecutor(new FuckassCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static FuckassLag getInstance() {
        return getPlugin(FuckassLag.class);
    }

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
            delayed = randomBoolean(Settings.getInstance().getPacketDelayChance());
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