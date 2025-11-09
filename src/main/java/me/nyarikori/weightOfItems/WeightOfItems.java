package me.nyarikori.weightOfItems;

import me.nyarikori.bukkit.injector.BukkitInjectorInitializer;
import me.nyarikori.commons.container.DependencyContainer;
import me.nyarikori.weightOfItems.listener.InventoryListener;
import me.nyarikori.weightOfItems.listener.JoinListener;
import me.nyarikori.weightOfItems.listener.MoveListener;
import me.nyarikori.weightOfItems.listener.QuitListener;
import me.nyarikori.weightOfItems.placeholder.PlayerWeightPlaceholder;
import me.nyarikori.weightOfItems.provider.CustomDependencyProvider;
import me.nyarikori.weightOfItems.service.LocalizationService;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author NyariKori
 */
public final class WeightOfItems extends JavaPlugin {

    @Override
    public void onEnable() {
        BukkitInjectorInitializer.setProvider(new CustomDependencyProvider(this));
        BukkitInjectorInitializer.init(this, "me.nyarikori");

        registerEvents();

        getLogger().info(((LocalizationService) DependencyContainer.getDependency(LocalizationService.class))
                .getMessage("enabled-message"));

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            ((PlayerWeightPlaceholder) DependencyContainer.getDependency(PlayerWeightPlaceholder.class)).register();
            getLogger().info(((LocalizationService) DependencyContainer.getDependency(LocalizationService.class))
                    .getMessage("placeholderapi-hook"));
        } else {
            getLogger().warning(((LocalizationService) DependencyContainer.getDependency(LocalizationService.class))
                    .getMessage("unsuccessful-placeholderapi-hook"));
        }
    }

    @Override
    public void onDisable() {
        getLogger().info(((LocalizationService) DependencyContainer.getDependency(LocalizationService.class))
                .getMessage("disabled-message"));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents((Listener) DependencyContainer.getDependency(JoinListener.class), this);
        getServer().getPluginManager().registerEvents((Listener) DependencyContainer.getDependency(QuitListener.class), this);
        getServer().getPluginManager().registerEvents((Listener) DependencyContainer.getDependency(InventoryListener.class), this);
        getServer().getPluginManager().registerEvents((Listener) DependencyContainer.getDependency(MoveListener.class), this);
    }
}
