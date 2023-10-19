package to.tinypota.ollivanders.common.storage;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.util.SpellHelper;
import to.tinypota.ollivanders.common.util.WandHelper;
import to.tinypota.ollivanders.registry.common.OllivandersCores;
import to.tinypota.ollivanders.registry.common.OllivandersItems;
import to.tinypota.ollivanders.registry.common.OllivandersNetworking;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.HashMap;
import java.util.UUID;

public class OllivandersServerState extends PersistentState {
	private final HashMap<UUID, OllivandersPlayerState> players = new HashMap<>();
	private final OllivandersFlooState flooState = new OllivandersFlooState(this);
	private final OllivandersVanishingCabinetState vanishingCabinetState = new OllivandersVanishingCabinetState(this);
	
	//TODO: Clean up all this fucking shit.
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		var playersNbtCompound = new NbtCompound();
		players.forEach((uuid, playerState) -> {
			var stateNbt = playerState.writeToNbt(new NbtCompound());
			playersNbtCompound.put(String.valueOf(uuid), stateNbt);
		});
		nbt.put("players", playersNbtCompound);
		nbt.put("floo", flooState.writeToNbt(new NbtCompound()));
		nbt.put("vanishingCabinet", vanishingCabinetState.writeToNbt(new NbtCompound()));
		return nbt;
	}
	
	public static OllivandersServerState readNbt(NbtCompound tag) {
		var serverState = new OllivandersServerState();
		var playersTag = tag.getCompound("players");
		playersTag.getKeys().forEach(key -> {
			var playerTag = playersTag.getCompound(key);
			var playerState = new OllivandersPlayerState(serverState);
			playerState.fromNbt(serverState, playerTag);
			var uuid = UUID.fromString(key);
			serverState.players.put(uuid, playerState);
		});
		
		var flooPosCompound = tag.getCompound("floo");
		serverState.flooState.fromNbt(serverState, flooPosCompound);
		
		var vanishingCabinetCompound = tag.getCompound("vanishingCabinet");
		serverState.vanishingCabinetState.fromNbt(serverState, vanishingCabinetCompound);
		return serverState;
	}
	
	public void syncPowerLevels(ServerPlayerEntity player) {
		var playerState = getPlayerState(player);
		var currentSpellPowerLevel= playerState.getCurrentSpellPowerLevel();
		var powerLevel = playerState.getPowerLevel();
		var castPercentage = SpellHelper.getCastPercentage(player);
		if (playerState.getCurrentSpell().equals(Spell.EMPTY.getCastName())) {
			castPercentage = -1;
		}
		var buf = PacketByteBufs.create();
		buf.writeInt(currentSpellPowerLevel.getId());
		buf.writeInt(powerLevel.getId());
		buf.writeDouble(castPercentage);
		ServerPlayNetworking.send(player, OllivandersNetworking.SYNC_POWER_LEVELS, buf);
	}
	
	private static Type<OllivandersServerState> type = new Type<>(
			OllivandersServerState::new, // If there's no 'StateSaverAndLoader' yet create one
			OllivandersServerState::readNbt, // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
			null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
	);
	
	public static OllivandersServerState getServerState(MinecraftServer server) {
		var persistentStateManager = server.getOverworld().getPersistentStateManager();
		
		OllivandersServerState state = persistentStateManager.getOrCreate(type, Ollivanders.ID);
		
		state.markDirty();
		
		return state;
	}
	
	public static OllivandersPlayerState getPlayerState(MinecraftServer server, PlayerEntity player) {
		var serverState = getServerState(server);
		return serverState.players.computeIfAbsent(player.getUuid(), uuid -> {
			var state = new OllivandersPlayerState(serverState);
			state.setSuitedCore(OllivandersRegistries.CORE.getId(OllivandersCores.CORES.getRandom(player)).toString());
			state.setSuitedWand(Registries.ITEM.getId(OllivandersItems.WANDS.getRandom(player)).toString());
			return state;
		});
	}
	
	public OllivandersPlayerState getPlayerState(PlayerEntity player) {
		return players.computeIfAbsent(player.getUuid(), uuid -> {
			var state = new OllivandersPlayerState(this);
			state.setSuitedCore(OllivandersRegistries.CORE.getId(OllivandersCores.CORES.getRandom(player)).toString());
			state.setSuitedWand(Registries.ITEM.getId(OllivandersItems.WANDS.getRandom(player)).toString());
			return state;
		});
	}
	
	public OllivandersFlooState getFlooState() {
		return flooState;
	}
	
	public OllivandersVanishingCabinetState getVanishingCabinetState() {
		return vanishingCabinetState;
	}
}