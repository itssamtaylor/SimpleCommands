package ca.samueltaylor.simple_commands.points;

import ca.samueltaylor.simple_commands.helpers.Location;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;

public class WarpPoint extends Point {
    public WarpPoint(String name, JsonObject json) {
        super(name, json);
    }

    public WarpPoint(String name, Location location) {
        super(name, location);
    }

    public WarpPoint(String name, PlayerEntity player) {
        super(name, player);
    }
}
