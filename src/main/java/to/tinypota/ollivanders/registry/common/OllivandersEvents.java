package to.tinypota.ollivanders.registry.common;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class OllivandersEvents {
	public static void init() {
	
	}
	
	public static List<BlockPos> getBlocksInBoundingBox(Entity entity) {
		var blockPositions = new ArrayList<BlockPos>();
		
		var boundingBox = entity.getBoundingBox();
		var minX = (int) Math.floor(boundingBox.minX);
		var maxX = (int) Math.ceil(boundingBox.maxX);
		var minZ = (int) Math.floor(boundingBox.minZ);
		var maxZ = (int) Math.ceil(boundingBox.maxZ);
		
		for (int x = minX; x < maxX; x++) {
			for (int z = minZ; z < maxZ; z++) {
				blockPositions.add(new BlockPos(x, (int) (entity.getY() + 1), z));
			}
		}
		
		return blockPositions;
	}
}
