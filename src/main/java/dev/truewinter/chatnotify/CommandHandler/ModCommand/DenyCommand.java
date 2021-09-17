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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DenyCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;

    public DenyCommand(JavaPlugin plugin, Database db, Notifier notifier) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cnm deny <player> <word>");
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
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
            Word word = db.getWordForPlayer(playerUUID, args[2]);

            if (word.getApproved()) {
                sender.sendMessage(ChatColor.YELLOW + "That word is not awaiting approval for that player");
                return;
            }

            db.removeWord(playerUUID, word.getWord());
            sender.sendMessage(ChatColor.GREEN + "Word denied and removed from database");
            notifier.notifyPlayerAboutDeny(word.getUUID(), word.getWord());
            notifier.update();
        } else {
            sender.sendMessage("That player is not subscribed to that word");
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
                    if (!word.getApproved()) {
                        tabList.add(word.getWord());
                    }
                }
            } else {
                for (Word word : db.getAllWords()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(word.getUUID());
                    if (offlinePlayer.getName().equalsIgnoreCase(args[1])) {
                        if (!word.getApproved()) {
                            tabList.add(word.getWord());
                        }
                    }
                }
            }
        }

        return tabList;
    }
}
