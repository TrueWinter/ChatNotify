package dev.truewinter.chatnotify.CommandHandler.ModCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

import java.util.*;

public class ModAddCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cnm add <player> <word>");
            return;
        }

        getPlugin().getPluginAdapter().getPlayerByName(args[1]).ifPresentOrElse(player -> {
            String playerName = player.getUsername();
            UUID playerUUID = player.getUuid();

            if (getDatabase().playerHasWord(playerUUID, args[2])) {
                sender.sendMessage(MessageColor.RED + "Player is already subscribed to that word");
            } else {
                getDatabase().addWord(playerUUID, args[2], true);
                sender.sendMessage(MessageColor.AQUA + playerName + MessageColor.GREEN + " will now be notified when " + MessageColor.GOLD + args[2] + MessageColor.GREEN + " is mentioned in chat");
                getNotifier().update();
            }
        }, () -> {
            sender.sendMessage(MessageColor.RED + "Player not found. Have they played on this server before?");
        });
    }

    @Override
    public List<String> handleTabComplete(CNPlayer sender, String[] args) {
        List<String> tabList = new ArrayList<>();

        if (args.length == 2) {
            for (CNPlayer player : getPlugin().getPluginAdapter().getOnlinePlayers()) {
                tabList.add(player.getUsername());
            }
        }

        return tabList;
    }
}
