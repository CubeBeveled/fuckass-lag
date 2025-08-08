package com.beveled.fuckassLag;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import org.bukkit.Bukkit;

import static com.beveled.fuckassLag.Utils.getDelay;
import static com.beveled.fuckassLag.Utils.randomBoolean;

public class C2SPacketListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Settings settings = Settings.getInstance();
        if (settings.getIgnoredPackets().contains(event.getPacketType()) || event.getConnectionState() != ConnectionState.PLAY) {
            if (settings.getDebug())
                FuckassLag.getInstance().getLogger().info("[Received] " + event.getPacketType().toString());
            return;
        }

        if (settings.getCancelC2SPackets()) {
            if (settings.getCancelPacketMode() == Settings.CancelPacketMode.RANDOM) {
                event.setCancelled(randomBoolean(settings.getCancelChance()));
                if (event.isCancelled() && settings.getDebug())
                    FuckassLag.getInstance().getLogger().info("[Received] [Cancelled] " + event.getPacketType().toString());
            }
        }

        if (!event.isCancelled() && settings.getDelayC2SPackets()) {
            int delay = getDelay();
            if (delay > 0) {
                PacketReceiveEvent cloned = event.clone();
                event.setCancelled(true);

                Bukkit.getScheduler().runTaskLater(FuckassLag.getInstance(), () -> {
                    event.getUser().sendPacket(cloned.getByteBuf());
                    if (settings.getDebug())
                        FuckassLag.getInstance().getLogger().info(String.format("[Received] [Delayed %s ticks] %s", delay, event.getPacketType().toString()));
                }, delay);
            }
        }
    }
}
