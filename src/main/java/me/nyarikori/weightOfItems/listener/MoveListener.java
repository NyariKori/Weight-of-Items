package me.nyarikori.weightOfItems.listener;

import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Component;
import me.nyarikori.weightOfItems.service.weight.WeightService;
import me.nyarikori.weightOfItems.service.weight.WeightType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author NyariKori
 */
@Component
public class MoveListener implements Listener {
    @Autowired
    private WeightService weightService;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("weightofitems.bypass")) {
            return;
        }

        if (weightService.getWeightType(player).equals(WeightType.MAX_WEIGHT)) {
            event.setCancelled(true);
        }
    }
}
