package dev.truewinter.chatnotify.CommandHandler.ModCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

public class OptInCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cnm optin");
            return;
        }

        getDatabase().setModOptIn(sender.getUuid(), true);
        sender.sendMessage("You will now receive ChatNotify mod notifications");
    }
}
