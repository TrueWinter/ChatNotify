package dev.truewinter.chatnotify.spigot;

import dev.truewinter.chatnotify.PluginAdapter.CNConsole;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        CNPlayer sender;
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            sender = new SpigotPlayer(player.getUniqueId(), player.getName());
        } else {
            sender = new CNConsole();
        }

        ChatNotifySpigot.getInstance().getPluginAdapter().onCommand(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        CNPlayer sender;
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            sender = new SpigotPlayer(player.getUniqueId(), player.getName());
        } else {
            sender = new CNConsole();
        }

        return ChatNotifySpigot.getInstance().getPluginAdapter().onTabComplete(sender, label, args);
    }
}
