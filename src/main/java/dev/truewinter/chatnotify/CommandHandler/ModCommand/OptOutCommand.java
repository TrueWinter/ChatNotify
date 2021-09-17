package dev.truewinter.chatnotify.CommandHandler.ModCommand;

import dev.truewinter.chatnotify.CommandHandler.Command;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class OptOutCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;

    public OptOutCommand(JavaPlugin plugin, Database db, Notifier notifier) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cnm optout");
            return;
        }

        db.setModOptIn(player.getUniqueId(), false);
        player.sendMessage("You will no longer receive ChatNotify mod notifications");
    }

    @Override
    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
