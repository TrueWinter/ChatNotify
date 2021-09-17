package dev.truewinter.chatnotify.CommandHandler;

import dev.truewinter.chatnotify.CommandHandler.AdminCommands.ReloadCommand;
import dev.truewinter.chatnotify.CommandHandler.ModCommand.*;
import dev.truewinter.chatnotify.CommandHandler.PlayerCommand.*;
import dev.truewinter.chatnotify.CommandHandler.PlayerCommand.ListCommand;
import dev.truewinter.chatnotify.Config;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler implements CommandExecutor, TabCompleter {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;
    Config config = null;

    Map<String, dev.truewinter.chatnotify.CommandHandler.Command> playerCommands = new HashMap<>();
    Map<String, String> playerCommandPerms = new HashMap<>();
    List<String> playerCommandList = new ArrayList<>();
    Map<String, dev.truewinter.chatnotify.CommandHandler.Command> modCommands = new HashMap<>();
    Map<String, String> modCommandPerms = new HashMap<>();
    List<String> modCommandList = new ArrayList<>();

    public CommandHandler(JavaPlugin plugin, Database db, Notifier notifier, Config config) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
        this.config = config;

        registerCommand("add", "chatnotify.commands.add", "cn", new AddCommand(plugin, db, notifier, config));
        registerCommand("remove", "chatnotify.commands.remove", "cn", new RemoveCommand(plugin, db, notifier));
        registerCommand("list", "chatnotify.commands.list", "cn", new ListCommand(plugin, db, notifier));
        registerCommand("on", "chatnotify.commands.on", "cn", new OnCommand(plugin, db, notifier));
        registerCommand("off", "chatnotify.commands.off", "cn", new OffCommand(plugin, db, notifier));
        registerCommand("sound", "chatnotify.commands.sound", "cn", new SoundCommand(plugin, db, notifier));

        registerCommand("queue", "chatnotify.commands.mod.queue", "cnm", new QueueCommand(plugin, db, notifier));
        registerCommand("optin", "chatnotify.commands.mod.optin", "cnm", new OptInCommand(plugin, db ,notifier));
        registerCommand("optout", "chatnotify.commands.mod.optout", "cnm", new OptOutCommand(plugin, db, notifier));
        registerCommand("allow", "chatnotify.commands.mod.allow", "cnm", new AllowCommand(plugin, db, notifier));
        registerCommand("deny", "chatnotify.commands.mod.deny", "cnm", new DenyCommand(plugin, db, notifier));
        registerCommand("list", "chatnotify.commands.mod.list", "cnm", new ModListCommand(plugin, db, notifier));
        registerCommand("add", "chatnotify.commands.mod.add", "cnm", new ModAddCommand(plugin, db, notifier));
        registerCommand("remove", "chatnotify.commands.mod.remove", "cnm", new ModRemoveCommand(plugin, db, notifier));

        registerCommand("reload", "chatnotify.commands.admin.reload", "cnm", new ReloadCommand(plugin, db, notifier, config));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                sender.sendMessage("ChatNotify v" + plugin.getDescription().getVersion() + " by TrueWinter\n/cn is used for player commands, and /cnm is used for mod commands");
                return true;
            }

            if (!isCommand(args[0], label)) {
                sender.sendMessage(ChatColor.RED + "Invalid command");
                return true;
            }

            if (!hasPerms(args[0], label, player)) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to run this command");
                return true;
            }

            addRelevantDBEntry(player, label);
            getCommand(args[0], label).handleCommand(sender, args);
        } else {
            sender.sendMessage("Only players can use these commands");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabComplete = new ArrayList<>();

        for (String tabListItem : handleTabComplete(sender, cmd, label, args)) {
            if (tabListItem.startsWith(args[args.length - 1])) {
                tabComplete.add(tabListItem);
            }
        }

        return tabComplete;
    }

    private List<String> handleTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tempList = new ArrayList<>();
        if (label.equals("cn")) {
            if (args.length < 2) {
                for (String temp : playerCommandList) {
                    if (sender.hasPermission(playerCommandPerms.get(temp))) {
                        tempList.add(temp);
                    }
                }
            } else if (isCommand(args[0], label)) {
                return getCommand(args[0], label).handleTabComplete(sender, args);
            }
        } else if (label.equals("cnm")) {
            if (args.length < 2) {
                for (String temp : modCommandList) {
                    if (sender.hasPermission(modCommandPerms.get(temp))) {
                        tempList.add(temp);
                    }
                }
            } else if (isCommand(args[0], label)) {
                return getCommand(args[0], label).handleTabComplete(sender, args);
            }
        }

        return tempList;
    }

    private void registerCommand(String command, String perm, String label, dev.truewinter.chatnotify.CommandHandler.Command classObject) {
        if (label.equals("cn")) {
            playerCommandPerms.put(command, perm);
            playerCommands.put(command, classObject);
            playerCommandList.add(command);
        } else if (label.equals("cnm")) {
            modCommandPerms.put(command, perm);
            modCommands.put(command, classObject);
            modCommandList.add(command);
        }
    }

    private boolean isCommand(String command, String label) {
        if (label.equals("cn")) {
            return playerCommands.containsKey(command) &&
                    playerCommandPerms.containsKey(command) &&
                    playerCommandList.contains(command);
        } else if (label.equals("cnm")) {
            return modCommands.containsKey(command) &&
                    modCommandPerms.containsKey(command) &&
                    modCommandList.contains(command);
        }

        return false;
    }

    private boolean hasPerms(String command, String label, Player player) {
        if (label.equals("cn")) {
            String requiredPerm = playerCommandPerms.get(command);
            return player.hasPermission(requiredPerm);
        } else if (label.equals("cnm")) {
            String requiredPerm = modCommandPerms.get(command);
            return player.hasPermission(requiredPerm);
        }

        return false;
    }

    private dev.truewinter.chatnotify.CommandHandler.Command getCommand(String command, String label) {
        if (label.equals("cn")) {
            return playerCommands.get(command);
        } else if (label.equals("cnm")) {
            return modCommands.get(command);
        }

        return null;
    }

    private void addRelevantDBEntry(Player player, String label) {
        if (label.equals("cn")) {
            if (!db.playerExistsInDB(player.getUniqueId())) {
                db.addPlayer(player.getUniqueId());
            }
        } else if (label.equals("cnm")) {
            if (!db.modExistsInDB(player.getUniqueId())) {
                db.addMod(player.getUniqueId());
            }
        }
    }
}
