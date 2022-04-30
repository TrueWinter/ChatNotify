package dev.truewinter.chatnotify.spigot;

import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotPlayer extends CNPlayer {
    public SpigotPlayer(Player player) {
        super(player.getUniqueId(), player.getName());
    }

    public SpigotPlayer(UUID uuid, String username) {
        super(uuid, username);
    }

    @Override
    public boolean isOnline() {
        Player player = Bukkit.getPlayer(getUuid());

        if (player == null) {
            return false;
        }

        return player.isOnline();
    }

    public boolean hasPermission(String permission) {
        Player player = Bukkit.getPlayer(getUuid());

        if (player == null) {
            return false;
        }

        return player.hasPermission(permission);
    }
}
