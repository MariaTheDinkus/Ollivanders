package to.tinypota.ollivanders.common.storage;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.dimension.DimensionTypes;

public class FlooPosStorage {
	private BlockPos pos;
	private Direction direction;
	private Identifier dimension;
	private boolean chosenRandomly;
	
	public FlooPosStorage(BlockPos pos, Direction direction, Identifier dimension, boolean chosenRandomly) {
		this.pos = pos;
		this.direction = direction;
		this.dimension = dimension;
		this.chosenRandomly = chosenRandomly;
	}
	
	public NbtCompound writeToNbt(NbtCompound nbt) {
		nbt.put("pos", NbtHelper.fromBlockPos(getPos()));
		nbt.putInt("direction", getDirection().getId());
		nbt.putString("dimension", getDimension().toString());
		nbt.putBoolean("chosenRandomly", isChosenRandomly());
		
		return nbt;
	}
	
	public static FlooPosStorage fromNbt(NbtCompound nbt) {
		var blockPos = NbtHelper.toBlockPos(nbt.getCompound("pos"));
		var direction = Direction.byId(nbt.getInt("direction")) != null ? Direction.byId(nbt.getInt("direction")) : Direction.NORTH;
		var dimensionString = nbt.getString("dimension");
		var dimension = dimensionString.isEmpty() ? DimensionTypes.OVERWORLD_ID : new Identifier(dimensionString);
		var chosenRandomly = nbt.getBoolean("chosenRandomly");
		
		return new FlooPosStorage(blockPos, direction, dimension, chosenRandomly);
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public Identifier getDimension() {
		return dimension;
	}
	
	public boolean isChosenRandomly() {
		return chosenRandomly;
	}
	
	public void setPos(BlockPos pos) {
		this.pos = pos;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public void setDimension(Identifier dimension) {
		this.dimension = dimension;
	}
}