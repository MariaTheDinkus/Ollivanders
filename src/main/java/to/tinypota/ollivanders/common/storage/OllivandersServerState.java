package to.tinypota.ollivanders.common.storage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.registry.common.OllivandersCores;
import to.tinypota.ollivanders.registry.common.OllivandersItems;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.HashMap;
import java.util.UUID;

public class OllivandersServerState extends PersistentState {
	private HashMap<UUID, OllivandersPlayerState> players = new HashMap<>();
	
	public static OllivandersServerState createFromNbt(NbtCompound tag) {
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
		var serverState = persistentStateManager.getOrCreate(OllivandersServerState::createFromNbt, OllivandersServerState::new, Ollivanders.ID);
		return serverState;
	}
	
	public static OllivandersPlayerState getPlayerState(LivingEntity player) {
		var serverState = getServerState(player.getWorld().getServer());
		var playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new OllivandersPlayerState());
		return playerState;
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		var playersNbtCompound = new NbtCompound();
		players.forEach((UUID, playerState) -> {
			var playerStateNbt = new NbtCompound();
			playerStateNbt.putString("suitedWand", playerState.getSuitedWand());
			playerStateNbt.putString("suitedCore", playerState.getSuitedCore());
			playerStateNbt.putString("currentSpell", playerState.getCurrentSpell());
			var skillLevels = new NbtCompound();
			playerState.getSkillLevels().forEach((spell, level) -> {
				skillLevels.putDouble(OllivandersRegistries.SPELL.getId(spell).toString(), level);
			});
			playerStateNbt.put("skillLevels", skillLevels);
			playersNbtCompound.put(String.valueOf(UUID), playerStateNbt);
		});
		nbt.put("players", playersNbtCompound);
		return nbt;
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
	
	public double getSkillLevel(LivingEntity player, Spell spell) {
		var playerState = getPlayerState(player);
		return playerState.getSkillLevel(spell);
	}
}