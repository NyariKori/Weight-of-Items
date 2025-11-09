package me.nyarikori.weightOfItems.service;

import me.nyarikori.commons.Lifecycle;
import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Service;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author NyariKori
 */
@Service
public class WeightService implements Lifecycle {
    @Autowired
    private Plugin plugin;
    @Autowired
    private ConfigService configService;
    @Autowired
    private LocalizationService localizationService;

    private final Map<String, Integer> playersWeightMap = new ConcurrentHashMap<>();
    private final Map<Material, Integer> blockWeightMap = new ConcurrentHashMap<>();

    @Override
    public void enable() {
        reload();
        plugin.getLogger().warning(blockWeightMap.toString());
    }

    public void reload() {
        ConfigurationSection section = configService.get("weights");
        section.getKeys(false).forEach(string -> {
            blockWeightMap.put(Material.getMaterial(string), section.getInt(string));
        });
    }

    public void registerPlayer(@NotNull Player player) {
        playersWeightMap.put(player.getName(), calculatePlayerInventoryWeight(player));
    }

    public void unregisterPlayer(@NotNull Player player) {
        playersWeightMap.remove(player.getName());
    }

    public int calculatePlayerInventoryWeight(@NotNull Player player) {
        ItemStack[] items = player.getInventory().getContents();

        int weight = 0;

        for (ItemStack item: items) {
            if (item == null || item.isEmpty() || blockWeightMap.get(item.getType()) == null) {
                continue;
            }

            weight += blockWeightMap.get(item.getType());
        }

        return weight;
    }
}
