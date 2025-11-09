package me.nyarikori.weightOfItems.service;

import lombok.SneakyThrows;
import me.nyarikori.commons.Lifecycle;
import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Service;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author NyariKori
 */
@Service
public class LocalizationService implements Lifecycle {
    @Autowired
    private Plugin plugin;
    private String currentLocale = "en";

    private final List<File> localesFiles = new ArrayList<>();
    private final Map<String, String> localizationMap = new ConcurrentHashMap<>();

    @Override
    @SneakyThrows
    public void enable() {
        File localesDirectory = new File(plugin.getDataFolder(), "language");
        File localesDirectoryParentFile = localesDirectory.getParentFile();

        if(!localesDirectoryParentFile.exists() && !localesDirectoryParentFile.mkdir()) {
            plugin.getLogger().warning("[Localization] Locales folder creation error");
        }

        if (!localesDirectory.exists() && !localesDirectory.mkdir()) {
            plugin.getLogger().warning("[Localization] Locales folder creation error");
        }

        File configFile = new File(localesDirectory.getPath() + "/language.yml");
        if (!configFile.exists() && !configFile.createNewFile()) {
            plugin.getLogger().warning("[Localization] Failed to create a localization configuration file");
        }

        FileConfiguration localeConfiguration = YamlConfiguration.loadConfiguration(configFile);
        String locale = localeConfiguration.getString("locale");

        if (locale == null || locale.isEmpty()) {
            localeConfiguration.set("locale", "en");
            localeConfiguration.save(configFile);
        } else {
            currentLocale = locale;
        }

        plugin.saveResource("language/en.yml", false);

        File[] files = localesDirectory.listFiles();
        if (files == null) {
            plugin.getLogger().warning("[Localization] Missing locale files, creating...");
            plugin.getLogger().warning("[Localization] You need restart the server to fix this");
            return;
        }

        localesFiles.addAll(Arrays.asList(files));
        reload();
    }

    public void reload() {
        localizationMap.clear();

        Optional<File> optionalFile = localesFiles.stream().filter(file ->
                file.getName().equals(currentLocale + ".yml")).findFirst();

        if (optionalFile.isEmpty()) {
            plugin.getLogger().warning("[Localization] Localization file not found");
            return;
        }

        YamlConfiguration localeConfiguration = YamlConfiguration.loadConfiguration(optionalFile.get());
        localeConfiguration.getKeys(false).forEach(string -> localizationMap.put(string, localeConfiguration.getString(string)));
    }

    public String getMessage(@NotNull String index) {
        String result = localizationMap.get(index);

        if(result == null || result.isEmpty()) {
            return index;
        }

        return result;
    }
}
