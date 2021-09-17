package dev.truewinter.chatnotify.CommandHandler.ModCommand;

import dev.truewinter.chatnotify.CommandHandler.Command;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import dev.truewinter.chatnotify.Word;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ModAddCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;

    public ModAddCommand(JavaPlugin plugin, Database db, Notifier notifier) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cnm add <player> <word>");
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        Set<Word> words = new HashSet<>();
        String playerName = "";
        UUID playerUUID = null;

        if (player != null) {
            playerName = player.getName();
            playerUUID = player.getUniqueId();
        } else {
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                if (offlinePlayer.getName().equalsIgnoreCase(args[1])) {
                    playerName = offlinePlayer.getName();
                    playerUUID = offlinePlayer.getUniqueId();
                    break;
                }
            }
        }

        if (playerName.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Player not found. Have they played on this server before?");
            return;
        }

        if (db.playerHasWord(playerUUID, args[2])) {
            sender.sendMessage(ChatColor.RED + "Player is already subscribed to that word");
        } else {
            db.addWord(playerUUID, args[2], true);
            sender.sendMessage(ChatColor.AQUA + playerName + ChatColor.GREEN + " will now be notified when " + ChatColor.GOLD + args[2] + ChatColor.GREEN + " is mentioned in chat");
            notifier.update();
        }
    }

    @Override
    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        List<String> tabList = new ArrayList<>();

        if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                tabList.add(player.getName());
            }
        }

        return tabList;
    }
}
