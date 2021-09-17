package dev.truewinter.chatnotify.CommandHandler.AdminCommands;

import dev.truewinter.chatnotify.CommandHandler.Command;
import dev.truewinter.chatnotify.Config;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;
    Config config = null;

    public ReloadCommand(JavaPlugin plugin, Database db, Notifier notifier, Config config) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
        this.config = config;
    }

    public void handleCommand(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cnm reload");
            return;
        }

        config.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Config reloaded");
    }

    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
