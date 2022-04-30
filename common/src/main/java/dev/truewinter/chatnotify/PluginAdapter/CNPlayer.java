package dev.truewinter.chatnotify.PluginAdapter;

import dev.truewinter.chatnotify.ChatNotify;

import java.util.UUID;

/**
 * An abstract class representing players in a cross-platform manner
 */
public abstract class CNPlayer {
    private UUID uuid;
    private String username;

    /**
     * Creates a CNPlayer instance
     * @param uuid the player's UUID
     * @param username the player's username
     */
    public CNPlayer(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    /**
     * Returns the player's UUID
     * @return the player's UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns the player's username
     * @return the player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's UUID
     * @param uuid the player's UUID
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Sets the player's username
     * @param username the player's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sends a message to the player
     * @param message the message to send
     */
    public void sendMessage(String message) {
        ChatNotify.getInstance().getPluginAdapter().sendMessage(this, message);
    }

    /**
     * Returns true if the player is online
     * @return true if the player is online
     */
    public abstract boolean isOnline();

    /**
     * Returns true if the player has the permission
     * @param permission the permission to check
     * @return true if the player has the permission
     */
    public abstract boolean hasPermission(String permission);
}
