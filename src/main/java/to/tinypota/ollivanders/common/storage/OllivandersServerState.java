package to.tinypota.ollivanders.common.storage;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.registry.common.OllivandersCores;
import to.tinypota.ollivanders.registry.common.OllivandersItems;
import to.tinypota.ollivanders.registry.common.OllivandersNetworking;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class OllivandersServerState extends PersistentState {
	private final HashMap<UUID, OllivandersPlayerState> players = new HashMap<>();
	private final OllivandersFlooState flooState = new OllivandersFlooState();
	
	//TODO: Clean up all this fucking shit.
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		var playersNbtCompound = new NbtCompound();
		players.forEach((uuid, playerState) -> {
			var stateNbt = playerState.writeToNbt(new NbtCompound());
			playersNbtCompound.put(String.valueOf(uuid), stateNbt);
		});
		nbt.put("players", playersNbtCompound);
		
		var flooPosCompound = new NbtCompound();
		flooState.getFlooPositions().forEach((name, storage) -> {
			flooPosCompound.put(name, FlooPosStorage.writeToNbt(storage, new NbtCompound()));
		});
		nbt.put("floo", flooPosCompound);
		return nbt;
	}
	
	public static OllivandersServerState readNbt(NbtCompound tag) {
		var serverState = new OllivandersServerState();
		var playersTag = tag.getCompound("players");
		playersTag.getKeys().forEach(key -> {
			var playerTag = playersTag.getCompound(key);
			var playerState = new OllivandersPlayerState();
			playerState.setSuitedWand(playerTag.getString("suitedWand"));
			playerState.setSuitedCore(playerTag.getString("suitedCore"));
			playerState.setCurrentSpell(playerTag.getString("currentSpell"));
			playerState.setCurrentSpellPowerLevel(SpellPowerLevel.byId(playerTag.getInt("currentSpellPowerLevel")));
			var skillLevels = playerTag.getCompound("skillLevels");
			playerState.getSkillLevels().clear();
			for (var spellId : skillLevels.getKeys()) {
				var identifier = new Identifier(spellId);
				var spell = OllivandersRegistries.SPELL.get(identifier);
				double skillLevel = skillLevels.getDouble(spellId);
				playerState.getSkillLevels().put(spell, skillLevel);
			}
			playerState.setPowerLevel(SpellPowerLevel.byId(playerTag.getInt("powerLevel")));
			var uuid = UUID.fromString(key);
			serverState.players.put(uuid, playerState);
		});
		
		var flooPosCompound = tag.getCompound("floo");
		serverState.flooState.getFlooPositions().clear();
		for (var name : flooPosCompound.getKeys()) {
			var flooCompound = flooPosCompound.getCompound(name);
			var storage = FlooPosStorage.fromNbt(flooCompound);
			serverState.flooState.getFlooPositions().put(name, storage);
		}
		return serverState;
	}
	
	public SpellPowerLevel getCurrentSpellPowerLevel(PlayerEntity player) {
		var playerState = getPlayerState(player);
		return playerState.getCurrentSpellPowerLevel();
	}
	
	public void setCurrentSpellPowerLevel(PlayerEntity player, SpellPowerLevel powerLevel) {
		var playerState = getPlayerState(player);
		playerState.setCurrentSpellPowerLevel(powerLevel);
		markDirty();
		syncPowerLevels(player);
	}
	
	public SpellPowerLevel getPowerLevel(PlayerEntity player) {
		var playerState = getPlayerState(player);
		return playerState.getPowerLevel();
	}
	
	public void setPowerLevel(PlayerEntity player, SpellPowerLevel powerLevel) {
		var playerState = getPlayerState(player);
		playerState.setPowerLevel(powerLevel);
		markDirty();
		syncPowerLevels(player);
	}
	
	public boolean increasePowerLevel(PlayerEntity player) {
		var playerState = getPlayerState(player);
		var ret = playerState.increasePowerLevel();
		markDirty();
		syncPowerLevels(player);
		return ret;
	}
	
	public void decreasePowerLevel(PlayerEntity player) {
		var playerState = getPlayerState(player);
		playerState.decreasePowerLevel();
		syncPowerLevels(player);
	}
	
	public static String getSuitedWand(PlayerEntity player) {
		var serverState = getServerState(player.getWorld().getServer());
		var playerState = getPlayerState(player);
		var suitedWand = playerState.getSuitedWand();
		if (suitedWand.isEmpty()) {
			playerState.setSuitedWand(Registries.ITEM.getId(OllivandersItems.WANDS.getRandom(player)).toString());
			serverState.markDirty();
			return playerState.getSuitedWand();
		} else {
			return suitedWand;
		}
	}
	
	public static void setSuitedWand(PlayerEntity player, String suitedWand) {
		var serverState = getServerState(player.getWorld().getServer());
		var playerState = getPlayerState(player);
		playerState.setSuitedWand(suitedWand);
		serverState.markDirty();
	}
	
	public static String getSuitedCore(PlayerEntity player) {
		var serverState = getServerState(player.getWorld().getServer());
		var playerState = getPlayerState(player);
		var suitedCore = playerState.getSuitedCore();
		if (suitedCore.isEmpty()) {
			playerState.setSuitedCore(OllivandersRegistries.CORE.getId(OllivandersCores.CORES.getRandom(player)).toString());
			serverState.markDirty();
			return playerState.getSuitedCore();
		} else {
			return suitedCore;
		}
	}
	
	public static void setSuitedCore(PlayerEntity player, String suitedCore) {
		var serverState = getServerState(player.getWorld().getServer());
		var playerState = getPlayerState(player);
		playerState.setSuitedCore(suitedCore);
		serverState.markDirty();
	}
	
	public static String getCurrentSpell(PlayerEntity player) {
		var playerState = getPlayerState(player);
		return playerState.getCurrentSpell();
	}
	
	public static void setCurrentSpell(PlayerEntity player, String currentSpell) {
		var serverState = getServerState(player.getWorld().getServer());
		var playerState = getPlayerState(player);
		playerState.setCurrentSpell(currentSpell);
		serverState.markDirty();
	}
	
	public void syncPowerLevels(PlayerEntity player) {
		var currentSpellPowerLevel = getCurrentSpellPowerLevel(player);
		var powerLevel = getPowerLevel(player);
		var buf = PacketByteBufs.create();
		buf.writeInt(currentSpellPowerLevel.getId());
		buf.writeInt(powerLevel.getId());
		ServerPlayNetworking.send((ServerPlayerEntity) player, OllivandersNetworking.SYNC_POWER_LEVELS, buf);
	}
	
	public static OllivandersServerState getServerState(MinecraftServer server) {
		var persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
		return persistentStateManager.getOrCreate(OllivandersServerState::readNbt, OllivandersServerState::new, Ollivanders.ID);
	}
	
	public static OllivandersPlayerState getPlayerState(PlayerEntity player) {
		var serverState = getServerState(player.getWorld().getServer());
		return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new OllivandersPlayerState());
	}
	
	public static OllivandersFlooState getFlooState(MinecraftServer server) {
		var serverState = getServerState(server);
		return serverState.flooState;
	}
	
	public void addFlooPosition(String name, BlockPos pos, Direction direction, Identifier dimension, boolean chosenRandomly) {
		flooState.addFlooPosition(name, pos, direction, dimension, chosenRandomly);
		markDirty();
	}
	
	public void removeFlooPosition(String name) {
		flooState.removeFlooPosition(name);
		markDirty();
	}
	
	public void removeFlooByPos(BlockPos pos) {
		flooState.removeFlooByPos(pos);
		markDirty();
	}
	
	public @Nullable FlooPosStorage getFlooByNameOrRandom(String name) {
		return flooState.getFlooByNameOrRandom(name);
	}
	
	public @Nullable FlooPosStorage getFlooPosByName(String name) {
		return flooState.getFlooPosByName(name);
	}
	
	public @Nullable String getFlooNameByPos(BlockPos pos) {
		return flooState.getFlooNameByPos(pos);
	}
	
	public Optional<FlooPosStorage> getRandomFlooPos() {
		return flooState.getRandomFlooPos();
	}
	
	public OllivandersFlooState getFlooState() {
		return flooState;
	}
	
	public void addSkillLevel(PlayerEntity player, Spell spell, double amount) {
		var playerState = getPlayerState(player);
		playerState.addSkillLevel(spell, amount);
		markDirty();
	}
	
	public void subtractSkillLevel(PlayerEntity player, Spell spell, double amount) {
		var playerState = getPlayerState(player);
		playerState.subtractSkillLevel(spell, amount);
		markDirty();
	}
	
	public static double getSkillLevel(PlayerEntity player, Spell spell) {
		var playerState = getPlayerState(player);
		return playerState.getSkillLevel(spell);
	}
}