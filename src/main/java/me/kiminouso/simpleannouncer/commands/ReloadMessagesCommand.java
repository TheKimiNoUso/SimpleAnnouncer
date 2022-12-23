/* Authored by TheKimiNoUso 2022 */
package me.kiminouso.simpleannouncer.commands;

import me.kiminouso.simpleannouncer.SimpleAnnouncer;
import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadMessagesCommand extends TippieCommand {
    public ReloadMessagesCommand() {
        super.subLevel = 1;
        super.name = "reload";
        super.description = "Reload configuration";
        super.permission = "announcer.reload";
    }

    @Override
    public void executes(
            @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).reload();
        sender.sendMessage("§8[§9SimpleAnnouncer§8] §aSuccessfully reloaded your config.yml");
    }
}
