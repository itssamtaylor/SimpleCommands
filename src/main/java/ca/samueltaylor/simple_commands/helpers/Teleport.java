package ca.samueltaylor.simple_commands.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Objects;

public class Teleport {
    protected static HashMap<PlayerEntity, Location> playerLocations = new HashMap<PlayerEntity, Location>();

    protected Location location;
    protected BlockPos blockPos;
    protected ServerWorld world;
    protected Entity player;
    protected float pitchDegrees;
    protected float yawDegrees;
    protected float pitchClamp;

    public Teleport(Entity player, ServerWorld world, Location location) {
        this.location = location;
        this.world = world;
        this.player = player;

        this.blockPos = new BlockPos(location.posX, location.posY, location.posZ);
        this.pitchDegrees = MathHelper.wrapDegrees(location.pitch);
        this.yawDegrees = MathHelper.wrapDegrees(location.yaw);
        this.pitchClamp = MathHelper.clamp(this.pitchDegrees, -90.0F, 90.0F);

        if(!World.isValid(blockPos)) {
            throw new InvalidParameterException("Invalid block position: " + blockPos);
        }
    }

    public Teleport(Entity player, Location location) {
        this(player, (ServerWorld) player.world, location);
    }

    public void teleport() {
        if(this.player instanceof PlayerEntity) {
            this.logCurrentLocation((PlayerEntity) this.player);
        }

        if(this.player instanceof ServerPlayerEntity) {
            this.teleportAsServerPlayerEntity((ServerPlayerEntity) player);
        } else {
            this.teleportAsEntity();
        }

        this.player.setHeadYaw(this.yawDegrees);

        if(!(this.player instanceof LivingEntity) || !((LivingEntity) this.player).isFallFlying()) {
            this.player.setVelocity(this.player.getVelocity().multiply(1.0D, 0.0D, 1.0D));
            this.player.setOnGround(true);
        }

        if(this.player instanceof PathAwareEntity) {
            ((PathAwareEntity) this.player).getNavigation().stop();
        }
    }

    protected void logCurrentLocation(PlayerEntity player) {
        playerLocations.put(player, new Location(player));
    }

    protected void teleportAsServerPlayerEntity(ServerPlayerEntity player) {
        ChunkPos chunkPos = new ChunkPos(this.blockPos);
        this.world.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, player.getId());
        player.stopRiding();

        if (player.isSleeping()) {
            player.wakeUp(true, true);
        }

        if (this.world == player.world) {
            player.networkHandler.requestTeleport(this.location.x, this.location.y, this.location.z, this.yawDegrees, this.pitchDegrees);
        } else {
            player.teleport(this.world, this.location.x, this.location.y, this.location.z, this.yawDegrees, this.pitchDegrees);
        }

        this.player = player;
    }

    protected void teleportAsEntity() {
        if(this.world == this.player.world) {
            this.player.refreshPositionAndAngles(this.location.x, this.location.y, this.location.z, this.yawDegrees, this.pitchClamp);
            this.player.setHeadYaw(this.yawDegrees);
        } else {
            this.player.detach();

            Entity entity = this.player;
            this.player = this.player.getType().create(this.world);

            if(this.player == null) {
                return;
            }

            this.player.copyFrom(entity);
            this.player.refreshPositionAndAngles(this.location.x, this.location.y, this.location.z, this.yawDegrees, this.pitchClamp);
            entity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
            this.world.onDimensionChanged(this.player);
        }
    }

    public static void teleport(Entity player, ServerWorld world, Location location) {
        new Teleport(player, world, location).teleport();
    }

    public static boolean back(PlayerEntity player) {
        if(playerLocations.containsKey(player)) {
            Location location = playerLocations.get(player);
            teleport(player, Objects.requireNonNull(player.getServer()).getWorld(location.dimension), location);
            playerLocations.remove(player);
            return true;
        }
        return false;
    }

}
