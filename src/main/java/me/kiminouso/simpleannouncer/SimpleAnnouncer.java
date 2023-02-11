/* Authored by TheKimiNoUso 2022 */
package me.kiminouso.simpleannouncer;

import java.util.*;
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
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleAnnouncer extends JavaPlugin {
    @Getter
    private final Map<String, TextComponent> announcements = new HashMap<>();

    @Getter
    private final AnnouncementTask announcementTask = new AnnouncementTask();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Metrics metrics = new Metrics(this, 17120);

        getServer().getScheduler().runTaskLater(this, announcementTask::start, 90L);

        Bukkit.getPluginCommand("announcer").setExecutor(new AnnouncerBaseCommand());

        // region Migrate Legacy Configs

        if (getConfig().contains("messages")) {
            getConfig().set("messages", null);
            migrateLegacy(Bukkit.getConsoleSender());
        } else {
            loadAnnouncements();
        }

        // endregion Migrate Legacy Configs

        // region Load
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
            getServer()
                    .getLogger()
                    .log(
                            Level.INFO,
                            "Found PlaceholderAPI soft dependency! Any placeholders will be set in announcements.");

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
    public void loadAnnouncements() {
        announcements.clear();

        List<String> announcementIds = new ArrayList<>(getConfig().getConfigurationSection("announcements").getKeys(false));
        Collections.shuffle(announcementIds);

        for (String id : announcementIds) {
            // Turns announcement into Text Component
            String message = getConfig().getString("announcements." + id + ".message");

            message = Stream.of(ColorUtils.translateColorCodes('&', message))
                    .map(component -> component.toLegacyText())
                    .collect(Collectors.joining());
            TextComponent msg = new TextComponent(message);

            List<String> hoverMessage = getConfig().getStringList("announcements." + id + ".hover");

            // Handle hover-able messages
            if (getConfig().contains("announcements." + id + ".hover")) {
                msg.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new Text(Stream.of(ColorUtils.translateColorCodes('&', String.join("\n", hoverMessage)))
                                .map(component -> component.toLegacyText())
                                .collect(Collectors.joining()))));
            }
            announcements.put(id, msg);
        }
    }

    public String getRandomAnnouncementId() {
        if (announcements.isEmpty())
            loadAnnouncements();

        Random rnd = new Random();
        List<String> validIds = announcements.keySet().stream().toList();

        return validIds.get(rnd.nextInt(0, validIds.size()));
    }

    private String generateId() {
        String[] letters = {
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
                "v", "w", "x", "y", "z"
        };

        StringBuilder id = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) {
            id.append(letters[rnd.nextInt(0, letters.length)]);
        }

        return id.toString();
    }

    private void migrateLegacy(CommandSender sender) {
        List<String> legacy;

        try {
            legacy = getConfig().getStringList("messages");
        } catch (Exception ignored) {
            sender.sendMessage("§cNo messages left to migrate!");
            return;
        }

        for (String string : legacy) {
            String i = generateId();
            String message = string;
            String[] hoverMessages = null;

            // Handle hover-able messages
            if (string.contains("{") && string.contains("}")) {
                String hoverRaw = StringUtils.substringBetween(string, "{", "}");
                hoverMessages = hoverRaw.split("\\|");

                message = message.replace("{" + hoverRaw + "}", "");
            }

            getConfig().set("announcements." + i + ".message", message);
            if (hoverMessages != null) {
                getConfig().set("announcements." + i + ".hover", Arrays.stream(hoverMessages).toList());
            }
        }

        saveConfig();
    }

    public void reload() {
        if (announcementTask.isActive()) announcementTask.end();

        reloadConfig();
        loadAnnouncements();

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
