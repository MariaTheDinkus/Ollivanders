package to.tinypota.ollivanders.common.storage;

import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class FlooPosStorage {
	private BlockPos pos;
	private Direction direction;
	private Identifier dimension;
	
	public FlooPosStorage(BlockPos pos, Direction direction, Identifier dimension) {
		this.pos = pos;
		this.direction = direction;
		this.dimension = dimension;
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