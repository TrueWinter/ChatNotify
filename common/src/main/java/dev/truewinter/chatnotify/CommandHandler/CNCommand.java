package dev.truewinter.chatnotify.CommandHandler;

import dev.truewinter.chatnotify.ChatNotify;
import dev.truewinter.chatnotify.Config;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class CNCommand {
    private ChatNotify plugin = ChatNotify.getInstance();
    private Database db = plugin.getDatabase();
    private Notifier notifier = plugin.getNotifier();
    private Config config = plugin.getConfig();
    private String permission;

    public abstract void handleCommand(CNPlayer sender, String[] args);

    public List<String> handleTabComplete(CNPlayer sender, String[] args) {
        return new ArrayList<>();
    }

    public ChatNotify getPlugin() {
        return plugin;
    }

    public Database getDatabase() {
        return db;
    }

    public Notifier getNotifier() {
        return notifier;
    }

    public Config getConfig() {
        return config;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
