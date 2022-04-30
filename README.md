# ChatNotify

ChatNotify is a Spigot/Paper 1.17+ plugin that notifies players if they are mentioned in chat, or receive a message. Players can configure which words will trigger a notification.

The plugin currently only supports messages in chat and private messages sent using Essentials.

## Configuration

```yaml
# Requires player with chatnotify.commands.mod.allow to approve ChatNotify subscriptions
# Possible values:
# - "all": all subscriptions have to be approved
# - "part": if the word is not part of the player name, require approval
# - "none": no approval required
require-approval: "part"

# Any subscribed word this length or shorter will require approval
require-approval-length: 3

# Do not edit anything below this line
init-setup-done: false
config-version: 1
```

## Commands

### Player

- `/cn add <word>`: Allows players to subscribe to a word in order to receive notifications when it appears in chat
- `/cn remove <word>`: Allows players to unsubscribe from a word
- `/cn list`: Lists words the player is subscribed to. Words requiring approval will be marked with `*`
- `/cn off` or `/cn on`: Allows players to toggle notifications
- `/cn sound <on|off>`: Allows players to toggle the sound that plays when mentioned

### Mod

- `/cnm add <player> <word>`: Mods can use this command to subscribe a player to a word
- `/cnm remove <player> <word>`: Mods can use this command to unsubscribe a player from a word
- `/cnm queue`: This command will show the words that require approval
- `/cnm allow <player> <word>`: Mods can approve words in the approval queue using this command
- `/cnm deny <player> <word>`: Mods can deny words in the approval queue using this command. This will remove the word from the database
- `/cnm optin` or `/cnm optout`: This command allows mods to toggle ChatNotify mod notifications

### Admin

- `/cnm reload`: Allows admins to reload config

## Important Note

ChatNotify will not notify any player or mod until the first time they run a ChatNotify command.

## Permissions

- `chatnotify.notify`: Players with this permission will be notified of words they are subscribed to
- `chatnotify.bypassapproval`: Players with this permission can subscribe to any word without requiring approval
- `chatnotify.mod.notify`: Players with this permission will receive mod messages from ChatNotify
- `chatnotify.commands.add`: Allows players to add to their subscribed words
- `chatnotify.commands.remove`: Allows players to remove from their subscribed words
- `chatnotify.commands.list`: Allows players to list their subscribed words
- `chatnotify.commands.on`: Allows players to opt-in to ChatNotify messages
- `chatnotify.commands.off`: Allows players to opt-out from ChatNotify messages
- `chatnotify.commands.sound`: Allows players to toggle ChatNotify sounds
- `chatnotify.commands.mod.add`: Allows mods to add to other players' subscribed words
- `chatnotify.commands.mod.remove`: Allows mods to remove other players' subscribed words
- `chatnotify.commands.mod.list`: Allows mods to list other players' subscribed words
- `chatnotify.commands.mod.allow`: Allows mods to allow flagged subscribed words 
- `chatnotify.commands.mod.deny`: Allows mods to deny flagged subscribed words
- `chatnotify.commands.mod.queue`: Shows the words that still need approval
- `chatnotify.commands.mod.optin`: Allows mods to opt-in to ChatNotify messages
- `chatnotify.commands.mod.optout`: Allows mods to opt-out from ChatNotify messages
- `chatnotify.commands.admin.reload`: Allows admins to reload the ChatNotify config

## Dependencies

ChatNotify will **not** work without the following plugins being installed:

- Essentials(X)
- Essentials(X)Chat

## Extending Support to Other Platforms

ChatNotify has been written in such a way to easily allow for other platforms and chat plugins to be supported. See the `PluginAdapter.md` file for more information.