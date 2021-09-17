package dev.truewinter.chatnotify.CommandHandler;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Command {
    public void handleCommand(CommandSender sender, String[] args) {

    }

    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
