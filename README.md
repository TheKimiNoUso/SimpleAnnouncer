# SimpleAnnouncer
An extremely simple and easy to use Announcement plugin without the bells and whistles that most plugins on the market add. This is overall pretty lightweight and has a customisable cooldown via config.

## Key Features
- Hexadecimal colour codes supported!
- You can add messages via in-game commands (/addmessage, /listmessages, and /removemessage), or by manually editing the config.
- Add as many messages as you like.
- Messages are written completely by you, I intentionally left out a prefix option to allow you more customisability.

## Information
The plugin will send 1 message in chat per cooldown specified by you in your config.yml which can be found in your server files ./plugins/SimpleAnnouncer/config.yml.

It won't send the same message it will shuffle the list of strings you add to your config, it will not choose the same message that was sent in the previous announcement. For this reason I recommend you include 5 or more of your own announcements.

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
