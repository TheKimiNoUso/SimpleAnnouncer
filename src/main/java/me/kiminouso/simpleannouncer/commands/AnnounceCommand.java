package me.kiminouso.simpleannouncer.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.kiminouso.simpleannouncer.SimpleAnnouncer;
import me.tippie.tippieutils.commands.TippieCommand;
import me.tippie.tippieutils.functions.ColorUtils;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnounceCommand extends TippieCommand {
    public AnnounceCommand() {
        super.subLevel = 1;
        super.name = "announce";
        super.description = "Manually announce a message! Supports PlaceholderAPI & Hovering";
        super.permission = "announcer.announce";
    }

    @Override
    public void executes(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NoSuchMethodException {
        if (args.length < 1) {
            sender.sendMessage("§8[§9SimpleAnnouncer§8] §cUsage: /announce <message>.");
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("announcer.bypass"))
                return;

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                message = PlaceholderAPI.setPlaceholders(player, message);
            }

            message = Stream.of(ColorUtils.translateColorCodes('&', message))
                    .map(component -> component.toLegacyText())
                    .collect(Collectors.joining());

            TextComponent finalMessage = new TextComponent(message);

            if (message.contains("{") && message.contains("}")) {
                String hoverMessage = StringUtils.substringBetween(message, "{", "}");

                finalMessage = new TextComponent(message.replace("{" + StringUtils.substringBetween(message, "{", "}") + "}", ""));
                finalMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Stream.of(ColorUtils.translateColorCodes('&',
                                SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).translateHoverMessage(hoverMessage)))
                        .map(component -> component.toLegacyText())
                        .collect(Collectors.joining()))));
            }

            player.spigot().sendMessage(finalMessage);
        }
    }
}
