package me.nyarikori.weightOfItems.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Component;
import me.nyarikori.weightOfItems.service.ConfigService;
import me.nyarikori.weightOfItems.service.weight.WeightService;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author NyariKori
 */
@Component
public class PlayerWeightPlaceholder extends PlaceholderExpansion {
    @Autowired
    private WeightService weightService;
    @Autowired
    private ConfigService configService;

    @Override
    public @NotNull String getIdentifier() {
        return "weightofitems";
    }

    @Override
    public @NotNull String getAuthor() {
        return "nyarikori";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getVersion() {
        return "";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player.getName() == null || player.getName().isEmpty()) {
            return null;
        }

        if (params.equalsIgnoreCase("player_weight")) {
            Player onlinePlayer = player.getPlayer();
            if (onlinePlayer == null) {
                return "Player offline.";
            }

            return String.format("%.2f", weightService.getPlayerWeight(onlinePlayer));
        }

        if (params.equalsIgnoreCase("player_weight_colored")) {
            Player onlinePlayer = player.getPlayer();
            if (onlinePlayer == null) {
                return "Player offline.";
            }

            double playerWeight = weightService.getPlayerWeight(onlinePlayer);

            switch (weightService.getWeightType(onlinePlayer)) {
                case BASE_WEIGHT -> {
                    return "<green>" + String.format("%.2f", playerWeight);
                }
                case MEDIUM_WEIGHT -> {
                    return "<yellow>" + String.format("%.2f", playerWeight);
                }
                case HIGH_WEIGHT -> {
                    return "<red>" + String.format("%.2f", playerWeight);
                }
                case MAX_WEIGHT -> {
                    return "<dark_red>" + String.format("%.2f", playerWeight);
                }
            }
        }

        if (params.equalsIgnoreCase("max_weight")) {
            return configService.get("max-weight").toString();
        }

        if (params.equalsIgnoreCase("high_weight")) {
            return configService.get("high-weight").toString();
        }

        if (params.equalsIgnoreCase("medium_weight")) {
            return configService.get("medium-weight").toString();
        }

        return null;
    }
}
