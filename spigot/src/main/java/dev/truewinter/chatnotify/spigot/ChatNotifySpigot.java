package dev.truewinter.chatnotify.spigot;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.chat.EssentialsChat;
import dev.truewinter.chatnotify.ChatNotify;
import dev.truewinter.chatnotify.PluginAdapter.CNLogger;
import net.ess3.api.events.PrivateMessageSentEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatNotifySpigot extends JavaPlugin implements Listener {
    private Adapter pluginAdapter;
    private CNSpigotLogger cnSpigotLogger;
    private CommandHandler commandHandler;
    private static ChatNotifySpigot plugin;

    @Override
    public void onEnable() {
        plugin = this;
        pluginAdapter = new Adapter();
        cnSpigotLogger = new CNSpigotLogger();
        commandHandler = new CommandHandler();

        Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        EssentialsChat essChat = (EssentialsChat) Bukkit.getServer().getPluginManager().getPlugin("EssentialsChat");

        if (ess == null || essChat == null) {
            plugin.getLogger().severe("Essentials and EssentialsChat is required, disabling ChatNotify");
            plugin.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ChatNotify.getInstance().setPluginAdapter(pluginAdapter);
        try {
            ChatNotify.getInstance().enable(getDataFolder());
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to enable common API, disabling ChatNotify");
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        plugin.getCommand("cn").setExecutor(commandHandler);
        plugin.getCommand("cnm").setExecutor(commandHandler);

        plugin.getServer().getPluginManager().registerEvents(this, this);

        plugin.getLogger().info("Started ChatNotify (Spigot) v" + plugin.getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        plugin.getLogger().info("ChatNotify v" + plugin.getDescription().getVersion() + " disabled");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        // Delay of 3 ticks ensures that this runs after player message
        new BukkitRunnable() {
            @Override
            public void run() {
                Adapter.adaptPlayer(e.getPlayer()).ifPresent(p -> {
                    pluginAdapter.onChatMessage(p, e.getMessage());
                });
            }
        }.runTaskLater(plugin, 3);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMessage(PrivateMessageSentEvent e) {
        if (e.getResponse().isSuccess()) {
            Adapter.adaptEssentialsPlayer(e.getSender()).ifPresent(sender -> {
                Adapter.adaptEssentialsPlayer(e.getRecipient()).ifPresent(recipient -> {
                    pluginAdapter.onPrivateMessage(sender, recipient, e.getMessage());
                });
            });
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent e) {
        // Delay of 3 ticks ensures that this runs after all other login messages
        new BukkitRunnable() {
            @Override
            public void run() {
                Adapter.adaptPlayer(e.getPlayer()).ifPresent(pluginAdapter::notifyModsOnLogin);
            }
        }.runTaskLater(plugin, 3);
    }

    protected static ChatNotifySpigot getInstance() {
        return plugin;
    }

    protected CNLogger getCNLogger() {
        return this.cnSpigotLogger;
    }

    protected Adapter getPluginAdapter() {
        return this.pluginAdapter;
    }
}
