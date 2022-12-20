package me.kiminouso.simpleannouncer.commands;

import me.kiminouso.simpleannouncer.SimpleAnnouncer;
import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AddMessageCommand extends TippieCommand {
    @Override
    public void executes(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NoSuchMethodException {
        SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).addMessage(String.join(" ", Arrays.copyOfRange(args, 0, args.length)));
        sender.sendMessage("§aSuccessfully added your message! Please wait 3 seconds for it to register.");
    }
}
