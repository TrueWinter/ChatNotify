package dev.truewinter.chatnotify.CommandHandler.ModCommand;

import dev.truewinter.chatnotify.CommandHandler.Command;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import dev.truewinter.chatnotify.Word;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class QueueCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;

    public QueueCommand(JavaPlugin plugin, Database db, Notifier notifier) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cnm queue");
            return;
        }

        String outputString = "";
        Set<Word> words = db.getAllWordsRequiringApproval();
        List<String> outputLines = new ArrayList<>();
        int wordsCompleted = 0;

        for (Word word : words) {
            Player player = Bukkit.getPlayer(word.getUUID());
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(word.getUUID());

            String tempString = "";

            if (player != null) {
                tempString = ChatColor.AQUA + player.getName() + ": " + ChatColor.GOLD + word.getWord() + ChatColor.RESET;
            } else {
                tempString = ChatColor.AQUA + offlinePlayer.getName() + ": " + ChatColor.GOLD + word.getWord() + ChatColor.RESET;
            }

            outputLines.add(tempString);
        }

        Collections.sort(outputLines);

        for (String line : outputLines) {
            wordsCompleted += 1;
            outputString += line;

            if (wordsCompleted != words.size()) {
                outputString += "\n";
            }
        }

        if (!outputString.equals("")) {
            String output = ChatColor.GREEN + "The following words need approval. Run " + ChatColor.GOLD + "/cnm allow <player> <word>" + ChatColor.GREEN + " or " + ChatColor.GOLD + "/cnm deny <player> <word>" + ChatColor.RESET + "\n" + outputString;
            sender.sendMessage(output);
        } else {
            sender.sendMessage(ChatColor.GREEN + "There are no words needing approval");
        }
    }

    @Override
    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
