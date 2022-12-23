# SimpleAnnouncer
An extremely simple and easy to use Announcement plugin without the bells and whistles that most plugins on the market add. This is overall pretty lightweight and has a customisable cooldown via config.

## Key Features
- Hexadecimal colour codes supported!
- No hard dependencies, meaning the plugin works 'out of the box'.
- Support for PlaceholderAPI.
- Support for Java 8 or above.
- You can add messages via in-game commands (/addmessage, /listmessages, and /removemessage), or by manually editing the config.
- Add as many messages as you like.
- Messages are written completely by you, I intentionally left out a prefix option to allow you more customisability.

## Information
An extremely simple and easy to use Announcement plugin without the bells and whistles that most plugins on the market add, making it extremely light-weight. Completely free and support is easily accessible should you have something to suggestion or require help with.

The plugin will send 1 message in chat per cooldown specified by you in your config.yml which can be found in your server files ./plugins/SimpleAnnouncer/config.yml.

Your messages are shuffled in a queue and will not be sent in order; once the queue is empty, it will re-cycle all of your messages back into the queue.

# Commands & Permissions
## Commands
- **/reloadannouncer** - Reloads the config.
- **/addmessage <string>** - Add a new message.
- **/listmessages** - List all of the registered announcements messages.
- **/removemessage** <#> - Remove a message. Check the index number using /listmessages.

## Permissions
- **announcer.reload** - Gives access to /reloadannouncer
- **announcer.addmessage** - Gives access to /addmessage
- **announcer.removemessage** - Gives access to /removemessage
- **announcer.listmessages** - Gives access to /listmessages
- **announcer.bypass** - Prevents the permission holder from receiving automated announcements
