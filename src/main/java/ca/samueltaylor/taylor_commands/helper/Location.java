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
	public Identifier dimension;
	public RegistryKey<World> world;
	//private BlockPos pos;
	
	public Location(BlockPos pos, Identifier dimension) {
		init(pos.getX(),pos.getY(),pos.getZ(),dimension);
	}
	public Location(BlockPos pos, RegistryKey<World> dimension) {
		this(pos, dimension.getValue());
		this.world = dimension;
	}
	public Location(int x, int y, int z, Identifier dimension)
	{
		this.x = x;
		this.y = y;
		this.z = z;

		this.posX = x;
		this.posY = y;
		this.posZ = z;

		this.dimension = dimension;
	}
	public Location(int x, int y, int z, RegistryKey<World> dimension) {
		this(x, y, z, dimension.getValue());
		this.world = dimension;
	}

	public Location(double posX, double posY, double posZ, Identifier dimension)
	{
		init(posX, posY, posZ, dimension);
	}
	public Location(double posX, double posY, double posZ, RegistryKey<World> dimension)
	{
		this(posX, posY, posZ, dimension.getValue());
		this.world = dimension;
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
		init(player.getX(), player.getY(), player.getZ(), player.world.getRegistryKey().getValue());

	}
	/**
	 * construct a location from another location's toString().
	 */
	public Location(String info)
	{
		String[] part = info.split("[,]");
		try
		{
			init(Double.parseDouble(part[0]), Double.parseDouble(part[1]), Double.parseDouble(part[2]), new Identifier(part[3]));
		}
		catch(Exception e)
		{
			System.err.println("Exception on attempting to rebuild Location from String.");
			init(0,0,0, World.OVERWORLD.getValue());
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

		this.world = player.world.getRegistryKey();
		this.dimension = this.world.getValue();
	}

	private void init(double posX, double posY, double posZ, Identifier i)
	{
		this.x = round(posX);
		this.y = round(posY);
		this.z = round(posZ);

		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		

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
