package dev.truewinter.chatnotify.CommandHandler.PlayerCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

public class OffCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cn off");
            return;
        }

        getDatabase().setPlayerOptIn(sender.getUuid(), false);
        sender.sendMessage("You will no longer receive notifications about subscribed words in chat");
    }
}
