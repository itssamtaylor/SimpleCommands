package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.RequiredStringArgument;
import ca.samueltaylor.simple_commands.helpers.Chat;
import ca.samueltaylor.simple_commands.points.WarpPoint;
import ca.samueltaylor.simple_commands.points.WarpPointManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Objects;

public class Warp extends RequiredStringArgument {
    public Warp() {
        command = "warp";
        argumentName = "WarpPointName";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        String warpPointName = StringArgumentType.getString(commandContext, argumentName);
        PlayerEntity player = commandContext.getSource().getPlayer();
        WarpPoint warpPoint = WarpPointManager.instance().getPoint(warpPointName);
        Chat chat = new Chat(player);

        if(warpPoint == null) {
            if(!warpPointName.equals("")) {
                chat.send(warpPointName + " not found!");
            }

            chat.send("Available warp points: " + WarpPointManager.instance().listPoints());
            return Command.SINGLE_SUCCESS;
        }

        warpPoint.teleport(player, Objects.requireNonNull(player.getServer()).getWorld(warpPoint.location.dimension));

        this.logCommand(commandContext);
        return Command.SINGLE_SUCCESS;
    }
}
