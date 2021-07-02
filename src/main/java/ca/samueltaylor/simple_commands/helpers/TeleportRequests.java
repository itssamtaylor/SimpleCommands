package ca.samueltaylor.simple_commands.helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;

public class TeleportRequests {

    protected static HashMap<PlayerEntity, PlayerEntity> requests = new HashMap<>();

    public static void add(PlayerEntity requestingPlayer, PlayerEntity askPlayer) {
        requests.put(askPlayer, requestingPlayer);
    }

    public static PlayerEntity accept(PlayerEntity player) {
        PlayerEntity requester = getRequestingPlayer(player);

        if(requester != null) {
            new Teleport(requester, (ServerWorld) player.world, new Location(player)).teleport();
            remove(player);
        }
        return requester;
    }

    public static HashMap<PlayerEntity, PlayerEntity> getRequests() {
        return requests;
    }

    public static PlayerEntity getRequestingPlayer(PlayerEntity player) {
        return requests.get(player);
    }

    public static PlayerEntity deny(PlayerEntity player) {
        PlayerEntity requester = getRequestingPlayer(player);
        remove(player);
        return requester;
    }

    public static void remove(PlayerEntity player) {
        requests.remove(player);
    }

}
