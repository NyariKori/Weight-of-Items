package me.nyarikori.weightOfItems.listener;

import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Component;
import me.nyarikori.weightOfItems.service.weight.WeightService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author NyariKori
 */
@Component
public class JoinListener implements Listener {
    @Autowired
    private WeightService weightService;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        weightService.registerPlayer(event.getPlayer());
    }
}
