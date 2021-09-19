package dev.truewinter.chatnotify;

import com.earth2me.essentials.Essentials;
import dev.truewinter.chatnotify.CommandHandler.CommandHandler;
import net.ess3.api.events.PrivateMessageSentEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatNotify extends JavaPlugin implements Listener {
    static Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");

    JavaPlugin plugin = this;
    Database db = null;
    Notifier notifier = null;
    CommandHandler ch = null;
    Config config = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        config = new Config(plugin);
        config.loadConfig();

        db = new Database(plugin, config);
        notifier = new Notifier(plugin, db);
        ch = new CommandHandler(plugin, db, notifier, config);

        plugin.getCommand("cn").setExecutor(ch);
        plugin.getCommand("cnm").setExecutor(ch);

        getServer().getPluginManager().registerEvents(this, this);

        notifier.update();
    }

    @Override
    public void onDisable() {

    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        // Delay of 3 ticks ensures that this runs after player message
        new BukkitRunnable() {
            @Override
            public void run() {
                notifier.handleChatEvent(e);
            }
        }.runTaskLater(plugin, 3);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMessage(PrivateMessageSentEvent e) {
        if (e.getResponse().isSuccess()) {
            notifier.handlePlayerMessageEvent(e.getSender(), e.getRecipient(), e.getMessage());
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent e) {
        // Delay of 3 ticks ensures that this runs after all other login messages
        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = e.getPlayer();
                notifier.notifyModOnLogin(player);
            }
        }.runTaskLater(plugin, 3);
    }
}
