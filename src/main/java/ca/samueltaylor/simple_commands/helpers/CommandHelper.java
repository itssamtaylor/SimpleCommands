package ca.samueltaylor.simple_commands.helpers;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;
import java.util.Iterator;

public class CommandHelper {
    public static ServerPlayerEntity getPlayer(ServerCommandSource commandSource, Collection<ServerPlayerEntity> players) {
        Iterator<ServerPlayerEntity> playerIterator = players.iterator();
        ServerPlayerEntity player = null;

        while(playerIterator.hasNext()) {
            player = playerIterator.next();
        }

        return player;
    }
}
