package dev.truewinter.chatnotify;

import dev.truewinter.chatnotify.PluginAdapter.CNPlayer;

import java.util.*;

/**
 * @hidden
 */
public class Notifier {
    private ChatNotify plugin = ChatNotify.getInstance();
    private Database db = plugin.getDatabase();

    private Set<Word> words = new HashSet<>();

    public void update() {
        words.clear();
        words.addAll(db.getAllWords());
    }

    public void handleChatEvent(CNPlayer player, String message) {
        if (!containsSubscribedWord(message)) return;
        Map<CNPlayer, Set<String>> sendToPlayers = new HashMap<>();

        for (Word word : getSubscribedWordsInMessage(message))  {
            if (playerIsOnline(word.getUUID()) && word.getApproved()) {
                if (!player.getUuid().toString().equals(word.getUUID().toString())) {
                    plugin.getPluginAdapter().getPlayerByUuid(word.getUUID()).ifPresent(toPlayer -> {
                        if (!sendToPlayers.containsKey(toPlayer)) {
                            sendToPlayers.put(toPlayer, new HashSet<>());
                        }
                        sendToPlayers.get(toPlayer).add(word.getWord());
                    });
                }
            }
        }

        for (Map.Entry<CNPlayer, Set<String>> entry : sendToPlayers.entrySet()) {
            CNPlayer p = entry.getKey();
            Set<String> words = entry.getValue();

            if (p.hasPermission("chatnotify.notify") && db.playerIsOptedIn(p.getUuid())) {
                String wordsString = String.join(", ", words);
                String singularOrPlural = "word";

                if (words.size() > 1) {
                    singularOrPlural = "words";
                }

                plugin.getPluginAdapter().sendMessage(p,MessageColor.LIGHT_PURPLE + p.getUsername() + " mentioned the following " + singularOrPlural + " you're subscribed to: " + MessageColor.GOLD + wordsString);

                if (db.playerSoundOn(p.getUuid())) {
                    plugin.getPluginAdapter().playSound(p);
                }
            }
        }
    }

    public void handlePlayerMessageEvent(CNPlayer sender, CNPlayer recipient, String message) {
        if (!sender.getUuid().equals(recipient.getUuid())) {
            if (db.playerIsOptedIn(recipient.getUuid()) && db.playerSoundOn(recipient.getUuid())) {
                plugin.getPluginAdapter().playSound(recipient);
            }
        }
    }

    public void notifyMods(UUID uuid, String word, boolean approved) {
        List<CNPlayer> sendTo = new ArrayList<>();

        for (CNPlayer online : plugin.getPluginAdapter().getOnlinePlayers()) {
            if (online.hasPermission("chatnotify.mod.notify") && db.modIsOptedIn(online.getUuid())) {
                if (!online.getUuid().equals(uuid)) {
                    sendTo.add(online);
                }
            }
        }

        plugin.getPluginAdapter().getPlayerByUuid(uuid).ifPresent(p -> {
            if (approved) {
                plugin.getPluginAdapter().sendMessage(sendTo, MessageColor.GOLD + p.getUsername() + " subscribed to the word " + MessageColor.YELLOW + word);
            } else {
                plugin.getPluginAdapter().sendMessage(sendTo, MessageColor.GOLD + p.getUsername() + " would like to subscribe to the word " + MessageColor.YELLOW + word + MessageColor.GOLD + " but requires approval. Run " + MessageColor.YELLOW + "/cnm queue" + MessageColor.GOLD + " for information");
            }
        });
    }

    public void notifyModOnLogin(CNPlayer player) {
        if (player.hasPermission("chatnotify.mod.notify") && db.modIsOptedIn(player.getUuid())) {
            if (db.getAllWordsRequiringApproval().size() != 0) {
                plugin.getPluginAdapter().sendMessage(player, MessageColor.GREEN + "There are one or more ChatNotify requests for review. Run " + MessageColor.GOLD + "/cnm queue" + MessageColor.GREEN + " for more information");
            }
        }
    }

    public void notifyPlayerAboutAllow(UUID uuid, String word) {
        plugin.getPluginAdapter().getPlayerByUuid(uuid).ifPresent(player -> {
            plugin.getPluginAdapter().sendMessage(player, MessageColor.GREEN + "A mod has approved your subscription to the word: " + MessageColor.GOLD + word);
        });
    }

    public void notifyPlayerAboutDeny(UUID uuid, String word) {
        plugin.getPluginAdapter().getPlayerByUuid(uuid).ifPresent(player -> {
            plugin.getPluginAdapter().sendMessage(player, MessageColor.YELLOW + "A mod has denied your subscription to the word: " + MessageColor.GOLD + word);
        });
    }

    private boolean playerIsOnline(UUID uuid) {
        return plugin.getPluginAdapter().getPlayerByUuid(uuid).map(CNPlayer::isOnline).orElse(false);
    }

    private boolean containsSubscribedWord(String message) {
        boolean containsWord = false;

        for (Word word : words) {
            if (message.toLowerCase().contains(word.getWord())) {
                containsWord = true;
                break;
            }
        }

        return containsWord;
    }

    private Set<Word> getSubscribedWordsInMessage(String message) {
        Set<Word> subscribedWords = new HashSet<>();

        for (Word word : words) {
            if (message.toLowerCase().contains(word.getWord())) {
                subscribedWords.add(word);
            }
        }

        return subscribedWords;
    }
}
