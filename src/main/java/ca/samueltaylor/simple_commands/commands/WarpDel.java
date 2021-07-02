package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.RequiredStringArgument;
import ca.samueltaylor.simple_commands.helpers.Chat;
import ca.samueltaylor.simple_commands.points.WarpPointManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class WarpDel extends RequiredStringArgument {
    public WarpDel() {
        command = "delwarp";
        argumentName = "WarpPointName";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity player = commandContext.getSource().getPlayer();
        String warpPointName = StringArgumentType.getString(commandContext, argumentName);
        Chat chat = new Chat(player);

        WarpPointManager.instance().delete(warpPointName);
        chat.send("Warp point " + warpPointName + " deleted!");

        this.logCommand(commandContext);
        return Command.SINGLE_SUCCESS;
    }
}
