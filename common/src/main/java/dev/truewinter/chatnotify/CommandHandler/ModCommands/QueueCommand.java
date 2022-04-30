package dev.truewinter.chatnotify.CommandHandler.ModCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;
import dev.truewinter.chatnotify.Word;

import java.util.*;

public class QueueCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cnm queue");
            return;
        }

        String outputString = "";
        Set<Word> words = getDatabase().getAllWordsRequiringApproval();
        List<String> outputLines = new ArrayList<>();
        int wordsCompleted = 0;

        for (Word word : words) {
            getPlugin().getPluginAdapter().getPlayerByUuid(word.getUUID()).ifPresent(player -> {
                outputLines.add(MessageColor.AQUA + player.getUsername() + ": " + MessageColor.GOLD + word.getWord() + MessageColor.RESET);
            });
        }

        Collections.sort(outputLines);

        for (String line : outputLines) {
            wordsCompleted += 1;
            outputString += line;

            if (wordsCompleted != words.size()) {
                outputString += "\n";
            }
        }

        if (!outputString.equals("")) {
            String output = MessageColor.GREEN + "The following words need approval. Run " + MessageColor.GOLD + "/cnm allow <player> <word>" + MessageColor.GREEN + " or " + MessageColor.GOLD + "/cnm deny <player> <word>" + MessageColor.RESET + "\n" + outputString;
            sender.sendMessage(output);
        } else {
            sender.sendMessage(MessageColor.GREEN + "There are no words needing approval");
        }
    }
}
