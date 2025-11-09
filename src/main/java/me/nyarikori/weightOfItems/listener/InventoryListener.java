package me.nyarikori.weightOfItems.listener;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Component;
import me.nyarikori.weightOfItems.service.ConfigService;
import me.nyarikori.weightOfItems.service.weight.WeaknessService;
import me.nyarikori.weightOfItems.service.weight.WeightService;
import me.nyarikori.weightOfItems.service.weight.WeightType;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author NyariKori
 */
@Component
public class InventoryListener implements Listener {
    @Autowired
    private WeightService weightService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private WeaknessService weaknessService;

    @EventHandler
    public void onInventoryChange(PlayerInventorySlotChangeEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("weightofitems.bypass")) {
            return;
        }

        GameMode gameMode = player.getGameMode();

        if (gameMode.equals(GameMode.CREATIVE) && (boolean) configService.get("disable-weights-in-creative")
        ||  gameMode.equals(GameMode.SPECTATOR) && (boolean) configService.get("disable-weights-in-spectator")
        ||  gameMode.equals(GameMode.ADVENTURE) && (boolean) configService.get("disable-weights-in-adventure")) {
            return;
        }

        weightService.updatePlayerWeight(player);
        WeightType weightType = weightService.getWeightType(player);

        float mediumWeightSpeed = Float.parseFloat(configService.get("medium-weight-speed").toString());
        float highWeightSpeed = Float.parseFloat(configService.get("high-weight-speed").toString());
        float maxWeightSpeed = Float.parseFloat(configService.get("max-weight-speed").toString());

        switch (weightType) {
            case MEDIUM_WEIGHT:
                player.setWalkSpeed(mediumWeightSpeed);
                addWeakness(WeightType.MEDIUM_WEIGHT, player);
                break;

            case HIGH_WEIGHT:
                player.setWalkSpeed(highWeightSpeed);
                addWeakness(WeightType.HIGH_WEIGHT, player);
                break;

            case MAX_WEIGHT:
                player.setWalkSpeed(maxWeightSpeed);
                addWeakness(WeightType.MAX_WEIGHT, player);
                break;

            default:
                player.setWalkSpeed(0.2f);

                if (isShouldUseWeakness(WeightType.MEDIUM_WEIGHT) || isShouldUseWeakness(WeightType.HIGH_WEIGHT)
                        || isShouldUseWeakness(WeightType.MAX_WEIGHT)) {
                    weaknessService.removeWeakness(player);
                }

                break;
        }
    }

    private void addWeakness(WeightType weightType, Player player) {
        if (!isShouldUseWeakness(weightType)) {
            return;
        }

        weaknessService.addWeakness(weightType, player);
    }

    private boolean isShouldUseWeakness(WeightType weightType) {
        return configService.get("use-weakness-on-" + weightType.getConfigName() + "-weight");
    }
}
