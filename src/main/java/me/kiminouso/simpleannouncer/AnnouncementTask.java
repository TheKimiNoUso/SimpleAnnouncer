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
        if (size == 0)
            SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).loadAnnouncements(
                    SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getConfig().getStringList("messages"));

        // Choose a random message from the queue
        Random rnd = new Random();
        TextComponent msg = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().get(rnd.nextInt(size));

        // Iterate over all players in order to send the announcement to them
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Check if the player shouldn't see the message
            if (player.hasPermission("announcer.bypass"))
                return;

            String text = msg.getText();

            // Set PlaceholderAPI placeholders if plugin exists
            if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
                text = PlaceholderAPI.setPlaceholders(player, text);

            msg.setText(Stream.of(ColorUtils.translateColorCodes('&', text))
                    .map(component -> component.toLegacyText())
                    .collect(Collectors.joining()));

            player.spigot().sendMessage(msg);
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
