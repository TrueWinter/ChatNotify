package dev.truewinter.chatnotify.CommandHandler.PlayerCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

import java.util.ArrayList;
import java.util.List;

public class SoundCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cn sound <on|off>");
            return;
        }

        if (args[1].equals("on")) {
            getDatabase().setPlayerSound(sender.getUuid(), true);
            sender.sendMessage("A sound will play when a subscribed word is mentioned in chat");
        } else if (args[1].equals("off")) {
            getDatabase().setPlayerSound(sender.getUuid(), false);
            sender.sendMessage("A sound will no longer play when a subscribed word is mentioned in chat");
        } else {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cn sound <on|off>");
        }
    }

    @Override
    public List<String> handleTabComplete(CNPlayer sender, String[] args) {
        List<String> tabList = new ArrayList<>();

        if (args.length == 2) {
            tabList.add("on");
            tabList.add("off");
        }

        return tabList;
    }
}
