package dev.truewinter.chatnotify.CommandHandler.ModCommand;

import dev.truewinter.chatnotify.CommandHandler.Command;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import dev.truewinter.chatnotify.Word;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ModRemoveCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;

    public ModRemoveCommand(JavaPlugin plugin, Database db, Notifier notifier) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cnm remove <player> <word>");
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        String playerName = "";
        UUID playerUUID = null;

        if (player != null) {
            playerName = player.getName();
            playerUUID = player.getUniqueId();
        } else {
            for (Word word : db.getAllWords()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(word.getUUID());
                if (offlinePlayer.getName().equalsIgnoreCase(args[1])) {
                    playerName = offlinePlayer.getName();
                    playerUUID = word.getUUID();
                    break;
                }
            }
        }

        if (playerName.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Player is not online and is not in database");
            return;
        }

        if (db.playerHasWord(playerUUID, args[2])) {
            db.removeWord(playerUUID, args[2]);
            sender.sendMessage(ChatColor.AQUA + playerName + ChatColor.GREEN + " will no longer be notified when " + ChatColor.GOLD + args[2] + ChatColor.GREEN + " is mentioned in chat");
            notifier.update();
        } else {
            sender.sendMessage(ChatColor.RED + "Player is not subscribed to that word");
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

        if (args.length == 3) {
            Player player = Bukkit.getPlayer(args[1]);

            if (player != null) {
                for (Word word : db.getAllWordsForPlayer(player.getUniqueId())) {
                    tabList.add(word.getWord());
                }
            } else {
                for (Word word : db.getAllWords()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(word.getUUID());
                    if (offlinePlayer.getName().equalsIgnoreCase(args[1])) {
                        tabList.add(word.getWord());
                    }
                }
            }
        }

        return tabList;
    }
}
