/* Authored by TheKimiNoUso 2022 */
package me.kiminouso.simpleannouncer.commands;

import me.kiminouso.simpleannouncer.SimpleAnnouncer;
import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RemoveMessageCommand extends TippieCommand {
    public RemoveMessageCommand() {
        super.subLevel = 1;
        super.name = "remove";
        super.description = "Remove a message from your announcements";
        super.permission = "announcer.removemessage";
    }

    @Override
    public void executes(
            @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
            throws NoSuchMethodException {
        if (args.length < 1) {
            sender.sendMessage(
                    "§8[§9SimpleAnnouncer§8] §cUsage: /removemessage <#>. To list all messages, do /listmessages");
            return;
        }

        if (SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().size() == 0) {
            sender.sendMessage(
                    "§8[§9SimpleAnnouncer§8] §cThere are no registered announcements! Add some using /addmessage, or by using config.yml");
            return;
        }

        int index;

        try {
            index = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(
                    "§8[§9SimpleAnnouncer§8] §cYou didn't enter a valid number! Usage: /removemessage <#>. To list all messages, do /listmessages");
            return;
        }

        SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).removeMessage(index);
        sender.sendMessage(
                "§8[§9SimpleAnnouncer§8] §cSuccessfully removed your message! Please wait a couple of seconds for it to register.");
    }
}
