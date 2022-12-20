package me.kiminouso.simpleannouncer;

import lombok.Getter;
import lombok.Setter;
import me.tippie.tippieutils.functions.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class AnnouncementTask {
    private int cooldown;

    private final Runnable task = () -> Bukkit.getServer().getScheduler().runTaskTimer(SimpleAnnouncer.getPlugin(SimpleAnnouncer.class), () -> {
        int size = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().size();

        if (size == 0) {
            Bukkit.getServer().getLogger().log(Level.INFO, "Attempting to re-cycle your announcements.");
            SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().addAll(SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getConfig().getStringList("messages"));
        }

        Random rnd = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getRandom();
        String msg = SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().get(rnd.nextInt(size));

        Bukkit.broadcastMessage(Stream.of(ColorUtils.translateColorCodes('&', msg)).map(component -> component.toLegacyText()).collect(Collectors.joining()));
        SimpleAnnouncer.getPlugin(SimpleAnnouncer.class).getMessages().remove(msg);
    }, 1L, cooldown * 1200L);

    private BukkitTask activeTask = null;

    public void start() {
        if (activeTask != null)
            activeTask.cancel();

        activeTask = Bukkit.getScheduler().runTaskTimer(SimpleAnnouncer.getPlugin(SimpleAnnouncer.class), task, 0, 20L);
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
