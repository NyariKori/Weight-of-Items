package me.nyarikori.weightOfItems.listener;

import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Component;
import me.nyarikori.weightOfItems.service.weight.WeightService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author NyariKori
 */
@Component
public class QuitListener implements Listener {
    @Autowired
    private WeightService weightService;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        weightService.unregisterPlayer(event.getPlayer());
    }
}
