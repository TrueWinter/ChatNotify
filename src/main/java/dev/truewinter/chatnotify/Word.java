package dev.truewinter.chatnotify;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Word {
    private JavaPlugin plugin = null;
    private Connection conn = null;
    private int id = 0;
    private UUID uuid = null;
    private String word = null;
    private boolean approved = false;

    public Word(JavaPlugin plugin, Connection conn, int id, UUID uuid, String word, boolean approved) {
        this.plugin = plugin;
        this.conn = conn;
        this.id = id;
        this.uuid = uuid;
        this.word = word;
        this.approved = approved;
    }

    public int getId() {
        return id;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getWord() {
        return word;
    }

    public boolean getApproved() {
        return approved;
    }

    private int booleanToInt(boolean bool) {
        if (bool) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean intToBoolean(int integer) {
        if (integer == 1) {
            return true;
        } else {
            return false;
        }
    }
}
