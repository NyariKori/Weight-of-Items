package me.nyarikori.weightOfItems.command;

import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.nyarikori.commons.annotation.Autowired;
import me.nyarikori.commons.annotation.Component;
import me.nyarikori.commons.annotation.command.CommandType;
import me.nyarikori.commons.annotation.command.NCommand;
import me.nyarikori.weightOfItems.service.ConfigService;
import me.nyarikori.weightOfItems.service.LocalizationService;
import me.nyarikori.weightOfItems.service.weight.WeightService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

@Component
@NCommand(commandType = CommandType.LITE_COMMANDS)
@Command(name = "weightofitems", aliases = {"woi", "weightitems", "weights"})
public class WeightOfItemsCommand {
    @Autowired
    private MiniMessage miniMessage;
    @Autowired
    private ConfigService configService;
    @Autowired
    private WeightService weightService;
    @Autowired
    private LocalizationService localizationService;

    @Async
    @Execute
    @Permission("weightofitems.reload")
    void reload(@Context CommandSender sender) {
        configService.reload();
        weightService.reload();
        localizationService.reload();
        sender.sendMessage(miniMessage.deserialize(configService.get("reload-message")));
    }
}
