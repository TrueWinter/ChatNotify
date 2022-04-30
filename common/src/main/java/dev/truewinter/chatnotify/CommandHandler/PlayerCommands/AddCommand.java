package dev.truewinter.chatnotify.CommandHandler.PlayerCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

public class AddCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 2) {
            getPlugin().getPluginAdapter().sendMessage(sender, MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cn add <word>" + MessageColor.YELLOW + " (without <>)");
            return;
        }

        String word = args[1].toLowerCase();

        if (getDatabase().playerHasWord(sender.getUuid(), word)) {
            sender.sendMessage(MessageColor.YELLOW + "You're already subscribed to that word");
            return;
        }

        if (word.length() > 20) {
            sender.sendMessage(MessageColor.YELLOW + "The maximum word length is 20 characters");
            return;
        }

        if (sender.hasPermission("chatnotify.bypassapproval")) {
            getDatabase().addWord(sender.getUuid(), word, true);
            sender.sendMessage(MessageColor.GREEN + "You will now receive notifications when the word " + MessageColor.GOLD + word + MessageColor.GREEN + " is mentioned in chat");
            getNotifier().notifyMods(sender.getUuid(), word, true);
        } else if (getConfig().getRequireApprovalLength() >= word.length()) {
            getDatabase().addWord(sender.getUuid(), word, false);
            sender.sendMessage(MessageColor.DARK_GREEN + "A mod will need to approve this word subscription before you will receive notifications when the word " + MessageColor.GOLD + word + MessageColor.DARK_GREEN + " is mentioned in chat");
            getNotifier().notifyMods(sender.getUuid(), word, false);
        } else if (getConfig().getRequireApproval().equals("none")) {
            getDatabase().addWord(sender.getUuid(), word, true);
            sender.sendMessage(MessageColor.GREEN + "You will now receive notifications when the word " + MessageColor.GOLD + word + MessageColor.GREEN + " is mentioned in chat");
            getNotifier().notifyMods(sender.getUuid(), word, true);
        } else if (getConfig().getRequireApproval().equals("part")) {
            if (sender.getUsername().toLowerCase().contains(word)) {
                getDatabase().addWord(sender.getUuid(), word, true);
                sender.sendMessage(MessageColor.GREEN + "You will now receive notifications when the word " + MessageColor.GOLD + word + MessageColor.GREEN + " is mentioned in chat");
                getNotifier().notifyMods(sender.getUuid(), word, true);
            } else {
                getDatabase().addWord(sender.getUuid(), word, false);
                sender.sendMessage(MessageColor.DARK_GREEN + "A mod will need to approve this word subscription before you will receive notifications when the word " + MessageColor.GOLD + word + MessageColor.DARK_GREEN + " is mentioned in chat");
                getNotifier().notifyMods(sender.getUuid(), word, false);
            }
        } else {
            getDatabase().addWord(sender.getUuid(), word, false);
            sender.sendMessage(MessageColor.DARK_GREEN + "A mod will need to approve this word subscription before you will receive notifications when the word " + MessageColor.GOLD + word + MessageColor.DARK_GREEN + " is mentioned in chat");
            getNotifier().notifyMods(sender.getUuid(), word, false);
        }

        getNotifier().update();
    }
}
