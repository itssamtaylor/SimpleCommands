package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.BaseCommand;
import ca.samueltaylor.simple_commands.helpers.Chat;
import ca.samueltaylor.simple_commands.helpers.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class Back extends BaseCommand {
    static {
        command = "back";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity player = commandContext.getSource().getPlayer();
        Chat chat = new Chat(player);

        if(Teleport.back(player)) {
            chat.send("Warped to previous location.");
        } else {
            chat.send("No previous location found.");
        }

        return Command.SINGLE_SUCCESS;
    }
}
