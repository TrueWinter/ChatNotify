package dev.truewinter.chatnotify.CommandHandler.PlayerCommand;

import dev.truewinter.chatnotify.CommandHandler.Command;
import dev.truewinter.chatnotify.Config;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class AddCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;
    Config config = null;

    public AddCommand(JavaPlugin plugin, Database db, Notifier notifier, Config config) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
        this.config = config;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length != 2) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cn add <word>" + ChatColor.YELLOW + " (without <>)");
            return;
        }

        String word = args[1].toLowerCase();

        if (db.playerHasWord(player.getUniqueId(), word)) {
            sender.sendMessage(ChatColor.YELLOW + "You're already subscribed to that word");
            return;
        }

        if (word.length() > 20) {
            sender.sendMessage(ChatColor.YELLOW + "The maximum word length is 20 characters");
            return;
        }

        if (player.hasPermission("chatnotify.bypassapproval")) {
            db.addWord(player.getUniqueId(), word, true);
            sender.sendMessage(ChatColor.GREEN + "You will now receive notifications when the word " + ChatColor.GOLD + word + ChatColor.GREEN + " is mentioned in chat");
            notifier.notifyMods(player.getUniqueId(), word, true);
        } else if (config.getRequireApprovalLength() >= word.length()) {
            db.addWord(player.getUniqueId(), word, false);
            sender.sendMessage(ChatColor.DARK_GREEN + "A mod will need to approve this word subscription before you will receive notifications when the word " + ChatColor.GOLD + word + ChatColor.DARK_GREEN + " is mentioned in chat");
            notifier.notifyMods(player.getUniqueId(), word, false);
        } else if (config.getRequireApproval().equals("none")) {
            db.addWord(player.getUniqueId(), word, true);
            sender.sendMessage(ChatColor.GREEN + "You will now receive notifications when the word " + ChatColor.GOLD + word + ChatColor.GREEN + " is mentioned in chat");
            notifier.notifyMods(player.getUniqueId(), word, true);
        } else if (config.getRequireApproval().equals("part")) {
            if (player.getName().toLowerCase().contains(word)) {
                db.addWord(player.getUniqueId(), word, true);
                sender.sendMessage(ChatColor.GREEN + "You will now receive notifications when the word " + ChatColor.GOLD + word + ChatColor.GREEN + " is mentioned in chat");
                notifier.notifyMods(player.getUniqueId(), word, true);
            } else {
                db.addWord(player.getUniqueId(), word, false);
                sender.sendMessage(ChatColor.DARK_GREEN + "A mod will need to approve this word subscription before you will receive notifications when the word " + ChatColor.GOLD + word + ChatColor.DARK_GREEN + " is mentioned in chat");
                notifier.notifyMods(player.getUniqueId(), word, false);
            }
        } else {
            db.addWord(player.getUniqueId(), word, false);
            sender.sendMessage(ChatColor.DARK_GREEN + "A mod will need to approve this word subscription before you will receive notifications when the word " + ChatColor.GOLD + word + ChatColor.DARK_GREEN + " is mentioned in chat");
            notifier.notifyMods(player.getUniqueId(), word, false);
        }

        notifier.update();
    }

    @Override
    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
