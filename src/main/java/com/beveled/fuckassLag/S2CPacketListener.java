package com.beveled.fuckassLag;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import org.bukkit.Bukkit;

import static com.beveled.fuckassLag.Utils.getDelay;
import static com.beveled.fuckassLag.Utils.randomBoolean;

public class S2CPacketListener implements PacketListener {
    @Override
    public void onPacketSend(PacketSendEvent event) {
        Settings settings = Settings.getInstance();
        if (settings.getIgnoredPackets().contains(event.getPacketType()) || event.getConnectionState() != ConnectionState.PLAY) {
            if (settings.getDebug())
                FuckassLag.getInstance().getLogger().info("[Sent] " + event.getPacketType().toString());
            return;
        }

        if (settings.getCancelS2CPackets()) {
            if (settings.getCancelPacketMode() == Settings.CancelPacketMode.RANDOM) {
                event.setCancelled(randomBoolean(Settings.getInstance().getCancelChance()));
                if (event.isCancelled() && settings.getDebug())
                    FuckassLag.getInstance().getLogger().info("[Sent] [Cancelled] " + event.getPacketType().toString());
            }
        }

        if (!event.isCancelled() && settings.getDelayS2CPackets()) {
            int delay = getDelay();
            if (delay < 1) {
                PacketSendEvent cloned = event.clone();
                event.setCancelled(true);

                Bukkit.getScheduler().runTaskLater(FuckassLag.getInstance(), () -> {
                    event.getUser().sendPacket(cloned.getByteBuf());
                    if (settings.getDebug())
                        FuckassLag.getInstance().getLogger().info(String.format("[Sent] [Delayed %s ticks] %s", delay, event.getPacketType().toString()));
                }, delay);
            }
        }
    }
}
