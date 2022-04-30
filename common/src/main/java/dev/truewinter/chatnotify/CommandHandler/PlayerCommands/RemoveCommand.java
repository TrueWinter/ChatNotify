package dev.truewinter.chatnotify.CommandHandler.PlayerCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;
import dev.truewinter.chatnotify.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RemoveCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cn remove <word>" + MessageColor.YELLOW + " (without <>)");
            return;
        }

        String word = args[1].toLowerCase();

        if (!getDatabase().playerHasWord(sender.getUuid(), word)) {
            sender.sendMessage(MessageColor.YELLOW + "You're not subscribed to that word");
            return;
        }

        getDatabase().removeWord(sender.getUuid(), word);
        sender.sendMessage(MessageColor.GREEN + "You will no longer receive notifications when the word " + MessageColor.GOLD + word + MessageColor.GREEN + " is mentioned in chat");
        getNotifier().update();
    }

    @Override
    public List<String> handleTabComplete(CNPlayer sender, String[] args) {
        Set<Word> words = getDatabase().getAllWordsForPlayer(sender.getUuid());
        List<String> wordList = new ArrayList<>();

        if (args.length == 2) {
            for (Word word : words) {
                wordList.add(word.getWord());
            }
        }

        return wordList;
    }
}
