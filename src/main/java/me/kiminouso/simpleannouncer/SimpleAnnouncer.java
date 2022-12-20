package me.kiminouso.simpleannouncer;

import lombok.Getter;
import lombok.Setter;
import me.kiminouso.simpleannouncer.commands.AddMessageCommand;
import me.kiminouso.simpleannouncer.commands.ListMessagesCommand;
import me.kiminouso.simpleannouncer.commands.ReloadMessagesCommand;
import me.kiminouso.simpleannouncer.commands.RemoveMessageCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public final class SimpleAnnouncer extends JavaPlugin {
    @Getter
    @Setter
    private final List<String> messages = new ArrayList<>();
    @Getter
    private final Random random = new Random();
    private final AnnouncementTask announcementTask = new AnnouncementTask();

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 17120);
        getServer().getScheduler().runTaskLater(this, announcementTask::start, 90L);

        //region Commands
        Bukkit.getPluginCommand("reloadannouncer").setExecutor(new ReloadMessagesCommand());
        Bukkit.getPluginCommand("addmessage").setExecutor(new AddMessageCommand());
        Bukkit.getPluginCommand("listmessages").setExecutor(new ListMessagesCommand());
        Bukkit.getPluginCommand("removemessage").setExecutor(new RemoveMessageCommand());
        //endregion Commands

        //region Load
        saveDefaultConfig();
        messages.clear();
        messages.addAll(getConfig().getStringList("messages"));
        Collections.shuffle(messages);

        if (getConfig().getStringList("messages").size() == 0)
            getServer().getLogger().log(Level.SEVERE, "You don't have any messages registered in your config.yml!");

        try {
            announcementTask.setCooldown(getConfig().getInt("timer"));
        } catch (NumberFormatException e) {
            announcementTask.setCooldown(1);
            getServer().getLogger().log(Level.SEVERE, "Setting timer to 1 minute by default due to an error in your config.yml. Please check your 'timer:' path.");
            e.printStackTrace();
        }

        if (!announcementTask.isActive())
            announcementTask.start();
        //endregion Load
    }

    @Override
    public void onDisable() {
        if (announcementTask.isActive())
            announcementTask.end();
    }

    public void reload() {
        if (announcementTask.isActive())
            announcementTask.end();
        reloadConfig();
        messages.clear();
        messages.addAll(getConfig().getStringList("messages"));
        getServer().getScheduler().runTaskLater(this, announcementTask::start, 20L);
    }

    public void addMessage(String newMessage) {
        List<String> configList = getConfig().getStringList("messages");
        configList.add(newMessage);
        getConfig().set("messages", configList);
        saveConfig();
        getServer().getScheduler().runTaskLater(this, this::reload, 3 * 20L);
    }

    public void removeMessage(int index) {
        List<String> configList = getConfig().getStringList("messages");
        configList.remove(index);
        getConfig().set("messages", configList);
        saveConfig();
        getServer().getScheduler().runTaskLater(this, this::reload, 3 * 20L);
    }
}
