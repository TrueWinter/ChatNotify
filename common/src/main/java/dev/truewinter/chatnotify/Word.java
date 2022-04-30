package dev.truewinter.chatnotify;

import java.util.UUID;

/**
 * @hidden
 */
public class Word {
    private ChatNotify plugin = ChatNotify.getInstance();
    private int id = 0;
    private UUID uuid = null;
    private String word = null;
    private boolean approved = false;

    public Word(int id, UUID uuid, String word, boolean approved) {
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
}
