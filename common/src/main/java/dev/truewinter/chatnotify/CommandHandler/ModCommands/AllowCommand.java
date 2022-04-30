package dev.truewinter.chatnotify.CommandHandler.ModCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;
import dev.truewinter.chatnotify.Word;

import java.util.*;

public class AllowCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cnm allow <player> <word>");
            return;
        }

        getPlugin().getPluginAdapter().getPlayerByName(args[1]).ifPresentOrElse(player -> {
            UUID playerUUID = player.getUuid();

            if (getDatabase().playerHasWord(playerUUID, args[2])) {
                Word word = getDatabase().getWordForPlayer(playerUUID, args[2]);

                if (word.getApproved()) {
                    sender.sendMessage(MessageColor.YELLOW + "That word is not awaiting approval for that player");
                    return;
                }

                getDatabase().setApproved(true, playerUUID, word.getWord());
                sender.sendMessage(MessageColor.GREEN + "Word approved");
                getNotifier().notifyPlayerAboutAllow(word.getUUID(), word.getWord());
                getNotifier().update();
            } else {
                sender.sendMessage("That player is not subscribed to that word");
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

        if (args.length == 3) {
            getPlugin().getPluginAdapter().getPlayerByName(args[1]).ifPresent(player -> {
                for (Word word : getDatabase().getAllWordsForPlayer(player.getUuid())) {
                    if (!word.getApproved()) {
                        tabList.add(word.getWord());
                    }
                }
            });
        }

        return tabList;
    }
}
