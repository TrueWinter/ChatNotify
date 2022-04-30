package dev.truewinter.chatnotify.CommandHandler.PlayerCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;
import dev.truewinter.chatnotify.Word;

import java.util.HashSet;
import java.util.Set;

public class ListCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cn list");
            return;
        }

        Set<String> playerWordsString = new HashSet<>();

        for (Word word : getDatabase().getAllWordsForPlayer(sender.getUuid())) {
            if (word.getApproved()) {
                playerWordsString.add(word.getWord());
            } else {
                playerWordsString.add(word.getWord() + MessageColor.RED + "*" + MessageColor.GOLD);
            }
        }

        if (playerWordsString.size() != 0) {
            String wordsString = String.join(", ", playerWordsString);
            String singularOrPlural = playerWordsString.size() > 1 ? "words" : "word";

            String output = MessageColor.GREEN + "You are currently subscribed to the following " + singularOrPlural + ": " + MessageColor.GOLD + wordsString;
            sender.sendMessage(output);
        } else {
            sender.sendMessage(MessageColor.GREEN + "You are not subscribed to any words");
        }
    }
}
