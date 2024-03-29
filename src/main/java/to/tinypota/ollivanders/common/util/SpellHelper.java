package to.tinypota.ollivanders.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.api.wand.WandMatchLevel;
import to.tinypota.ollivanders.common.item.WandItem;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class SpellHelper {
	private static final ArrayList<String> SPELL_NAMES = new ArrayList<>();
	
	public static Spell getCurrentSpell(PlayerEntity player) {
		var playerState = OllivandersServerState.getPlayerState(player.getServer(), player);
		return SpellHelper.getSpellByName(playerState.getCurrentSpell());
	}
	
	public static void setCurrentSpell(PlayerEntity player, Spell spell) {
		var serverState = OllivandersServerState.getServerState(player.getServer());
		var playerState = serverState.getPlayerState(player);
		playerState.setCurrentSpell(spell.getCastName());
		serverState.syncPowerLevels((ServerPlayerEntity) player);
	}
	
	public static void setCurrentSpell(PlayerEntity player, String name) {
		var serverState = OllivandersServerState.getServerState(player.getServer());
		var playerState = serverState.getPlayerState(player);
		playerState.setCurrentSpell(name);
		serverState.syncPowerLevels((ServerPlayerEntity) player);
	}
	
	public static void emptyCurrentSpell(PlayerEntity player) {
		var serverState = OllivandersServerState.getServerState(player.getServer());
		var playerState = serverState.getPlayerState(player);
		playerState.setCurrentSpell(Spell.EMPTY.getCastName());
		serverState.syncPowerLevels((ServerPlayerEntity) player);
	}
	
	public static void addSpellName(String name) {
		SPELL_NAMES.add(name);
	}
	
	public static ArrayList<String> getSpellNames() {
		return SPELL_NAMES;
	}
	
	public static boolean containsSpellName(String message) {
		for (var name : SPELL_NAMES) {
			if (!name.equals("empty") && message.toLowerCase().startsWith(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static Spell getSpellByName(String name) {
		var matchedSpell = new AtomicReference<>(Spell.EMPTY);
		OllivandersRegistries.SPELL.forEach(spell -> {
			if (spell.getCastName().equals(name.toLowerCase())) {
				matchedSpell.set(spell);
			}
		});
		return matchedSpell.get();
	}
	
	public static Spell getSpellFromMessage(String message) {
		var matchedSpell = new AtomicReference<>(Spell.EMPTY);
		OllivandersRegistries.SPELL.forEach(spell -> {
			if (message.toLowerCase().startsWith(spell.getCastName())) {
				matchedSpell.set(spell);
			}
		});
		return matchedSpell.get();
	}
	
	public static double getCastPercentage(Spell currentSpell, WandMatchLevel wandMatchLevel, SpellPowerLevel powerLevel) {
		var castPercentage = 1 * wandMatchLevel.getDefaultCastPercentage() - (currentSpell.hasCustomCastPercents() ? currentSpell.getCustomCastPercents(powerLevel) : powerLevel.getCastPercentageSubtractor());
		
		return castPercentage;
	}
	
	public static double getCastPercentage(ServerPlayerEntity player) {
		var serverState = OllivandersServerState.getServerState(player.getServer());
		var playerState = serverState.getPlayerState(player);
		var currentSpell = SpellHelper.getCurrentSpell(player);
		if (!player.getMainHandStack().isEmpty() && player.getMainHandStack().getItem() instanceof WandItem && WandHelper.hasCore(player.getMainHandStack())) {
			var wandMatchLevel = WandHelper.getWandMatch(player.getMainHandStack(), player);
			var powerLevel = playerState.getPowerLevel();
			var skillLevel = playerState.getSkillLevel(currentSpell);
			var castPercentage = 1 * wandMatchLevel.getDefaultCastPercentage() - (currentSpell.hasCustomCastPercents() ? currentSpell.getCustomCastPercents(powerLevel) : powerLevel.getCastPercentageSubtractor());
			castPercentage += skillLevel / 100 / 10;
			castPercentage = Math.max(0, Math.min(castPercentage, 1));
			
			return castPercentage;
		}
		return -1;
	}
	
	public static Vec3d getRotationVector(Vec3d startPos, Vec3d endPos) {
		// Calculate the difference vector
		Vec3d diff = endPos.subtract(startPos);
		
		// Calculate the length of the vector
		double length = Math.sqrt(diff.x * diff.x + diff.y * diff.y + diff.z * diff.z);
		
		// Normalize the difference vector
		Vec3d normalized = new Vec3d(diff.x / length, diff.y / length, diff.z / length);
		
		return normalized;
	}
}
