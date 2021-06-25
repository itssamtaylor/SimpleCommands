package ca.samueltaylor.taylor_commands.helper;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Set;

public class Teleport
{ 
	public static HashMap<ServerPlayerEntity, Location> playerBackMap = new HashMap<ServerPlayerEntity, Location>();

	/**
	 * Send player to location.
	 * @param exact: use doubles when it's true;
	 */
	public static void warp(Entity player, ServerWorld world, Location location, boolean exact) {
		double x = location.x, y = location.y, z = location.z;
		float yaw = location.yaw, pitch = location.pitch;
		if (!exact) {
			x += 0.5D;
			z += 0.5D;
		}
		BlockPos blockPos = new BlockPos(x, y, z);
		if (!World.isValid(blockPos)) {
			throw new InvalidParameterException("Invalid block position: " + blockPos);
		} else {
			float f = MathHelper.wrapDegrees(yaw);
			float g = MathHelper.wrapDegrees(pitch);
			if (player instanceof ServerPlayerEntity) {
				ChunkPos chunkPos = new ChunkPos(new BlockPos(x, y, z));
				world.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, player.getId());
				player.stopRiding();
				if (((ServerPlayerEntity)player).isSleeping()) {
					((ServerPlayerEntity)player).wakeUp(true, true);
				}

				if (world == player.world) {
					((ServerPlayerEntity)player).networkHandler.requestTeleport(x, y, z, f, g);
				} else {
					((ServerPlayerEntity)player).teleport(world, x, y, z, f, g);
				}

				player.setHeadYaw(f);
			} else {
				float h = MathHelper.clamp(g, -90.0F, 90.0F);
				if (world == player.world) {
					player.refreshPositionAndAngles(x, y, z, f, h);
					player.setHeadYaw(f);
				} else {
					player.detach();
					Entity entity = player;
					player = player.getType().create(world);
					if (player == null) {
						return;
					}

					player.copyFrom(entity);
					player.refreshPositionAndAngles(x, y, z, f, h);
					player.setHeadYaw(f);
					entity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
					world.onDimensionChanged(player);
				}
			}

			if (!(player instanceof LivingEntity) || !((LivingEntity)player).isFallFlying()) {
				player.setVelocity(player.getVelocity().multiply(1.0D, 0.0D, 1.0D));
				player.setOnGround(true);
			}

			if (player instanceof PathAwareEntity) {
				((PathAwareEntity)player).getNavigation().stop();
			}
		}
	}
/*	*//**
	 * @param name: the name of target warp point. player to warp point
	 *//*
	public static void warp(PlayerEntity player, PlayerEntity target)
	{
		if (target.dimension != player.dimension)
			MyCommandBase.transDimension(player, new(target.playerLocation);
		Location loc = new Location(target);
		warp(player, loc, false);
	}*/

	public static boolean goBack(ServerPlayerEntity player)
	{
		Location loc;
		if (playerBackMap.containsKey(player))
		{
			loc = playerBackMap.get(player);
			warp(player, player.getServerWorld(), loc, true);
			return true;
		} 
		else
		{
			return false;
		}
	}
}

