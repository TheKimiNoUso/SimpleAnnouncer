/* Authored by TheKimiNoUso 2022 */
package me.kiminouso.simpleannouncer.commands;

import java.awt.*;
import me.tippie.tippieutils.commands.TippieCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

public class AnnouncerBaseCommand extends TippieCommand {
    public AnnouncerBaseCommand() {
        super.name = "announcer";
        super.getSubCommands().add(new AddMessageCommand());
        super.getSubCommands().add(new RemoveMessageCommand());
        super.getSubCommands().add(new ListMessagesCommand());
        super.getSubCommands().add(new ToggleCommand());
        super.getSubCommands().add(new ReloadMessagesCommand());
        super.getSubCommands().add(new AnnounceCommand());
        super.getSubCommands().add(new SetDelayCommand());
    }

    @Override
    protected void sendHelpMessage(CommandSender sender, String label, String prefix) {
        sender.sendMessage("\n§8[§9SimpleAnnouncer§8]§9 Announcement Commands");

        getSubCommands().forEach(cmd -> {
            if (!sender.hasPermission(cmd.getPermission())) return;

            TextComponent helpMessage =
                    new TextComponent("§7 - §e/" + label + " " + cmd.getName() + ":§f " + cmd.getDescription());
            helpMessage.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Click to open command.")));
            helpMessage.setClickEvent(
                    new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " " + cmd.getName() + " "));
            sender.spigot().sendMessage(helpMessage);
        });
    }
}
