package dev.truewinter.chatnotify.CommandHandler;

import dev.truewinter.chatnotify.*;
import dev.truewinter.chatnotify.PluginAdapter.CNConsole;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

import java.io.IOException;
import java.util.*;

public class CommandHandler {
    private final ChatNotify plugin = ChatNotify.getInstance();
    private final Map<String, AbstractCommandHandler> commandHandlers = new HashMap<>();

    public CommandHandler() {
        registerCommandHandler("cn", new PlayerCommandHandler());
        registerCommandHandler("cnm", new ModCommandHandler());
    }

    public void onCommand(CNPlayer sender, String cmd, String[] args) {
        if (sender instanceof CNConsole) {
            plugin.getPluginAdapter().getLogger().warn("Only players can use these commands");
            return;
        }

        if (args.length == 0) {
            try {
                sender.sendMessage("ChatNotify v" + plugin.getVersion() + " by TrueWinter\n/cn is used for player commands, and /cnm is used for mod commands");
            } catch(IOException e) {
                sender.sendMessage(MessageColor.RED + "Failed to get common API version");
                plugin.getPluginAdapter().getLogger().error("Failed to get common API version", e);
            }
            return;
        }

        if (!commandHandlers.containsKey(cmd)) return;

        if (!commandHandlers.get(cmd).hasCommand(args[0])) {
            sender.sendMessage(MessageColor.RED + "Invalid command");
            return;
        }

        if (!hasPerms(args[0], cmd, sender)) {
            sender.sendMessage(MessageColor.RED + "You do not have permission to run this command");
            return;
        }

        commandHandlers.get(cmd).addRelevantDBEntry(sender);
        commandHandlers.get(cmd).getCommand(args[0]).handleCommand(sender, args);
    }

    public List<String> onTabComplete(CNPlayer sender, String cmd, String[] args) {
        List<String> tabComplete = new ArrayList<>();

        if (sender instanceof CNConsole) return tabComplete;

        for (String tabListItem : handleTabComplete(sender, cmd, args)) {
            if (tabListItem.startsWith(args[args.length - 1])) {
                tabComplete.add(tabListItem);
            }
        }

        return tabComplete;
    }

    private List<String> handleTabComplete(CNPlayer sender, String cmd, String[] args) {
        List<String> tempList = new ArrayList<>();
        if (!commandHandlers.containsKey(cmd)) return tempList;

        if (args.length < 2) {
            for (String command : commandHandlers.get(cmd).getCommands().keySet()) {
                if (hasPerms(command, cmd, sender)) {
                    tempList.add(command);
                }
            }
        } else if (commandHandlers.get(cmd).hasCommand(args[0])) {
            if (hasPerms(args[0], cmd, sender)) {
                return commandHandlers.get(cmd).getCommand(args[0]).handleTabComplete(sender, args);
            }
        }

        return tempList;
    }

    private void registerCommandHandler(String command, AbstractCommandHandler commandHandler) {
        commandHandlers.put(command, commandHandler);
    }

    private boolean hasPerms(String command, String label, CNPlayer player) {
        return player.hasPermission(commandHandlers.get(label).getCommand(command).getPermission());
    }
}
