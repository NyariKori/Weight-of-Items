package me.nyarikori.weightOfItems.service.weight;

import me.nyarikori.commons.Lifecycle;
import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Service;
import me.nyarikori.weightOfItems.service.ConfigService;
import me.nyarikori.weightOfItems.service.LocalizationService;
import me.nyarikori.weightOfItems.service.PlaceholderAPIService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
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
    private MiniMessage miniMessage;

    @Autowired
    private ConfigService configService;
    @Autowired
    private LocalizationService localizationService;
    @Autowired
    private PlaceholderAPIService placeholderAPIService;

    private final Map<String, Double> playersWeightMap = new ConcurrentHashMap<>();
    private final Map<Material, Double> blockWeightMap = new ConcurrentHashMap<>();

    @Override
    public void enable() {
        reload();
        if (!(boolean) configService.get("use-actionbar")) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().getOnlinePlayers().forEach(player -> player.sendActionBar(
                        miniMessage.deserialize(placeholderAPIService.setPlaceholders(player, localizationService.getMessage("actionbar-text")))));
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    public void reload() {
        ConfigurationSection section = configService.get("weights");
        section.getKeys(false).forEach(string -> blockWeightMap.put(Material.getMaterial(string), section.getDouble(string)));
    }

    public void registerPlayer(@NotNull Player player) {
        playersWeightMap.put(player.getName(), calculatePlayerInventoryWeight(player));
    }

    public void unregisterPlayer(@NotNull Player player) {
        playersWeightMap.remove(player.getName());
    }

    public double calculatePlayerInventoryWeight(@NotNull Player player) {
        ItemStack[] items = player.getInventory().getContents();

        double weight = 0;

        for (ItemStack item: items) {
            if (item == null || item.isEmpty() || blockWeightMap.get(item.getType()) == null) {
                continue;
            }

            weight += blockWeightMap.get(item.getType())  * item.getAmount();
        }

        return weight;
    }

    public void updatePlayerWeight(@NotNull Player player) {
        playersWeightMap.replace(player.getName(), calculatePlayerInventoryWeight(player));
    }

    public WeightType getWeightType(@NotNull Player player) {
        double inventoryWeight = playersWeightMap.get(player.getName());

        double mediumWeight = configService.get("medium-weight");
        double highWeight = configService.get("high-weight");
        double maxWeight = configService.get("max-weight");

        int code = (inventoryWeight < 0) ? -1 :
                (inventoryWeight < mediumWeight) ? 0 :
                (inventoryWeight < highWeight) ? 1 :
                (inventoryWeight < maxWeight) ? 2 : 3;

        return switch (code) {
            case 1 -> WeightType.MEDIUM_WEIGHT;
            case 2 -> WeightType.HIGH_WEIGHT;
            case 3 -> WeightType.MAX_WEIGHT;
            default -> WeightType.BASE_WEIGHT;
        };
    }

    public double getPlayerWeight(@NotNull Player player) {
        return playersWeightMap.get(player.getName());
    }
}
