package to.tinypota.ollivanders.common.storage;

import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class OllivandersFlooState {
	private HashMap<String, Pair<BlockPos, Direction>> flooPositions = new HashMap<>();
	
	public HashMap<String, Pair<BlockPos, Direction>> getFlooPositions() {
		return flooPositions;
	}
	
	public void setFlooPositions(HashMap<String, Pair<BlockPos, Direction>> flooPositions) {
		this.flooPositions = flooPositions;
	}
	
	public void addFlooPosition(String name, BlockPos pos, Direction direction) {
		flooPositions.put(name, new Pair<>(pos, direction));
	}
	
	public void removeFlooPosition(String name) {
		flooPositions.remove(name);
	}
	
	public void removeFlooByPos(BlockPos pos) {
		var atomicName = new AtomicReference<String>();
		
		flooPositions.forEach((key, value) -> {
			if (value.getLeft().equals(pos)) {
				atomicName.set(key);
			}
		});
		
		if (atomicName.get() != null) {
			flooPositions.remove(atomicName.get());
		}
	}
	
	@Nullable
	public Pair<BlockPos, Direction> getFlooByNameOrRandom(String name) {
		var flooPosByName = getFlooPosByName(name);
		
		if (flooPosByName != null) {
			return flooPosByName;
		} else {
			var randomFlooPos = getRandomFlooPos();
			
			return randomFlooPos.orElse(null);
		}
	}
	
	@Nullable
	public Pair<BlockPos, Direction> getFlooPosByName(String name) {
		var returnPos = new AtomicReference<Pair<BlockPos, Direction>>();
		flooPositions.forEach((flooName, pair) -> {
			
			if (flooName.equals(name)) {
				returnPos.set(pair);
			}
		});
		
		return returnPos.get();
	}
	
	public String getFlooNameByPos(BlockPos pos) {
		var returnPos = new AtomicReference<String>();
		flooPositions.forEach((flooName, pair) -> {
			if (pair.getLeft().equals(pos)) {
				returnPos.set(flooName);
			}
		});
		
		return returnPos.get();
	}
	
	public Optional<Pair<BlockPos, Direction>> getRandomFlooPos() {
		var values = flooPositions.values();
		return values.stream().skip((int) (values.size() * Math.random())).findFirst();
	}
}