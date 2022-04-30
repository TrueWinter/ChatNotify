package dev.truewinter.chatnotify.CommandHandler.ModCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;
import dev.truewinter.chatnotify.Word;

import java.util.*;

public class ModListCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cnm list <player>");
            return;
        }

        getPlugin().getPluginAdapter().getPlayerByName(args[1]).ifPresentOrElse(player -> {
            Set<Word> words = getDatabase().getAllWordsForPlayer(player.getUuid());
            String playerName = player.getUsername();

            Set<String> playerWordsString = new HashSet<>();


            for (Word word : words) {
                if (word.getApproved()) {
                    playerWordsString.add(word.getWord());
                } else {
                    playerWordsString.add(word.getWord() + MessageColor.RED + "*" + MessageColor.GOLD);
                }
            }

            if (playerWordsString.size() != 0) {
                String wordsString = String.join(", ", playerWordsString);
                String singularOrPlural = playerWordsString.size() > 1 ? "words" : "word";

                String output = MessageColor.AQUA + playerName + MessageColor.GREEN + " is currently subscribed to the following " + singularOrPlural + ": " + MessageColor.GOLD + wordsString;
                sender.sendMessage(output);
            } else {
                sender.sendMessage(MessageColor.AQUA + playerName + MessageColor.GREEN + " is not subscribed to any words");
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

        return tabList;
    }
}
