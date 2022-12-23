/* Authored by TheKimiNoUso 2022 */
package me.kiminouso.simpleannouncer.commands;

import java.util.Arrays;
import me.kiminouso.simpleannouncer.SimpleAnnouncer;
import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AddMessageCommand extends TippieCommand {
    public AddMessageCommand() {
        super.subLevel = 1;
        super.name = "add";
        super.description = "Add a message from your announcements";
        super.permission = "announcer.addmessage";
    }

    @Override
    public void executes(
            @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
            throws NoSuchMethodException {
        if (args.length < 1) {
            sender.sendMessage("§8[§9SimpleAnnouncer§8] §cUsage: /addmessage <message>.");
            return;
        }

        SimpleAnnouncer.getPlugin(SimpleAnnouncer.class)
                .addMessage(String.join(" ", Arrays.copyOfRange(args, 0, args.length)));
        sender.sendMessage(
                "§8[§9SimpleAnnouncer§8] §aSuccessfully added your message! Please wait a couple of seconds for it to register.");
    }
}
