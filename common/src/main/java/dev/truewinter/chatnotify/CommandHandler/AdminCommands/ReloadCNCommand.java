package dev.truewinter.chatnotify.CommandHandler.AdminCommands;

import dev.truewinter.chatnotify.CommandHandler.CNCommand;
import dev.truewinter.chatnotify.MessageColor;
import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

public class ReloadCNCommand extends CNCommand {
    @Override
    public void handleCommand(CNPlayer sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(MessageColor.YELLOW + "Invalid usage. Correct usage: " + MessageColor.GOLD + "/cnm reload");
            return;
        }

        try {
            getConfig().reloadConfig();
            sender.sendMessage(MessageColor.GREEN + "Config reloaded");
        } catch (Exception e) {
            sender.sendMessage(MessageColor.RED + "Failed to reload config, check console for more info");
            getPlugin().getPluginAdapter().getLogger().error("Failed to reload config", e);
        }
    }
}
