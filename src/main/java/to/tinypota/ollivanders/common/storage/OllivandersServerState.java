package to.tinypota.ollivanders.common.storage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.registry.common.OllivandersCores;
import to.tinypota.ollivanders.registry.common.OllivandersItems;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.HashMap;
import java.util.UUID;

public class OllivandersServerState extends PersistentState {
	private HashMap<UUID, OllivandersPlayerState> players = new HashMap<>();
 
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
		NbtCompound playersNbtCompound = new NbtCompound();
		players.forEach((UUID, playerState) -> {
			NbtCompound playerStateNbt = new NbtCompound();
			playerStateNbt.putString("suitedCore", playerState.getSuitedCore());
			playerStateNbt.putString("currentSpell", playerState.getCurrentSpell());
			playersNbtCompound.put(String.valueOf(UUID), playerStateNbt);
		});
		nbt.put("players", playersNbtCompound);
        return nbt;
    }    
 
    public static OllivandersServerState createFromNbt(NbtCompound tag) {
        OllivandersServerState serverState = new OllivandersServerState();
		NbtCompound playersTag = tag.getCompound("players");
		playersTag.getKeys().forEach(key -> {
			NbtCompound playerTag = playersTag.getCompound(key);
			OllivandersPlayerState playerState = new OllivandersPlayerState();
		
			playerState.setSuitedCore(playerTag.getString("suitedCore"));
			playerState.setCurrentSpell(playerTag.getString("currentSpell"));
		
			UUID uuid = UUID.fromString(key);
			serverState.players.put(uuid, playerState);
		});
        return serverState;
    }
	
	public static String getSuitedWand(LivingEntity player) {
		OllivandersServerState serverState = getServerState(player.getWorld().getServer());
		OllivandersPlayerState playerState = getPlayerState(player);
		var suitedWand = playerState.getSuitedWand();
		if (suitedWand.isEmpty()) {
			playerState.setSuitedWand(Registries.ITEM.getId(OllivandersItems.WANDS.getRandom(player)).toString());
			serverState.markDirty();
			return playerState.getSuitedWand();
		} else {
			return suitedWand;
		}
	}
	
	public static void setSuitedWand(LivingEntity player, String suitedWand) {
		OllivandersServerState serverState = getServerState(player.getWorld().getServer());
		OllivandersPlayerState playerState = getPlayerState(player);
		playerState.setSuitedWand(suitedWand);
		serverState.markDirty();
	}
	
	public static String getSuitedCore(LivingEntity player) {
		OllivandersServerState serverState = getServerState(player.getWorld().getServer());
		OllivandersPlayerState playerState = getPlayerState(player);
		var suitedCore = playerState.getSuitedCore();
		if (suitedCore.isEmpty()) {
			playerState.setSuitedCore(OllivandersRegistries.CORE.getId(OllivandersCores.CORES.getRandom(player)).toString());
			serverState.markDirty();
			return playerState.getSuitedCore();
		} else {
			return suitedCore;
		}
	}
	
	public static void setSuitedCore(LivingEntity player, String suitedCore) {
		OllivandersServerState serverState = getServerState(player.getWorld().getServer());
		OllivandersPlayerState playerState = getPlayerState(player);
		playerState.setSuitedCore(suitedCore);
		serverState.markDirty();
	}
	
	public static String getCurrentSpell(LivingEntity player) {
		OllivandersPlayerState playerState = getPlayerState(player);
		return playerState.getCurrentSpell();
	}
	
	public static void setCurrentSpell(LivingEntity player, String currentSpell) {
		OllivandersServerState serverState = getServerState(player.getWorld().getServer());
		OllivandersPlayerState playerState = getPlayerState(player);
		playerState.setCurrentSpell(currentSpell);
		serverState.markDirty();
	}
	
	public static OllivandersServerState getServerState(MinecraftServer server) {
		PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
		OllivandersServerState serverState = persistentStateManager.getOrCreate(OllivandersServerState::createFromNbt, OllivandersServerState::new, Ollivanders.ID);
		return serverState;
	}
	
	public static OllivandersPlayerState getPlayerState(LivingEntity player) {
		OllivandersServerState serverState = getServerState(player.getWorld().getServer());
		OllivandersPlayerState playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new OllivandersPlayerState());
		return playerState;
	}
}