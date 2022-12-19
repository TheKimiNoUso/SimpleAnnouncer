package me.kiminouso.announcer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Announcer extends JavaPlugin {

    private List<String> messages = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("reloadannouncer").setExecutor(new ReloadMessagesCommand());

        Random rnd = new Random();

        saveDefaultConfig();
        messages.clear();
        messages.addAll(getConfig().getStringList("messages"));

        Collections.shuffle(messages);

        getServer().getScheduler().runTaskTimer(this, () -> {
            Bukkit.broadcastMessage(messages.get(rnd.nextInt(messages.size())));
        }, 1L, getConfig().getInt("timer") * 1200L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
