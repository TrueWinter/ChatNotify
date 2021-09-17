package dev.truewinter.chatnotify;

import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    private JavaPlugin plugin = null;

    private String requireApproval = "none";
    private int requireApprovalLength = 0;
    private boolean initSetupDone = false;
    
    public Config(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        requireApproval = plugin.getConfig().getString("require-approval");
        requireApprovalLength = plugin.getConfig().getInt("require-approval-length");
        initSetupDone = plugin.getConfig().getBoolean("init-setup-done");
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }

    public String getRequireApproval() {
        return requireApproval;
    }

    public int getRequireApprovalLength() {
        return requireApprovalLength;
    }

    public boolean isInitSetupDone() {
        return initSetupDone;
    }

    public void setInitSetupDone(boolean done) {
        initSetupDone = done;
        plugin.getConfig().set("init-setup-done", true);
        plugin.saveConfig();
    }
}
