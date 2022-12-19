package me.kiminouso.announcer;

import me.tippie.tippieutils.functions.ColorUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Text;

import java.awt.*;
import java.util.*;
import java.util.List;

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
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes(ChatColor.COLOR_CHAR, messages.get(rnd.nextInt(messages.size()))));
        }, 1L, getConfig().getInt("timer") * 1200L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
