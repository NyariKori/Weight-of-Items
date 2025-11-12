package me.nyarikori.weightOfItems.service.weight;

import me.nyarikori.commons.Lifecycle;
import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Service;
import me.nyarikori.weightOfItems.service.ConfigService;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeaknessService implements Lifecycle {
    @Autowired
    private ConfigService configService;

    private final List<String> playersWithWeakness = new ArrayList<>();

    public void addWeakness(@NotNull WeightType weightType, @NotNull Player player) {
        switch (weightType) {
            case BASE_WEIGHT:
                return;
            case MEDIUM_WEIGHT:
                createWeakness(player, configService.get("weakness-amplifier-medium-weight"));
                break;
            case HIGH_WEIGHT:
                createWeakness(player, configService.get("weakness-amplifier-high-weight"));
                break;
            case MAX_WEIGHT:
                createWeakness(player, configService.get("weakness-amplifier-max-weight"));
                break;
        }
    }

    private void createWeakness(Player player, int amplifier) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,
                Integer.MAX_VALUE, amplifier, false, configService.get("use-weakness-particles")));

        playersWithWeakness.add(player.getName());
    }

    public void removeWeakness(@NotNull Player player) {
        if (playersWithWeakness.contains(player.getName())) {
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            playersWithWeakness.remove(player.getName());
        }
    }
}
