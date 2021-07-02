package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.BaseCommand;
import ca.samueltaylor.simple_commands.helpers.Chat;
import ca.samueltaylor.simple_commands.helpers.TeleportRequests;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class TeleportAccept extends BaseCommand {
    static {
        command = "tpaccept";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity player = commandContext.getSource().getPlayer();
        PlayerEntity requester = TeleportRequests.accept(player);

        Chat.send(player, "Teleported " + requester.getName() + " to your location");
        Chat.send(requester, "Teleported to " + player.getName());

        return Command.SINGLE_SUCCESS;
    }
}
