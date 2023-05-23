package to.tinypota.ollivanders.common.storage;

import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class OllivandersFlooState {
	private HashMap<String, FlooPosStorage> flooPositions = new HashMap<>();
	
	public HashMap<String, FlooPosStorage> getFlooPositions() {
		return flooPositions;
	}
	
	public void setFlooPositions(HashMap<String, FlooPosStorage> flooPositions) {
		this.flooPositions = flooPositions;
	}
	
	public void addFlooPosition(String name, BlockPos pos, Direction direction, Identifier dimension) {
		flooPositions.put(name, new FlooPosStorage(pos, direction, dimension));
	}
	
	public void removeFlooPosition(String name) {
		flooPositions.remove(name);
	}
	
	public void removeFlooByPos(BlockPos pos) {
		var atomicName = new AtomicReference<String>();
		
		flooPositions.forEach((key, value) -> {
			if (value.getPos().equals(pos)) {
				atomicName.set(key);
			}
		});
		
		if (atomicName.get() != null) {
			flooPositions.remove(atomicName.get());
		}
	}
	
	@Nullable
	public FlooPosStorage getFlooByNameOrRandom(String name) {
		var flooPosByName = getFlooPosByName(name);
		
		if (flooPosByName != null) {
			return flooPosByName;
		} else {
			var randomFlooPos = getRandomFlooPos();
			
			return randomFlooPos.orElse(null);
		}
	}
	
	@Nullable
	public FlooPosStorage getFlooPosByName(String name) {
		var returnPos = new AtomicReference<FlooPosStorage>();
		flooPositions.forEach((flooName, storage) -> {
			
			if (flooName.equals(name)) {
				returnPos.set(storage);
			}
		});
		
		return returnPos.get();
	}
	
	public String getFlooNameByPos(BlockPos pos) {
		var returnPos = new AtomicReference<String>();
		flooPositions.forEach((flooName, storage) -> {
			if (storage.getPos().equals(pos)) {
				returnPos.set(flooName);
			}
		});
		
		return returnPos.get();
	}
	
	public Optional<FlooPosStorage> getRandomFlooPos() {
		var values = flooPositions.values();
		return values.stream().skip((int) (values.size() * Math.random())).findFirst();
	}
}