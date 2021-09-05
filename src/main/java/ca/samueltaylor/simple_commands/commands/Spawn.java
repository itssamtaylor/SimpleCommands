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
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Objects;

public class Spawn extends BaseCommand {
    public Spawn() {
        command = "spawn";
    }

    @Override
    public int execute(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        ServerPlayerEntity player = commandContext.getSource().getPlayer();
        RegistryKey<World> worldKey = World.OVERWORLD;
        ServerWorld world = Objects.requireNonNull(player.getServer()).getWorld(worldKey);
        int x = Objects.requireNonNull(world).getLevelProperties().getSpawnX();
        int y = Objects.requireNonNull(world).getLevelProperties().getSpawnY();
        int z = Objects.requireNonNull(world).getLevelProperties().getSpawnZ();

        Teleport.teleport(player, world, new Location(x, y, z, worldKey));

        this.logCommand(commandContext);
        return Command.SINGLE_SUCCESS;
    }
}
