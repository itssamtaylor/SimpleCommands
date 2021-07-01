package ca.samueltaylor.simple_commands.points;

import ca.samueltaylor.simple_commands.helpers.Location;
import ca.samueltaylor.simple_commands.helpers.Teleport;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

abstract public class Point {

    public String name;
    public Location location;

    public Point(String name, JsonObject json) {
        this(name, new Location(json.get("location").getAsJsonObject()));
    }

    public Point(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Point(String name, PlayerEntity player) {
        this(name, new Location(player));
    }

    public void teleport(Entity entity, World world) {
        new Teleport(entity, (ServerWorld) world, this.location).teleport();
    }
}
