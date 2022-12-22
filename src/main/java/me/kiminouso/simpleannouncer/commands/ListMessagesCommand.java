package me.kiminouso.simpleannouncer.commands;

import me.kiminouso.simpleannouncer.SimpleAnnouncer;
import me.tippie.tippieutils.commands.TippieCommand;
import me.tippie.tippieutils.functions.ColorUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListMessagesCommand extends TippieCommand {
    public ListMessagesCommand() {
        super.subLevel = 1;
        super.name = "list";
        super.description = "List all registered announcements";
        super.permission = "announcer.listmessage";
    }

    @Override
    public void executes(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NoSuchMethodException {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command must be sent from a player!");
        }

        List<String> configList = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getConfig().getStringList("messages");
        int index = 0;
        for (String message : configList) {
            TextComponent msg = new TextComponent(Stream.of(ColorUtils.translateColorCodes('&', "&7 -&e #" + index + ":&r " + message)).map(component -> component.toLegacyText()).collect(Collectors.joining()));
            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cClick to remove message.")));
            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/removemessage " + index));
            sender.spigot().sendMessage(msg);
            index++;
        }

        TextComponent msg = new TextComponent("§a[Click to add a message]");
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Click to add a message.")));
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/addmessage "));
        sender.spigot().sendMessage(msg);
    }
}
