package ca.samueltaylor.taylor_commands.helper;


import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

/**
 * contain a chunk location x,y,z
 * and a exact position posX,posY,posZ
 *
 * z and posZ represents height.
 */
public class Location 
{
	public int x,y,z;
	public double posX,posY,posZ;
	public float yaw = 0.0F, pitch = 0.0F;
	public RegistryKey<World> dimension;
	//private BlockPos pos;
	
	public Location(BlockPos pos, RegistryKey<World> dimension) {
		init(pos.getX(),pos.getY(),pos.getZ(), 0.0F, 0.0F,dimension);
	}
	public Location(int x, int y, int z, float yaw, float pitch, RegistryKey<World> dimension)
	{
		this.x = x;
		this.y = y;
		this.z = z;

		this.posX = x;
		this.posY = y;
		this.posZ = z;

		this.yaw = yaw;
		this.pitch = pitch;

		this.dimension = dimension;
	}

	public Location(int x, int y, int z, RegistryKey<World> dimension)
	{
		this(x,y,z,0.0F,0.0F,dimension);
	}

	public Location(double posX, double posY, double posZ, float yaw, float pitch, RegistryKey<World> dimension)
	{
		init(posX, posY, posZ, yaw, pitch, dimension);
	}

	public Location(double posX, double posY, double posZ, RegistryKey<World> dimension)
	{
		this(posX, posY, posZ, 0.0F, 0.0F, dimension);
	}

	/**
	 * construct the location the player is currently at
	 */
	public Location(PlayerEntity player)
	{
		init(player);
	}

	/**
	 * construct a location from where the player is looking at.
	 */
	public Location(PlayerEntity player, String s)
	{
		init(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch(), player.world.getRegistryKey());
	}
	/**
	 * construct a location from another location's toString().
	 */
	public Location(String info)
	{
		String[] part = info.split("[,]");
		try
		{
			init(Double.parseDouble(part[0]), Double.parseDouble(part[1]), Double.parseDouble(part[2]), 0.0F, 0.0F, World.OVERWORLD);
		}
		catch(Exception e)
		{
			System.err.println("Exception on attempting to rebuild Location from String.");
			init(0,0,0, 0.0F, 0.0F, World.OVERWORLD);
		}
	}

	private void init(PlayerEntity player)
	{
		this.x = round(player.getX());
		this.y = round(player.getY());
		this.z = round(player.getZ());

		this.posX = player.getX();
		this.posY = player.getY();
		this.posZ = player.getZ();

		this.yaw = player.getYaw();
		this.pitch = player.getPitch();

		this.dimension = player.world.getRegistryKey();
	}

	private void init(double posX, double posY, double posZ, float yaw, float pitch, RegistryKey<World> i)
	{
		this.x = round(posX);
		this.y = round(posY);
		this.z = round(posZ);

		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;

		this.yaw = yaw;
		this.pitch = pitch;

		this.dimension = i;
	}

	public void setSpawn(PlayerEntity player)
	{
		player.getServer().getWorld(player.getEntityWorld().getRegistryKey()).setSpawnPos(player.getBlockPos(), player.limbAngle);
	}

	/**
	 * floor then cast to int.
	 */
	private static int round(double pos)
	{
		return (int)Math.floor(pos);
	}

	public String toString()
	{
		return posX + "," + posY + "," + posZ + "," + dimension;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Location)
		{
			Location location = (Location)o;
			boolean equal = true;
			equal = equal && this.posX == location.posX;
			equal = equal && this.posY == location.posY;
			equal = equal && this.posZ == location.posZ;
			equal = equal && this.dimension == location.dimension;
			return equal;
		}
		return false;
	}
	
	public BlockPos getPosfrom() {
		BlockPos pos1 = new BlockPos(this.x,this.y, this.z);
		
		return pos1;
		
	}
}
