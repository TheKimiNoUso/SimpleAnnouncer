package me.kiminouso.simpleannouncer;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.tippie.tippieutils.functions.ColorUtils;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class AnnouncementTask {
    private int cooldown;

    private final Runnable task = () -> {
        int size = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().size();

        // Re-cycle messages into the queue if the queue is empty
        if (size == 0) {
            Bukkit.getServer().getLogger().log(Level.INFO, "Attempting to re-cycle your announcements.");
            SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().addAll(SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getConfig().getStringList("messages"));
            Collections.shuffle(SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages());
        }

        // Choose a random message from the queue
        Random rnd = new Random();
        String msg = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().get(rnd.nextInt(size));

        // Iterate over all players in order to send the announcement to them
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Check if the player shouldn't see the message
            if (player.hasPermission("announcer.bypass"))
                return;

            String finalMsg = msg;

            // Set PlaceholderAPI placeholders if plugin exists
            if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
                finalMsg = PlaceholderAPI.setPlaceholders(player, msg);

            // Turn message into a Text Component
            String hoverMessage;
            TextComponent message = new TextComponent(Stream.of(ColorUtils.translateColorCodes('&', finalMsg)).map(component -> component.toLegacyText()).collect(Collectors.joining()));

            // Handle hover-able messages
            if (finalMsg.contains("{") && finalMsg.contains("}")) {
                hoverMessage = StringUtils.substringBetween(finalMsg, "{", "}");

                if (hoverMessage.contains("|")) {
                    StringBuilder builder = new StringBuilder();
                    String[] messages = hoverMessage.split("\\|");

                    int i = 0;

                    for (String s : messages) {
                        String localMsg = s;
                        localMsg = localMsg.replace("}", "");
                        localMsg = localMsg.replace("{", "");
                        builder.append(Stream.of(ColorUtils.translateColorCodes('&', localMsg))
                                .map(component -> component.toLegacyText())
                                .collect(Collectors.joining())
                        );

                        if (i++ != messages.length - 1) {
                            builder.append("\n");
                        }
                    }

                    hoverMessage = builder.toString();
                }

                message = new TextComponent(Stream.of(ColorUtils.translateColorCodes('&', finalMsg.replace("{" + StringUtils.substringBetween(finalMsg, "{", "}") + "}", "")))
                        .map(component -> component.toLegacyText())
                        .collect(Collectors.joining())
                );
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverMessage)));
            }

            // Send announcement message
            player.spigot().sendMessage(message);
        }

        // Remove message from the queue
        SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().remove(msg);
    };

    private BukkitTask activeTask = null;

    public void start() {
        if (activeTask != null)
            activeTask.cancel();

        activeTask = Bukkit.getScheduler().runTaskTimer(SimpleAnnouncer.getPlugin(SimpleAnnouncer.class), task, 1L, cooldown * 1200L);
    }

    public void end() {
        if (activeTask != null) {
            activeTask.cancel();
            activeTask = null;
        }
    }

    public boolean isActive() {
        return activeTask != null;
    }
}
