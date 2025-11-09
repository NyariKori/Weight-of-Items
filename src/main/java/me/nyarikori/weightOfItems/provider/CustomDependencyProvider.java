package me.nyarikori.weightOfItems.provider;

import me.nyarikori.commons.provider.DependencyProvider;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author NyariKori
 */
public class CustomDependencyProvider extends DependencyProvider {
    public CustomDependencyProvider(@NotNull JavaPlugin plugin) {
        registerDependency(Plugin.class, plugin);
        registerDependency(JavaPlugin.class, plugin);

        registerDependency(MiniMessage.class, MiniMessage.miniMessage());
    }
}
