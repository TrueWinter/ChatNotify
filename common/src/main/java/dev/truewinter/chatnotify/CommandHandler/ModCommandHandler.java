package dev.truewinter.chatnotify.CommandHandler;

import dev.truewinter.chatnotify.ChatNotify;
import dev.truewinter.chatnotify.CommandHandler.AdminCommands.ReloadCNCommand;
import dev.truewinter.chatnotify.CommandHandler.ModCommands.*;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

import java.util.HashMap;
import java.util.Map;

public class ModCommandHandler extends AbstractCommandHandler {
    private final Map<String, CNCommand> subcommands = new HashMap<>();
    private final Database db = ChatNotify.getInstance().getDatabase();

    public ModCommandHandler() {
        registerSubCommand("queue", "chatnotify.commands.mod.queue", new QueueCommand());
        registerSubCommand("optin", "chatnotify.commands.mod.optin", new OptInCommand());
        registerSubCommand("optout", "chatnotify.commands.mod.optout", new OptOutCommand());
        registerSubCommand("allow", "chatnotify.commands.mod.allow", new AllowCommand());
        registerSubCommand("deny", "chatnotify.commands.mod.deny", new DenyCommand());
        registerSubCommand("list", "chatnotify.commands.mod.list", new ModListCommand());
        registerSubCommand("add", "chatnotify.commands.mod.add", new ModAddCommand());
        registerSubCommand("remove", "chatnotify.commands.mod.remove", new ModRemoveCommand());

        registerSubCommand("reload", "chatnotify.commands.admin.reload", new ReloadCNCommand());
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
        if (!db.modExistsInDB(player.getUuid())) {
            db.addMod(player.getUuid());
        }
    }

    private void registerSubCommand(String command, String perm, CNCommand classObject) {
        classObject.setPermission(perm);
        subcommands.put(command, classObject);
    }
}
