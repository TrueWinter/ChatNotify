package dev.truewinter.chatnotify.CommandHandler.PlayerCommand;

import dev.truewinter.chatnotify.CommandHandler.Command;
import dev.truewinter.chatnotify.Database;
import dev.truewinter.chatnotify.Notifier;
import dev.truewinter.chatnotify.Word;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RemoveCommand extends Command {
    JavaPlugin plugin = null;
    Database db = null;
    Notifier notifier = null;

    public RemoveCommand(JavaPlugin plugin, Database db, Notifier notifier) {
        this.plugin = plugin;
        this.db = db;
        this.notifier = notifier;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length != 2) {
            sender.sendMessage(ChatColor.YELLOW + "Invalid usage. Correct usage: " + ChatColor.GOLD + "/cn remove <word>" + ChatColor.YELLOW + " (without <>)");
            return;
        }

        String word = args[1].toLowerCase();

        if (!db.playerHasWord(player.getUniqueId(), word)) {
            sender.sendMessage(ChatColor.YELLOW + "You're not subscribed to that word");
            return;
        }

        db.removeWord(player.getUniqueId(), word);
        sender.sendMessage(ChatColor.GREEN + "You will no longer receive notifications when the word " + ChatColor.GOLD + word + ChatColor.GREEN + " is mentioned in chat");
        notifier.update();
    }

    @Override
    public List<String> handleTabComplete(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Set<Word> words = db.getAllWordsForPlayer(player.getUniqueId());
        List<String> wordList = new ArrayList<>();

        if (args.length == 2) {
            for (Word word : words) {
                wordList.add(word.getWord());
            }
        }

        return wordList;
    }
}
