package ca.samueltaylor.simple_commands.helpers;

import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class Location {
    public int x, y, z;
    public double posX, posY, posZ;
    public float pitch, yaw;
    public RegistryKey<World> dimension;

    public Location(int x, int y, int z, float pitch, float yaw, RegistryKey<World> dimension) {
        init(x, y, z, pitch, yaw, dimension);
    }

    public Location(double posX, double posY, double posZ, float pitch, float yaw, RegistryKey<World> dimension) {
        init(posX, posY, posZ, pitch, yaw, dimension);
    }

    public Location(PlayerEntity player) {
        this(player.getX(), player.getY(), player.getZ(), player.getPitch(), player.getYaw(), player.world.getRegistryKey());
    }

    public Location(BlockPos block, RegistryKey<World> dimension) {
        this(block.getX(), block.getY(), block.getZ(), 0.0F, 0.0F, dimension);
    }

    public Location(int x, int y, int z, RegistryKey<World> dimension) {
        this(x, y, z, 0.0F, 0.0F, dimension);
    }

    public Location(double posX, double posY, double posZ, RegistryKey<World> dimension) {
        this(posX, posY, posZ, 0.0F, 0.0F, dimension);
    }

    public Location(JsonObject json) {
        int x = json.get("x").getAsInt();
        int y = json.get("y").getAsInt();
        int z = json.get("z").getAsInt();
        float pitch = json.has("pitch") ? json.get("pitch").getAsFloat() : 0.0F;
        float yaw = json.has("yaw") ? json.get("yaw").getAsFloat() : 0.0F;
        RegistryKey<World> dimension = World.OVERWORLD;

        // TODO: Revise this check later to use field_25137 data from config as well
        if(json.has("dimension")) {
            JsonObject rootDimData = json.get("dimension").getAsJsonObject();
            if (rootDimData.has("field_25138")) {
                JsonObject fieldData = rootDimData.get("field_25138").getAsJsonObject();
                dimension = RegistryKey.of(Registry.WORLD_KEY, new Identifier(
                        fieldData.has("field_13353") ? fieldData.get("field_13353").getAsString() : "minecraft",
                        fieldData.has("field_13355") ? fieldData.get("field_13355").getAsString() : "overworld"
                ));
            }
        }

        init(x, y, z, pitch, yaw, dimension);
    }

    private void init(double posX, double posY, double posZ, float pitch, float yaw, RegistryKey<World> dimension) {
        this.x = (int) Math.floor(posX);
        this.y = (int) Math.floor(posY);
        this.z = (int) Math.floor(posZ);
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.pitch = pitch;
        this.yaw = yaw;
        this.dimension = dimension;
    }

    public String toString() {
        return this.posX + "," + this.posY + "," + this.posZ + "," + this.dimension;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("x", this.posX);
        json.addProperty("y", this.posY);
        json.addProperty("z", this.posZ);
        json.addProperty("pitch", this.pitch);
        json.addProperty("yaw", this.yaw);
        json.addProperty("dimension", this.dimension.toString());

        return json;
    }

    public boolean isSame(Location location) {
        return location.x == this.x && location.y == this.y && location.z == this.z && location.dimension == this.dimension;
    }
}
