package dev.truewinter.chatnotify;

import com.earth2me.essentials.messaging.IMessageRecipient;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Notifier {
    JavaPlugin plugin = null;
    Database db = null;

    Set<Word> words = new HashSet<>();

    public Notifier(JavaPlugin plugin, Database db) {
        this.plugin = plugin;
        this.db = db;
    }

    public void update() {
        words.clear();
        words.addAll(db.getAllWords());
    }

    public void handleChatEvent(AsyncPlayerChatEvent e) {
        if (containsSubscribedWord(e.getMessage())) {
            Map<Player, Set<String>> sendToPlayers = new HashMap<>();

            for (Word word : getSubscribedWordsInMessage(e.getMessage()))  {
                if (playerIsOnline(word.getUUID()) && word.getApproved()) {
                    Player player = Bukkit.getPlayer(word.getUUID());
                    if (!e.getPlayer().getUniqueId().toString().equals(word.getUUID().toString())) {
                        if (!sendToPlayers.containsKey(player)) {
                            sendToPlayers.put(player, new HashSet<String>());
                        }
                        sendToPlayers.get(player).add(word.getWord());
                    }
                }
            }

            for (Map.Entry<Player, Set<String>> entry : sendToPlayers.entrySet()) {
                Player player = entry.getKey();
                Set<String> words = entry.getValue();

                if (player.hasPermission("chatnotify.notify") && db.playerIsOptedIn(player.getUniqueId())) {
                    String wordsString = StringUtils.join(words, ", ");
                    String singularOrPlural = "word";

                    if (words.size() > 1) {
                        singularOrPlural = "words";
                    }

                    player.sendMessage(ChatColor.LIGHT_PURPLE + e.getPlayer().getName() + " mentioned the following " + singularOrPlural + " you're subscribed to: " + ChatColor.GOLD + wordsString);

                    if (db.playerSoundOn(player.getUniqueId())) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
                    }
                }
            }
        }
    }

    public void handlePlayerMessageEvent(IMessageRecipient sender, IMessageRecipient recipient, String message) {
        if (!sender.getUUID().equals(recipient.getUUID())) {
            Player player = Bukkit.getPlayer(recipient.getUUID());
            if (db.playerIsOptedIn(player.getUniqueId()) && db.playerSoundOn(player.getUniqueId())) {
                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
            }
        }
    }

    public void notifyMods(UUID uuid, String word, boolean approved) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission("chatnotify.mod.notify") && db.modIsOptedIn(online.getUniqueId())) {
                if (!online.getUniqueId().equals(uuid)) {
                    if (approved) {
                        online.sendMessage(ChatColor.GOLD + Bukkit.getPlayer(uuid).getName() + " subscribed to the word " + ChatColor.YELLOW + word);
                    } else {
                        online.sendMessage(ChatColor.GOLD + Bukkit.getPlayer(uuid).getName() + " would like to subscribe to the word " + ChatColor.YELLOW + word + ChatColor.GOLD + " but requires approval. Run " + ChatColor.YELLOW + "/cnm queue" + ChatColor.GOLD + " for information");
                    }
                }
            }
        }
    }

    public void notifyModOnLogin(Player player) {
        if (player.hasPermission("chatnotify.mod.notify") && db.modIsOptedIn(player.getUniqueId())) {
            if (db.getAllWordsRequiringApproval().size() != 0) {
                player.sendMessage(ChatColor.GREEN + "There are one or more ChatNotify requests for review. Run " + ChatColor.GOLD + "/cnm queue" + ChatColor.GREEN + " for more information");
            }
        }
    }

    public void notifyPlayerAboutAllow(UUID uuid, String word) {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.sendMessage(ChatColor.GREEN + "A mod has approved your subscription to the word: " + ChatColor.GOLD + word);
        }
    }

    public void notifyPlayerAboutDeny(UUID uuid, String word) {
        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            player.sendMessage(ChatColor.YELLOW + "A mod has denied your subscription to the word: " + ChatColor.GOLD + word);
        }
    }

    private boolean playerIsOnline(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return false;
        }

        return true;
    }

    private boolean containsSubscribedWord(String message) {
        boolean containsWord = false;

        for (Word word : words) {
            if (message.contains(word.getWord())) {
                containsWord = true;
                break;
            }
        }

        return containsWord;
    }

    private Set<Word> getSubscribedWordsInMessage(String message) {
        Set<Word> subscribedWords = new HashSet<>();

        for (Word word : words) {
            if (message.contains(word.getWord())) {
                subscribedWords.add(word);
            }
        }

        return subscribedWords;
    }
}
