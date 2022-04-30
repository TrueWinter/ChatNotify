package dev.truewinter.chatnotify.CommandHandler.ModCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;
import dev.truewinter.chatnotify.Word;

import java.util.*;

public class ModRemoveCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cnm remove <player> <word>");
            return;
        }

        getPlugin().getPluginAdapter().getPlayerByName(args[1]).ifPresentOrElse(player -> {
            String playerName = player.getUsername();
            UUID playerUUID = player.getUuid();

            if (getDatabase().playerHasWord(playerUUID, args[2])) {
                getDatabase().removeWord(playerUUID, args[2]);
                sender.sendMessage(MessageColor.AQUA + playerName + MessageColor.GREEN + " will no longer be notified when " + MessageColor.GOLD + args[2] + MessageColor.GREEN + " is mentioned in chat");
                getNotifier().update();
            } else {
                sender.sendMessage(MessageColor.RED + "Player is not subscribed to that word");
            }
        }, () -> {
            sender.sendMessage(MessageColor.RED + "Player is not online and is not in database");
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

        if (args.length == 3) {
            getPlugin().getPluginAdapter().getPlayerByName(args[1]).ifPresent(player -> {
                for (Word word : getDatabase().getAllWordsForPlayer(player.getUuid())) {
                    tabList.add(word.getWord());
                }
            });
        }

        return tabList;
    }
}
