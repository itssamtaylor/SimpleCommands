package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.BaseCommand;
import ca.samueltaylor.simple_commands.helpers.Chat;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class SpawnSet extends BaseCommand {
    public SpawnSet() {
        command = "setspawn";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        ServerPlayerEntity player = commandContext.getSource().getPlayer();
        ServerWorld world = commandContext.getSource().getWorld();
        Chat chat = new Chat(player);

        world.setSpawnPos(player.getBlockPos().up(), player.limbAngle);
        chat.send("Spawn set!");

        this.logCommand(commandContext);
        return Command.SINGLE_SUCCESS;
    }
}
