package me.kiminouso.simpleannouncer;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.tippie.tippieutils.functions.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class AnnouncementTask {
    private int cooldown;

    private final Runnable task = () -> {
        int size = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().size();

        if (size == 0) {
            Bukkit.getServer().getLogger().log(Level.INFO, "Attempting to re-cycle your announcements.");
            SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().addAll(SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getConfig().getStringList("messages"));
            Collections.shuffle(SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages());
        }

        Random rnd = new Random();
        String msg = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().get(rnd.nextInt(size));

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("announcer.bypass"))
                return;

            String finalMsg = msg;

            if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
                finalMsg = PlaceholderAPI.setPlaceholders(player, msg);

            player.sendMessage(Stream.of(ColorUtils.translateColorCodes('&', finalMsg)).map(component -> component.toLegacyText()).collect(Collectors.joining()));
        });

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
