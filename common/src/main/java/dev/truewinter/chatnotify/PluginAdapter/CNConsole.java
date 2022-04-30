package dev.truewinter.chatnotify.PluginAdapter;

import dev.truewinter.chatnotify.ChatNotify;

import java.util.UUID;

/**
 * An abstract class representing console in a cross-platform manner
 */
public class CNConsole extends CNPlayer {
    private static final UUID uuid = new UUID(0, 0);
    private static final String username = "[CONSOLE]";

    public CNConsole() {
        super(uuid, username);
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void sendMessage(String message) {
        ChatNotify.getInstance().getPluginAdapter().getLogger().info(message);
    }
}
