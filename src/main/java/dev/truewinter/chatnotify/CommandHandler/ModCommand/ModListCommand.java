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

public class ModListCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;

    public ModListCommand(JavaPlugin plugin, Database db, Notifier notifier) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cnm list <player>");
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        Set<Word> words = new HashSet<>();
        String playerName = "";

        if (player != null) {
            words = db.getAllWordsForPlayer(player.getUniqueId());
            playerName = player.getName();
        } else {
            for (Word word : db.getAllWords()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(word.getUUID());
                if (offlinePlayer.getName().equalsIgnoreCase(args[1])) {
                    words.add(word);
                    playerName = offlinePlayer.getName();
                }
            }
        }

        if (playerName.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Player is not online and is not in database");
            return;
        }

        Set<String> playerWordsString = new HashSet<>();


        for (Word word : words) {
            if (word.getApproved()) {
                playerWordsString.add(word.getWord());
            } else {
                playerWordsString.add(word.getWord() + ChatColor.RED + "*" + ChatColor.GOLD);
            }
        }

        if (playerWordsString.size() != 0) {
            String wordsString = StringUtils.join(playerWordsString, ", ");
            String singularOrPlural = playerWordsString.size() > 1 ? "words" : "word";

            String output = ChatColor.AQUA + playerName + ChatColor.GREEN + " is currently subscribed to the following " + singularOrPlural + ": " + ChatColor.GOLD + wordsString;
            sender.sendMessage(output);
        } else {
            sender.sendMessage(ChatColor.AQUA + playerName + ChatColor.GREEN + " is not subscribed to any words");
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
