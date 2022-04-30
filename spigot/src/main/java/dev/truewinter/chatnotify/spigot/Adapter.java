package dev.truewinter.chatnotify.spigot;

import com.earth2me.essentials.messaging.IMessageRecipient;
import dev.truewinter.chatnotify.PluginAdapter.CNLogger;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;
import dev.truewinter.chatnotify.PluginAdapter.PluginAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Adapter extends PluginAdapter {
    private ChatNotifySpigot plugin = ChatNotifySpigot.getInstance();

    public static Optional<CNPlayer> adaptPlayer(Player player) {
        Player p = Bukkit.getPlayer(player.getUniqueId());

        if (p == null) {
            return Optional.empty();
        }

        return Optional.of(new SpigotPlayer(player));
    }

    public static Optional<CNPlayer> adaptEssentialsPlayer(IMessageRecipient recipient) {
        Player player = Bukkit.getPlayer(recipient.getUUID());

        if (player == null) {
            return Optional.empty();
        }

        return Optional.of(new SpigotPlayer(player.getUniqueId(), player.getName()));
    }

    @Override
    public void sendMessage(List<CNPlayer> players, String message) {
        players.forEach(player -> {
            Player p = Bukkit.getPlayer(player.getUuid());

            if (p == null) {
                return;
            }

            if (!p.isOnline()) {
                return;
            }

            p.sendMessage(message);
        });
    }

    @Override
    public void playSound(CNPlayer player) {
        Player p = Bukkit.getPlayer(player.getUuid());
        if (p == null) return;
        if (!p.isOnline()) return;

        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
    }

    @Override
    public CNLogger getLogger() {
        return plugin.getCNLogger();
    }

    @Override
    public List<CNPlayer> getOnlinePlayers() {
        List<CNPlayer> players = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(new SpigotPlayer(player.getUniqueId(), player.getName()));
        }

        return players;
    }

    @Override
    public Optional<CNPlayer> getPlayerByUuid(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            if (offlinePlayer.getName() == null) {
                return Optional.empty();
            }

            return Optional.of(new SpigotPlayer(offlinePlayer.getUniqueId(), offlinePlayer.getName()));
        }

        return Optional.of(new SpigotPlayer(player.getUniqueId(), player.getName()));
    }

    @Override
    public Optional<CNPlayer> getPlayerByName(String name) {
        Player player = Bukkit.getPlayer(name);

        if (player == null) {
            // TODO: Find non-deprecated method
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);

            if (offlinePlayer.getName() == null) {
                return Optional.empty();
            }

            return Optional.of(new SpigotPlayer(offlinePlayer.getUniqueId(), offlinePlayer.getName()));
        }

        return Optional.of(new SpigotPlayer(player.getUniqueId(), player.getName()));
    }
}
