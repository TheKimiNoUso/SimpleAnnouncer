/* Authored by TheKimiNoUso 2022 */
package me.kiminouso.simpleannouncer.commands;

import me.kiminouso.simpleannouncer.SimpleAnnouncer;
import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetDelayCommand extends TippieCommand {
    public SetDelayCommand() {
        super.subLevel = 1;
        super.name = "setdelay";
        super.description = "Change your automated announcement delay";
        super.permission = "announcer.setdelay";
    }

    @Override
    public void executes(
            @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§8[§9SimpleAnnouncer§8] §cUsage: /announcer setdelay <message>.");
            return;
        }

        int newDelay;
        int oldDelay =
                SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getConfig().getInt("timer");

        try {
            newDelay = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§8[§9SimpleAnnouncer§8] §cYou didn't enter a valid number!");
            return;
        }

        SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).setDelay(newDelay);
        sender.sendMessage(
                "§8[§9SimpleAnnouncer§8] §aChanged your announcement delay from " + oldDelay + " to " + newDelay);
    }
}
