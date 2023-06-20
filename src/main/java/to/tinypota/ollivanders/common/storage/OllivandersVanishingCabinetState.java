package to.tinypota.ollivanders.common.storage;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class OllivandersVanishingCabinetState extends State {
	public OllivandersVanishingCabinetState(OllivandersServerState serverState) {
		super(serverState);
	}
	
	@Override
	public NbtCompound writeToNbt(NbtCompound nbt) {
		vanishingCabinetPositions.forEach((pos, vanishingCabinetStorage) -> nbt.put(String.valueOf(pos.asLong()), vanishingCabinetStorage.writeToNbt(new NbtCompound())));
		return nbt;
	}
	
	@Override
	public void fromNbt(OllivandersServerState serverState, NbtCompound nbt) {
		vanishingCabinetPositions.clear();
		nbt.getKeys().forEach(name -> {
			var vanishingCabinetCompound = nbt.getCompound(name);
			var storage = VanishingCabinetStorage.fromNbt(vanishingCabinetCompound);
			var pos = BlockPos.fromLong(Long.parseLong(name));
			vanishingCabinetPositions.put(pos, storage);
		});
	}
	
	private HashMap<BlockPos, VanishingCabinetStorage> vanishingCabinetPositions = new HashMap<>();
	
	public HashMap<BlockPos, VanishingCabinetStorage> getVanishingCabinetPositions() {
		return vanishingCabinetPositions;
	}
	
	public void setVanishingCabinetPositions(HashMap<BlockPos, VanishingCabinetStorage> vanishingCabinetPositions) {
		this.vanishingCabinetPositions = vanishingCabinetPositions;
		markDirty();
	}
	
	public void setVanishingCabinet(BlockPos pos, Direction direction, Identifier dimension) {
		vanishingCabinetPositions.put(pos, new VanishingCabinetStorage(direction, dimension));
		markDirty();
	}
	
	public void setVanishingCabinet(BlockPos pos, Direction direction, Identifier dimension, String uuid) {
		vanishingCabinetPositions.put(pos, new VanishingCabinetStorage(direction, dimension, uuid));
		markDirty();
	}
	
	public void removeVanishingCabinet(BlockPos pos) {
		vanishingCabinetPositions.remove(pos);
		markDirty();
	}
	
	@Nullable
	public Optional<VanishingCabinetStorage> getVanishingCabinet(BlockPos pos) {
		return Optional.ofNullable(vanishingCabinetPositions.get(pos));
	}
	
	public String getUuid(BlockPos pos) {
		var vanishingCabinetStorage = getVanishingCabinet(pos);
		return vanishingCabinetStorage.map(VanishingCabinetStorage::getUuid).orElse("");
	}
	
	public void setUuid(BlockPos pos, String uuid) {
		var vanishingCabinetStorage = getVanishingCabinet(pos);
		vanishingCabinetStorage.ifPresent(storage -> {
			storage.setUuid(uuid);
			markDirty();
		});
	}
	
	public BlockPos findMatchingCabinet(BlockPos pos) {
		for (Map.Entry<BlockPos, VanishingCabinetStorage> entry : vanishingCabinetPositions.entrySet()) {
			var searchCabinetStorage = serverState.getVanishingCabinetState().getVanishingCabinet(pos);
			if (searchCabinetStorage.isPresent() && !searchCabinetStorage.get().getUuid().isEmpty()) {
				BlockPos entryPos = entry.getKey();
				VanishingCabinetStorage cabinetStorage = entry.getValue();
				
				if (!entryPos.equals(pos) && !cabinetStorage.getUuid().isEmpty() && cabinetStorage.getUuid().equals(getUuid(pos))) {
					return entryPos;
				}
			}
		}
		
		return null;
	}
}