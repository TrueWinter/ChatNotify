package dev.truewinter.chatnotify.CommandHandler.PlayerCommand;

import dev.truewinter.chatnotify.CommandHandler.Command;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import dev.truewinter.chatnotify.Word;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SoundCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;

    public SoundCommand(JavaPlugin plugin, Database db, Notifier notifier) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length != 2) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cn sound <on|off>");
            return;
        }

        if (args[1].equals("on")) {
            db.setPlayerSound(player.getUniqueId(), true);
            player.sendMessage("A sound will play when a subscribed word is mentioned in chat");
        } else if (args[1].equals("off")) {
            db.setPlayerSound(player.getUniqueId(), false);
            player.sendMessage("A sound will no longer play when a subscribed word is mentioned in chat");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cn sound <on|off>");
        }
    }

    @Override
    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        List<String> tabList = new ArrayList<>();

        if (args.length == 2) {
            tabList.add("on");
            tabList.add("off");
        }

        return tabList;
    }
}
