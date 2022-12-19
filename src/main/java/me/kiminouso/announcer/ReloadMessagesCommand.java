package me.kiminouso.announcer;

import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadMessagesCommand extends TippieCommand {
    @Override
    public void executes(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NoSuchMethodException {
        Announcer.getPlugin(Announcer.class).reloadConfig();
    }
}
