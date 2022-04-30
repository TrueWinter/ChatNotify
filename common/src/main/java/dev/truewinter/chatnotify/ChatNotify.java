package dev.truewinter.chatnotify;

import dev.truewinter.chatnotify.CommandHandler.CommandHandler;
import dev.truewinter.chatnotify.PluginAdapter.PluginAdapter;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class ChatNotify {
    private static ChatNotify plugin;
    private static PluginAdapter pluginAdapter;

    private File dataDir = null;
    private Database db = null;
    private Notifier notifier = null;
    private CommandHandler ch = null;
    private Config config = null;

    private ChatNotify() {
        plugin = this;
    }

    /**
     * Enables the common API. The Plugin Adapter must be set before enable() is called.
     * @param dataDir the plugin's data directory
     * @throws Exception an Exception is thrown if the Plugin Adapter has not been set
     * @throws IOException an IOException is thrown if the config cannot be loaded
     */
    public void enable(File dataDir) throws Exception {
        if (pluginAdapter == null) {
            throw new Exception("Plugin Adapter is required before enable call");
        }

        this.dataDir = dataDir;

        config = new Config();
        config.loadConfig(new File(dataDir + File.separator + "config.yml"));

        db = new Database();
        notifier = new Notifier();
        ch = new CommandHandler();

        notifier.update();

        pluginAdapter.getLogger().info("ChatNotify Common API enabled");
    }

    /**
     * This is an internal method. It is only public to allow access from the CommandHandler and PluginAdapter packages.
     * @return the config
     * @hidden
     */
    public Config getConfig() {
        return this.config;
    }

    /**
     * This is an internal method. It is only public to allow access from the CommandHandler and PluginAdapter packages.
     * @return the database
     * @hidden
     */
    public Database getDatabase() {
        return this.db;
    }

    /**
     * This is an internal method. It is only public to allow access from the CommandHandler and PluginAdapter packages.
     * @return the notifier
     * @hidden
     */
    public Notifier getNotifier() {
        return this.notifier;
    }

    /**
     * This is an internal method. It is only public to allow access from the CommandHandler and PluginAdapter packages.
     * @return the command handler
     * @hidden
     */
    public CommandHandler getCommandHandler() {
        return this.ch;
    }

    /**
     * Returns the data directory
     * @return the data directory
     * @throws Exception an Exception is thrown if the data directory is null
     */
    public File getDataDir() throws Exception {
        if (dataDir == null) {
            throw new Exception("Data directory is null. Enable the common API before use");
        }
        return this.dataDir;
    }

    /**
     * Returns the version.
     * This is an internal method.
     * @return the version
     * @throws IOException an IOException is thrown if the version file cannot be loaded
     * @hidden
     */
    public String getVersion() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("chatnotify.properties"));
        return properties.getProperty("version");
    }

    /**
     * Returns the plugin adapter that has been set
     * @return the plugin adapter
     */
    public PluginAdapter getPluginAdapter() {
        return pluginAdapter;
    }

    /**
     * Allows you to set the plugin adapter that will be used
     * @param pluginAdapter the plugin adapter
     */
    public void setPluginAdapter(PluginAdapter pluginAdapter) {
        ChatNotify.pluginAdapter = pluginAdapter;
    }

    /**
     * Returns the ChatNotify instance, creating one if it doesn't already exist
     * @return the ChatNotify instance
     */
    public static ChatNotify getInstance() {
        if (plugin == null) {
            plugin = new ChatNotify();
        }

        return plugin;
    }

    /**
     * Returns true if a plugin adapter has been set
     * @return true if a plugin adapter has been set
     */
    public static boolean hasPluginAdapter() {
        return pluginAdapter != null;
    }
}
