# Supporting Other Platforms

In order to support other platforms, you will need to create a plugin that implements the PluginAdapter class from the common module. The Spigot module can be used as an example.

Support for registering chat plugins directly with official platform-specific ChatNotify plugins is planned for future releases.

## Implementation

Your Plugin Adapter should implement the following:

```java
public class Adapter extends PluginAdapter {
    // Send a message to a group of players
    public void sendMessage(List<CNPlayer> players, String message);
    
    // Plays a sound, more specifically ENTITY_ARROW_HIT_PLAYER
    public void playSound(CNPlayer player);
    
    // Returns a CNLogger instance (which you will also need to implement)
    public CNLogger getLogger();
    
    // Returns a list of online players
    public List<CNPlayer> getOnlinePlayers();
    
    // Gets a player (online or offline) by their UUID, wrapped in an Optional
    public Optional<CNPlayer> getPlayerByUuid(UUID uuid);

    // Gets a player (online or offline) by their username, wrapped in an Optional
    public Optional<CNPlayer> getPlayerByName(String name);
}
```

As `CNPlayer` is abstract, you will need to create your own class which extends `CNPlayer`, and implements the following:

```java
public class PlatformPlayer extends CNPlayer {
    // Returns true if the player is online 
    public boolean isOnline();

    // Returns true if the player has the permission
    public boolean hasPermission(String permission);
}
```

To register your Plugin Adapter, the following code can be used from the `onEnable()` method:

```java
ChatNotify.getInstance().setPluginAdapter(pluginAdapter);
try {
    ChatNotify.getInstance().enable(getDataFolder());
} catch (Exception e) {
    plugin.getLogger().severe("Failed to enable common API, disabling ChatNotify");
    e.printStackTrace();
    plugin.getServer().getPluginManager().disablePlugin(this);
    return;
}
```

Your plugin should also register the `cn` and `cnm` commands and pass them to the common API using the `PluginAdapter.onCommand()` and `PluginAdapter.onTabComplete()` methods.

You will also need to call the following methods:

- `PluginAdapter.notifyModsOnLogin()` should be called when a player logs on
- `PluginAdapter.onChatMessage()` should be called when a chat message is sent
- `PluginAdapter.onPrivateMessage()` should be called when a private message is sent

## JavaDocs

When building ChatNotify (`package` using Maven), JavaDocs will be generated in `common/target/site/apidocs`. Anything not documented in the JavaDocs should be considered internal and not be used.