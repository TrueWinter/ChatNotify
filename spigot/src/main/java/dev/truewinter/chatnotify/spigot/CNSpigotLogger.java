package dev.truewinter.chatnotify.spigot;

import dev.truewinter.chatnotify.PluginAdapter.CNLogger;

public class CNSpigotLogger implements CNLogger {
    ChatNotifySpigot plugin = ChatNotifySpigot.getInstance();

    @Override
    public void info(String message) {
        plugin.getLogger().info(message);
    }

    @Override
    public void warn(String message) {
        plugin.getLogger().warning(message);
    }

    @Override
    public void error(String message) {
        plugin.getLogger().severe(message);
    }

    @Override
    public void error(String message, Exception exception) {
        plugin.getLogger().severe(message);
        exception.printStackTrace();
    }
}
