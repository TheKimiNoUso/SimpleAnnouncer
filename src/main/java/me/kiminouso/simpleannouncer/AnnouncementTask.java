/* Authored by TheKimiNoUso 2022 */
package me.kiminouso.simpleannouncer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.tippie.tippieutils.functions.ColorUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@Getter
@Setter
public class AnnouncementTask {
    private int cooldown;

    private final Runnable task = () -> {
        // Choose a random message from the queue (Re-cycles old messages if the list is empty)
        String id = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getRandomAnnouncementId();

        TextComponent msg = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getAnnouncements().get(id);

        // Iterate over all players in order to send the announcement to them
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Check if the player shouldn't see the message
            if (player.hasPermission("announcer.bypass")) return;

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
        SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getAnnouncements().remove(id);
    };

    private BukkitTask activeTask = null;

    public void start() {
        if (activeTask != null) activeTask.cancel();

        activeTask = Bukkit.getScheduler()
                .runTaskTimer(SimpleAnnouncer.getPlugin(SimpleAnnouncer.class), task, 1L, cooldown * 1200L);
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
