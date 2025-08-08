package com.beveled.fuckassLag.Commands;

import com.beveled.fuckassLag.Settings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FuckassCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length > 0) {
            if (args[0].equals("reload")) {
                Settings.getInstance().load();
                sender.sendMessage("Reloaded fuckass lag");
                return true;
            } else if (args[0].equals("config")) {
                Map<String, String> options = new LinkedHashMap<>();

                options.put("Cancel Server -> Client packets", stringify(Settings.getInstance().getCancelS2CPackets()));
                options.put("Cancel Client -> Server packets", stringify(Settings.getInstance().getCancelC2SPackets()));
                options.put("Cancel packet mode", Settings.getInstance().getCancelPacketMode().toString());
                options.put("Cancel chance", stringify(Settings.getInstance().getCancelChance()));

                options.put("Delay Server -> Client packets", stringify(Settings.getInstance().getDelayS2CPackets()));
                options.put("Delay Client -> Server packets", stringify(Settings.getInstance().getDelayC2SPackets()));
                options.put("Delay packet mode", Settings.getInstance().getDelayPacketMode().toString());
                options.put("Delay chance", stringify(Settings.getInstance().getDelayChance()));
                options.put("Delay mode", stringify(Settings.getInstance().getDelayMode()));
                options.put("Delay ms", stringify(Settings.getInstance().getDelayMs()));
                options.put("Delay max (ms)", stringify(Settings.getInstance().getMaxDelayMs()));
                options.put("Delay min (ms)", stringify(Settings.getInstance().getMinDelayMs()));

                options.put("Disable warnings", stringify(Settings.getInstance().getDisableWarnings()));
                options.put("Debug", stringify(Settings.getInstance().getDebug()));

                sender.sendMessage(Component.text("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").color(TextColor.color(0, 60, 242)));
                for (Map.Entry<String, String> entry : options.entrySet()) {
                    String option = entry.getKey();
                    String value = entry.getValue();

                    sender.sendMessage(Component.text(String.format("§7%s§8: §7%s", option, value)));
                }
                sender.sendMessage(Component.text("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").color(TextColor.color(0, 60, 242)));

                return true;
            } else return false;
        } else return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of();
    }

    private String stringify(Object obj) {
        if (obj instanceof Boolean) {
            return String.valueOf(obj);
        } else if (obj instanceof Integer) {
            return String.valueOf(obj);
        } else {
            return String.valueOf(obj);
        }
    }
}
