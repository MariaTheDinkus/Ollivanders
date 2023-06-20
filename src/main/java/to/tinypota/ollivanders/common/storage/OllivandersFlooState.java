package to.tinypota.ollivanders.common.storage;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class OllivandersFlooState extends State {
	public OllivandersFlooState(OllivandersServerState serverState) {
		super(serverState);
	}
	
	@Override
	public NbtCompound writeToNbt(NbtCompound nbt) {
		flooPositions.forEach((name, flooPosStorage) -> nbt.put(name, flooPosStorage.writeToNbt(new NbtCompound())));
		return nbt;
	}
	
	@Override
	public void fromNbt(OllivandersServerState serverState, NbtCompound nbt) {
		flooPositions.clear();
		nbt.getKeys().forEach(name -> {
			var flooCompound = nbt.getCompound(name);
			var storage = FlooPosStorage.fromNbt(flooCompound);
			flooPositions.put(name, storage);
		});
	}
	
	private HashMap<String, FlooPosStorage> flooPositions = new HashMap<>();
	
	public HashMap<String, FlooPosStorage> getFlooPositions() {
		return flooPositions;
	}
	
	public void setFlooPositions(HashMap<String, FlooPosStorage> flooPositions) {
		this.flooPositions = flooPositions;
		markDirty();
	}
	
	public void addFlooPosition(String name, BlockPos pos, Direction direction, Identifier dimension, boolean chosenRandomly) {
		flooPositions.put(name.toLowerCase(), new FlooPosStorage(pos, direction, dimension, chosenRandomly));
		markDirty();
	}
	
	public void removeFlooPosition(String name) {
		flooPositions.remove(name.toLowerCase());
		markDirty();
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
		markDirty();
	}
	
	@Nullable
	public FlooPosStorage getFlooByNameOrRandom(String name) {
		var flooPosByName = getFlooPosByName(name.toLowerCase());
		
		if (flooPosByName.isPresent()) {
			return flooPosByName.get();
		} else {
			var randomFlooPos = getRandomFlooPos();
			
			return randomFlooPos.orElse(null);
		}
	}
	
	@Nullable
	public Optional<FlooPosStorage> getFlooPosByName(String name) {
		var entry = flooPositions.entrySet()
																		 .stream()
																		 .filter(e -> e.getKey().equalsIgnoreCase(name))
																		 .findFirst();
		
		return entry.map(Map.Entry::getValue);
	}
	
	public Optional<String> getFlooNameByPos(BlockPos pos) {
		var entry = flooPositions.entrySet()
								 .stream()
								 .filter(e -> e.getValue().getPos().equals(pos))
								 .findFirst();
		
		return entry.map(Map.Entry::getKey);
	}
	
	public Optional<FlooPosStorage> getRandomFlooPos() {
		var values = flooPositions.values();
		var filteredValues = values.stream().filter(flooPosStorage -> flooPosStorage.isChosenRandomly()).collect(Collectors.toList());
		return filteredValues.isEmpty() ? Optional.empty() : Optional.of(filteredValues.get((int) (filteredValues.size() * Math.random())));
	}
}