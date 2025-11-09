package me.nyarikori.weightOfItems.service;

import me.nyarikori.commons.Lifecycle;
import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Service;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author NyariKori
 */
@Service
public class ConfigService implements Lifecycle {
    @Autowired
    private Plugin plugin;
    private final Map<String, Object> configurationMap = new ConcurrentHashMap<>();

    @Override
    public void enable() {
        plugin.saveDefaultConfig();
        reload();
    }

    public void reload() {
        plugin.reloadConfig();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("configuration");

        if(section == null) {
            plugin.getLogger().severe("[ConfigService] The config does not contain a configuration section.");
            return;
        }

        section.getKeys(false).forEach(string -> configurationMap.put(string, plugin.getConfig()
                .get("configuration." + string)));
    }

    @SuppressWarnings("all")
    public <T> @NotNull T get(@NotNull String index) {
        T object = (T) configurationMap.get(index);

        if(object == null) {
            plugin.getLogger().severe("[ConfigService] index dont exists!" + index);
        }

        return object;
    }

    public boolean contains(@NotNull String index) {
        return configurationMap.get(index) != null;
    }

    public <T> void set(@NotNull String index, @NotNull T value) {
        plugin.getConfig().set("configuration." + index, value);
        plugin.saveConfig();
    }
}
