package ca.samueltaylor.simple_commands.commands;

import ca.samueltaylor.simple_commands.abstract_commands.BaseCommand;
import ca.samueltaylor.simple_commands.helpers.Location;
import ca.samueltaylor.simple_commands.helpers.Teleport;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Objects;

public class Spawn extends BaseCommand {
    static {
        command = "spawn";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        ServerPlayerEntity player = commandContext.getSource().getPlayer();
        ServerWorld world = commandContext.getSource().getWorld();
        int x = world.getLevelProperties().getSpawnX();
        int y = world.getLevelProperties().getSpawnY();
        int z = world.getLevelProperties().getSpawnZ();

        Teleport.teleport(player, Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD), new Location(x, y, z, World.OVERWORLD));
        return Command.SINGLE_SUCCESS;
    }
}
