/* Authored by TheKimiNoUso 2022 */
package me.kiminouso.simpleannouncer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import me.kiminouso.simpleannouncer.commands.*;
import me.tippie.tippieutils.functions.ColorUtils;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleAnnouncer extends JavaPlugin {
    @Getter
    private final List<TextComponent> messages = new ArrayList<>();

    @Getter
    private final AnnouncementTask announcementTask = new AnnouncementTask();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Metrics metrics = new Metrics(this, 17120);
        getServer().getScheduler().runTaskLater(this, announcementTask::start, 90L);

        Bukkit.getPluginCommand("announcer").setExecutor(new AnnouncerBaseCommand());

        // region Load
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            getServer()
                    .getLogger()
                    .log(
                            Level.INFO,
                            "Found PlaceholderAPI soft dependency! Any placeholders will be set in announcements.");

        loadAnnouncements(getConfig().getStringList("messages"));

        if (getConfig().getStringList("messages").size() == 0)
            getServer().getLogger().log(Level.SEVERE, "You don't have any messages registered in your config.yml!");

        try {
            announcementTask.setCooldown(getConfig().getInt("timer"));
        } catch (NumberFormatException e) {
            announcementTask.setCooldown(1);
            getServer()
                    .getLogger()
                    .log(
                            Level.SEVERE,
                            "Setting timer to 1 minute by default due to an error in your config.yml. Please check your 'timer:' path.");
            e.printStackTrace();
        }

        if (getConfig().getBoolean("send-auto-announcements", true)) {
            if (!announcementTask.isActive()) announcementTask.start();
        }
        // endregion Load
    }

    @Override
    public void onDisable() {
        if (announcementTask.isActive()) announcementTask.end();
    }

    /**
     * This function will load all the announcements in the messages path in
     * config.yml to a TextComponent and store them in a list. This stores raw
     * text without any kind of colouring or placeholder replacements.
     */
    public void loadAnnouncements(List<String> announcements) {
        messages.clear();

        for (String string : announcements) {
            // Turns announcement into Text Component
            String hoverMessage;
            string = Stream.of(ColorUtils.translateColorCodes('&', string))
                    .map(component -> component.toLegacyText())
                    .collect(Collectors.joining());
            TextComponent msg = new TextComponent(string);

            // Handle hover-able messages
            if (string.contains("{") && string.contains("}")) {
                hoverMessage = StringUtils.substringBetween(string, "{", "}");

                msg.setText(string.replace("{" + StringUtils.substringBetween(string, "{", "}") + "}", ""));
                msg.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new Text(Stream.of(ColorUtils.translateColorCodes('&', translateHoverMessage(hoverMessage)))
                                .map(component -> component.toLegacyText())
                                .collect(Collectors.joining()))));
            }
            messages.add(msg);
        }

        Collections.shuffle(messages);
    }

    public String translateHoverMessage(String string) {
        StringBuilder builder = new StringBuilder();
        String[] messages = string.split("\\|");

        int i = 0;

        for (String s : messages) {
            String localMsg = s;
            localMsg = localMsg.replace("}", "");
            localMsg = localMsg.replace("{", "");
            builder.append(Stream.of(ColorUtils.translateColorCodes('&', localMsg))
                    .map(component -> component.toLegacyText())
                    .collect(Collectors.joining()));

            if (i++ != messages.length - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    public void reload() {
        if (announcementTask.isActive()) announcementTask.end();

        reloadConfig();
        loadAnnouncements(getConfig().getStringList("messages"));

        if (getConfig().getBoolean("send-auto-announcements"))
            getServer().getScheduler().runTaskLater(this, announcementTask::start, 10L);
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

    public void setDelay(int newDelay) {
        getConfig().set("timer", newDelay);
        saveConfig();
        announcementTask.setCooldown(newDelay);
        getServer().getScheduler().runTaskLater(this, this::reload, 3 * 20L);
    }
}
