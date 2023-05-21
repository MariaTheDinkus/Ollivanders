package to.tinypota.ollivanders.common.storage;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.Ollivanders;

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
		
		flooPositions.forEach((key, value) -> {
			if (value.equals(pos)) {
				atomicName.set(key);
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
			
			return randomFlooPos.orElse(null);
		}
	}
	
	@Nullable
	public BlockPos getFlooPosByName(String name) {
		var returnPos = new AtomicReference<BlockPos>();
		flooPositions.forEach((flooName, flooBlockPos) -> {
			
			if (flooName.equals(name)) {
				returnPos.set(flooBlockPos);
			}
		});
		
		return returnPos.get();
	}
	
	public String getFlooNameByPos(BlockPos pos) {
		var returnPos = new AtomicReference<String>();
		flooPositions.forEach((flooName, flooBlockPos) -> {
			if (flooBlockPos.equals(pos)) {
				returnPos.set(flooName);
			}
		});
		
		return returnPos.get();
	}
	
	public Optional<BlockPos> getRandomFlooPos() {
		var values = flooPositions.values();
		return values.stream().skip((int) (values.size() * Math.random())).findFirst();
	}
}