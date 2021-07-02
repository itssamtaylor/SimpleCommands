package ca.samueltaylor.simple_commands.points;

import ca.samueltaylor.simple_commands.helpers.Location;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;

public class HomePoint extends Point {
    public HomePoint(String name, JsonObject json) {
        super(name, json);
    }

    public HomePoint(String name, Location location) {
        super(name, location);
    }

    public HomePoint(String name, PlayerEntity player) {
        super(name, player);
    }
}
