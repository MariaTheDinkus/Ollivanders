package to.tinypota.ollivanders.common.storage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.registry.common.OllivandersCores;
import to.tinypota.ollivanders.registry.common.OllivandersItems;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class OllivandersServerState extends PersistentState {
	private final HashMap<UUID, OllivandersPlayerState> players = new HashMap<>();
	private final OllivandersFlooState flooState = new OllivandersFlooState();
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		var playersNbtCompound = new NbtCompound();
		players.forEach((uuid, playerState) -> {
			var stateNbt = playerState.writeToNbt(new NbtCompound());
			playersNbtCompound.put(String.valueOf(uuid), stateNbt);
		});
		nbt.put("players", playersNbtCompound);
		
		var flooPosCompound = new NbtCompound();
		flooState.getFlooPositions().forEach((name, pos) -> {
			var flooCompound = new NbtCompound();
			
			flooCompound.put("pos", NbtHelper.fromBlockPos(pos));
			flooPosCompound.put(name, flooCompound);
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
			var skillLevels = playerTag.getCompound("skillLevels");
			playerState.getSkillLevels().clear();
			for (var spellId : skillLevels.getKeys()) {
				var identifier = new Identifier(spellId);
				var spell = OllivandersRegistries.SPELL.get(identifier);
				double skillLevel = skillLevels.getDouble(spellId);
				playerState.getSkillLevels().put(spell, skillLevel);
			}
			var uuid = UUID.fromString(key);
			serverState.players.put(uuid, playerState);
		});
		
		var flooPosCompound = tag.getCompound("floo");
		serverState.flooState.getFlooPositions().clear();
		for (var name : flooPosCompound.getKeys()) {
			var flooCompound = flooPosCompound.getCompound(name);
			var blockPos = NbtHelper.toBlockPos(flooCompound.getCompound("pos"));
			serverState.flooState.getFlooPositions().put(name, blockPos);
		}
		return serverState;
	}
	
	public static String getSuitedWand(LivingEntity player) {
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
	
	public static void setSuitedWand(LivingEntity player, String suitedWand) {
		var serverState = getServerState(player.getWorld().getServer());
		var playerState = getPlayerState(player);
		playerState.setSuitedWand(suitedWand);
		serverState.markDirty();
	}
	
	public static String getSuitedCore(LivingEntity player) {
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
	
	public static void setSuitedCore(LivingEntity player, String suitedCore) {
		var serverState = getServerState(player.getWorld().getServer());
		var playerState = getPlayerState(player);
		playerState.setSuitedCore(suitedCore);
		serverState.markDirty();
	}
	
	public static String getCurrentSpell(LivingEntity player) {
		var playerState = getPlayerState(player);
		return playerState.getCurrentSpell();
	}
	
	public static void setCurrentSpell(LivingEntity player, String currentSpell) {
		var serverState = getServerState(player.getWorld().getServer());
		var playerState = getPlayerState(player);
		playerState.setCurrentSpell(currentSpell);
		serverState.markDirty();
	}
	
	public static OllivandersServerState getServerState(MinecraftServer server) {
		var persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
		return persistentStateManager.getOrCreate(OllivandersServerState::readNbt, OllivandersServerState::new, Ollivanders.ID);
	}
	
	public static OllivandersPlayerState getPlayerState(LivingEntity player) {
		var serverState = getServerState(player.getWorld().getServer());
		return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new OllivandersPlayerState());
	}
	
	public static OllivandersFlooState getFlooState(MinecraftServer server) {
		var serverState = getServerState(server);
		return serverState.flooState;
	}
	
	public void addFlooPosition(String name, BlockPos pos) {
		flooState.addFlooPosition(name, pos);
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
	
	public @Nullable BlockPos getFlooByNameOrRandom(String name) {
		return flooState.getFlooByNameOrRandom(name);
	}
	
	public @Nullable BlockPos getFlooPosByName(String name) {
		return flooState.getFlooPosByName(name);
	}
	
	public @Nullable String getFlooNameByPos(BlockPos pos) {
		return flooState.getFlooNameByPos(pos);
	}
	
	public Optional<BlockPos> getRandomFlooPos() {
		return flooState.getRandomFlooPos();
	}
	
	public OllivandersFlooState getFlooState() {
		return flooState;
	}
	
	public void addSkillLevel(LivingEntity player, Spell spell, double amount) {
		var playerState = getPlayerState(player);
		playerState.addSkillLevel(spell, amount);
		markDirty();
	}
	
	public void subtractSkillLevel(LivingEntity player, Spell spell, double amount) {
		var playerState = getPlayerState(player);
		playerState.subtractSkillLevel(spell, amount);
		markDirty();
	}
	
	public static double getSkillLevel(LivingEntity player, Spell spell) {
		var playerState = getPlayerState(player);
		return playerState.getSkillLevel(spell);
	}
}