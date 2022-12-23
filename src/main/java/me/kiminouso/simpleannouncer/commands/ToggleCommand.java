/* Authored by TheKimiNoUso 2022 */
package me.kiminouso.simpleannouncer.commands;

import me.kiminouso.simpleannouncer.SimpleAnnouncer;
import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand extends TippieCommand {
    public ToggleCommand() {
        super.subLevel = 1;
        super.name = "toggle";
        super.description = "Toggle your automated announcements";
        super.permission = "announcer.toggle";
    }

    SimpleAnnouncer plugin = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class);

    @Override
    public void executes(
            @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
            throws NoSuchMethodException {
        if (plugin.getConfig().getBoolean("send-auto-announcements")) {
            plugin.getAnnouncementTask().end();
            setConfigValue(sender, false);
        } else {
            plugin.getAnnouncementTask().start();
            setConfigValue(sender, true);
        }
    }

    private void setConfigValue(CommandSender sender, boolean value) {
        plugin.getConfig().set("send-auto-announcements", value);
        plugin.saveConfig();

        sender.sendMessage(
                value
                        ? "§8[§9SimpleAnnouncer§8] §aToggled automatic announcements on."
                        : "§8[§9SimpleAnnouncer§8] §cToggled automatic announcements off.");
    }
}
