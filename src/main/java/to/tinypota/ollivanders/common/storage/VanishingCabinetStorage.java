package to.tinypota.ollivanders.common.storage;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.UUID;

public class VanishingCabinetStorage {
	private Direction direction;
	private Identifier dimension;
	private String uuid;
	
	public VanishingCabinetStorage(Direction direction, Identifier dimension, String uuid) {
		this.direction = direction;
		this.dimension = dimension;
		this.uuid = uuid;
	}
	
	public VanishingCabinetStorage(Direction direction, Identifier dimension) {
		this(direction, dimension, "");
	}
	
	public NbtCompound writeToNbt(NbtCompound nbt) {
		nbt.putInt("direction", getDirection().getId());
		nbt.putString("dimension", getDimension().toString());
		nbt.putString("uuid", getUuid());
		
		return nbt;
	}
	
	public static VanishingCabinetStorage fromNbt(NbtCompound nbt) {
		var direction = Direction.byId(nbt.getInt("direction")) != null ? Direction.byId(nbt.getInt("direction")) : Direction.NORTH;
		var dimensionString = nbt.getString("dimension");
		var dimension = dimensionString.isEmpty() ? DimensionTypes.OVERWORLD_ID : new Identifier(dimensionString);
		var uuid = nbt.getString("uuid");
		
		return new VanishingCabinetStorage(direction, dimension, uuid);
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public Identifier getDimension() {
		return dimension;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setDimension(Identifier dimension) {
		this.dimension = dimension;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}