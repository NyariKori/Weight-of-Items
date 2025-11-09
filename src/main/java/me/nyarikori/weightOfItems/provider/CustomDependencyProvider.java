package me.nyarikori.weightOfItems.provider;

import me.nyarikori.commons.provider.DependencyProvider;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author NyariKori
 */
public class WOIDependencyProvider extends DependencyProvider {
    public WOIDependencyProvider(@NotNull JavaPlugin plugin) {
        registerDependency(JavaPlugin.class, plugin);
        registerDependency(Plugin.class, plugin);
    }
}
