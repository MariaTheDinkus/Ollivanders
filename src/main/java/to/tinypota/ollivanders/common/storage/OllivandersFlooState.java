package to.tinypota.ollivanders.common.storage;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class OllivandersFlooState {
	private HashMap<String, BlockPos> flooPositions = new HashMap<>();
	
	public HashMap<String, BlockPos> getFlooPositions() {
		return flooPositions;
	}
	
	public void setFlooPositions(HashMap<String, BlockPos> flooPositions) {
		this.flooPositions = flooPositions;
	}
	
	public void addFlooPosition(String name, BlockPos pos) {
		flooPositions.put(name, pos);
	}
	
	public void removeFlooPosition(String name) {
		flooPositions.remove(name);
	}
	
	public void removeFlooByPos(BlockPos pos) {
		var atomicName = new AtomicReference<String>();
		
		flooPositions.entrySet().forEach(entry -> {
			if (entry.getValue().equals(pos)) {
				atomicName.set(entry.getKey());
			}
		});
		
		if (atomicName.get() != null) {
			flooPositions.remove(atomicName.get());
		}
	}
	
	@Nullable
	public BlockPos getFlooByNameOrRandom(String name) {
		var flooPosByName = getFlooPosByName(name);
		
		if (flooPosByName != null) {
			return flooPosByName;
		} else {
			var randomFlooPos = getRandomFlooPos();
			
			if (randomFlooPos.isPresent()) {
				return randomFlooPos.get();
			} else {
				return null;
			}
		}
	}
	
	@Nullable
	public BlockPos getFlooPosByName(String name) {
		var returnPos = new AtomicReference<BlockPos>();
		flooPositions.entrySet().forEach(flooEntry -> {
			var flooName = flooEntry.getKey();
			var flooBlockPos = flooEntry.getValue();
			
			if (flooName.equals(name)) {
				returnPos.set(flooBlockPos);
			}
		});
		
		return returnPos.get();
	}
	
	public Optional<BlockPos> getRandomFlooPos() {
		var values = flooPositions.values();
		return values.stream().skip((int) (values.size() * Math.random())).findFirst();
	}
}