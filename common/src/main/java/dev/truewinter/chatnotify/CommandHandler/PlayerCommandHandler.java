package dev.truewinter.chatnotify.CommandHandler;

import dev.truewinter.chatnotify.ChatNotify;
import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.CommandHandler.CommandHandler;
import dev.truewinter.chatnotify.CommandHandler.PlayerCommands.*;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlayerCommandHandler extends AbstractCommandHandler {
    private final Map<String, CNCommand> subcommands = new HashMap<>();
    private final Database db = ChatNotify.getInstance().getDatabase();

    public PlayerCommandHandler() {
        registerSubCommand("add", "chatnotify.commands.add", new AddCommand());
        registerSubCommand("remove", "chatnotify.commands.remove", new RemoveCommand());
        registerSubCommand("list", "chatnotify.commands.list", new ListCommand());
        registerSubCommand("on", "chatnotify.commands.on", new OnCommand());
        registerSubCommand("off", "chatnotify.commands.off", new OffCommand());
        registerSubCommand("sound", "chatnotify.commands.sound", new SoundCommand());
    }

    @Override
    public CNCommand getCommand(String command) {
        return subcommands.get(command);
    }

    @Override
    public Map<String, CNCommand> getCommands() {
        return subcommands;
    }

    @Override
    public boolean hasCommand(String command) {
        return subcommands.containsKey(command);
    }

    @Override
    public void addRelevantDBEntry(CNPlayer player) {
        if (!db.playerExistsInDB(player.getUuid())) {
            db.addPlayer(player.getUuid());
        }
    }

    private void registerSubCommand(String command, String perm, CNCommand classObject) {
        classObject.setPermission(perm);
        subcommands.put(command, classObject);
    }
}
