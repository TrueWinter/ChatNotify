package dev.truewinter.chatnotify.PluginAdapter;

import dev.truewinter.chatnotify.ChatNotify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The PluginAdapter class allows for ChatNotify to be used on multiple platforms without needing to rewrite ChatNotify for each of them.
 */
public abstract class PluginAdapter {
    private final ChatNotify plugin = ChatNotify.getInstance();

    /**
     * Notifies the common API of a new chat message
     * @param player the player who sent the message
     * @param message the message content
     * @see CNPlayer
     */
    public void onChatMessage(CNPlayer player, String message) {
        plugin.getNotifier().handleChatEvent(player, message);
    }

    /**
     * Notifies the common API of a new private message
     * @param sender the player who sent the message
     * @param receiver the receiver of the message
     * @param message the message content
     * @see CNPlayer
     */
    public void onPrivateMessage(CNPlayer sender, CNPlayer receiver, String message) {
        plugin.getNotifier().handlePlayerMessageEvent(sender, receiver, message);
    }

    /**
     * Notifies the mods upon login if there are subscriptions awaiting approval
     * @param player the mod who logged on
     * @see CNPlayer
     */
    public void notifyModsOnLogin(CNPlayer player) {
        plugin.getNotifier().notifyModOnLogin(player);
    }

    /**
     * Runs a command
     * @param sender the player who ran the command
     * @param command the command name (e.g. in /cn, the command name would be cn)
     * @param args command arguments
     * @see CNPlayer
     */
    public void onCommand(CNPlayer sender, String command, String[] args) {
        plugin.getCommandHandler().onCommand(sender, command, args);
    }

    /**
     * Retrieves the tab complete options for this command
     * @param sender the player who typed the command
     * @param command the command name (e.g. in /cn, the command name would be cn)
     * @param args command arguments
     * @return the tab complete options
     * @see CNPlayer
     */
    public List<String> onTabComplete(CNPlayer sender, String command, String[] args) {
        return plugin.getCommandHandler().onTabComplete(sender, command, args);
    }

    /**
     * Sends a message to a player, calls {@link #sendMessage(List, String)} internally
     * @param player the player to send the message to
     * @param message the message content
     * @see CNPlayer
     */
    public void sendMessage(CNPlayer player, String message) {
        sendMessage(List.of(player), message);
    }

    /**
     * Sends a message to multiple players
     * @param players a list of players to send the message to
     * @param message the message content
     * @see CNPlayer
     */
    public abstract void sendMessage(List<CNPlayer> players, String message);

    /**
     * Plays a sound for a player
     * @param player the player
     * @see CNPlayer
     */
    public abstract void playSound(CNPlayer player);

    /**
     * Gets a logger instance
     * @return the logger instance
     * @see CNLogger
     */
    public abstract CNLogger getLogger();

    /**
     * Returns a list of online players
     * @return the list of online players
     * @see CNPlayer
     */
    public abstract List<CNPlayer> getOnlinePlayers();

    /**
     * Gets a player by their UUID
     * @param uuid the player's UUID
     * @return the player, wrapped in an {@link Optional}
     * @see CNPlayer
     */
    public abstract Optional<CNPlayer> getPlayerByUuid(UUID uuid);

    /**
     * Gets a player by their username
     * @param name the player's username
     * @return the player, wrapped in an {@link Optional}
     * @see CNPlayer
     */
    public abstract Optional<CNPlayer> getPlayerByName(String name);
}
