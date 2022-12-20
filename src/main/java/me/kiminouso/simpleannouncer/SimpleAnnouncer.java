package me.kiminouso.simpleannouncer;

import me.kiminouso.simpleannouncer.commands.AddMessageCommand;
import me.kiminouso.simpleannouncer.commands.ListMessagesCommand;
import me.kiminouso.simpleannouncer.commands.ReloadMessagesCommand;
import me.kiminouso.simpleannouncer.commands.RemoveMessageCommand;
import me.tippie.tippieutils.functions.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SimpleAnnouncer extends JavaPlugin {

    private final List<String> messages = new ArrayList<>();
    private String previousMessage = "";

    @Override
    public void onEnable() {
        Random rnd = new Random();

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
        //endregion Load

        //region Task
        getServer().getScheduler().runTaskLater(this, () -> {
            getServer().getScheduler().runTaskTimer(this, () -> {
                String msg = messages.get(rnd.nextInt(messages.size()));

                if (messages.size() > 1) {
                    while (previousMessage.equals(msg)) {
                        msg = messages.get(rnd.nextInt(messages.size()));
                    }
                }

                Bukkit.broadcastMessage(Stream.of(ColorUtils.translateColorCodes('&', msg)).map(component -> component.toLegacyText()).collect(Collectors.joining()));
                previousMessage = msg;
            }, 1L, getConfig().getInt("timer") * 1200L);
        }, 3 * 20L);
        //endregion Task
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reload() {
        reloadConfig();
        messages.clear();
        messages.addAll(getConfig().getStringList("messages"));
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
