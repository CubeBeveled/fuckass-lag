package com.beveled.fuckassLag;

import com.beveled.fuckassLag.Commands.FuckassCommand;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Set;

public final class FuckassLag extends JavaPlugin {
    @Override
    public void onLoad() {
        this.getLogger().info("Loading settings");
        Settings.getInstance().load();

        this.getLogger().info("Loading packetevents");
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();

        this.getLogger().info("Registering events");
        if (Settings.getInstance().getCancelC2SPackets() || Settings.getInstance().getDelayC2SPackets())
            PacketEvents.getAPI().getEventManager().registerListener(new C2SPacketListener(), PacketListenerPriority.LOWEST);

        if (Settings.getInstance().getCancelS2CPackets() || Settings.getInstance().getDelayS2CPackets())
            PacketEvents.getAPI().getEventManager().registerListener(new S2CPacketListener(), PacketListenerPriority.LOWEST);
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Loading commands");
        Objects.requireNonNull(getCommand("fuckasslag")).setExecutor(new FuckassCommand());
        this.getLogger().info("Initializing packetevents");
        PacketEvents.getAPI().init();
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Terminating packetevents");
        PacketEvents.getAPI().terminate();
    }

    public static FuckassLag getInstance() {
        return getPlugin(FuckassLag.class);
    }
}