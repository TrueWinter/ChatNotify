package dev.truewinter.chatnotify.CommandHandler;

import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

import java.util.Map;

public abstract class AbstractCommandHandler {
    public abstract CNCommand getCommand(String command);
    public abstract Map<String, CNCommand> getCommands();
    public abstract boolean hasCommand(String command);
    public abstract void addRelevantDBEntry(CNPlayer player);
}
