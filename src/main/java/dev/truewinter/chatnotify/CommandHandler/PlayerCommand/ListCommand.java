package dev.truewinter.chatnotify.CommandHandler.PlayerCommand;

import dev.truewinter.chatnotify.CommandHandler.Command;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import dev.truewinter.chatnotify.Word;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;

    public ListCommand(JavaPlugin plugin, Database db, Notifier notifier) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cn list");
            return;
        }

        Set<String> playerWordsString = new HashSet<>();

        for (Word word : db.getAllWordsForPlayer(player.getUniqueId())) {
            if (word.getApproved()) {
                playerWordsString.add(word.getWord());
            } else {
                playerWordsString.add(word.getWord() + ChatColor.RED + "*" + ChatColor.GOLD);
            }
        }

        if (playerWordsString.size() != 0) {
            String wordsString = StringUtils.join(playerWordsString, ", ");
            String singularOrPlural = playerWordsString.size() > 1 ? "words" : "word";

            String output = ChatColor.GREEN + "You are currently subscribed to the following " + singularOrPlural + ": " + ChatColor.GOLD + wordsString;
            sender.sendMessage(output);
        } else {
            sender.sendMessage(ChatColor.GREEN + "You are not subscribed to any words");
        }
    }

    @Override
    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
