package me.nyarikori.weightOfItems.service;

import me.clip.placeholderapi.PlaceholderAPI;
import me.nyarikori.commons.Lifecycle;
import me.nyarikori.commons.annotation.Service;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Service
public class PlaceholderAPIService implements Lifecycle {
    private boolean isPlaceholderUsing = false;

    @Override
    public void enable() {
        isPlaceholderUsing = Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public String setPlaceholders(@NotNull Player player, @NotNull String text) {
        if (isPlaceholderUsing) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }

        return text;
    }
}
