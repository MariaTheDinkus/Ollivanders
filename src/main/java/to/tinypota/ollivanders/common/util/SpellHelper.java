package to.tinypota.ollivanders.common.util;

import net.minecraft.entity.player.PlayerEntity;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class SpellHelper {
	private static final ArrayList<String> SPELL_NAMES = new ArrayList<>();
	
	public static Spell getCurrentSpell(PlayerEntity player) {
		return SpellHelper.getSpellByName(OllivandersServerState.getCurrentSpell(player));
	}
	
	public static void setCurrentSpell(PlayerEntity player, Spell spell) {
		OllivandersServerState.setCurrentSpell(player, spell.getCastName());
	}
	
	public static void setCurrentSpell(PlayerEntity player, String name) {
		OllivandersServerState.setCurrentSpell(player, name);
	}
	
	public static void emptyCurrentSpell(PlayerEntity player) {
		OllivandersServerState.setCurrentSpell(player, Spell.EMPTY.getCastName());
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
			if (spell.getCastName().equals(name)) {
				matchedSpell.set(spell);
			}
		});
		return matchedSpell.get();
	}
	
	public static Spell getSpellFromMessage(String message) {
		var matchedSpell = new AtomicReference<>(Spell.EMPTY);
		OllivandersRegistries.SPELL.forEach(spell -> {
			if (message.startsWith(spell.getCastName())) {
				matchedSpell.set(spell);
			}
		});
		return matchedSpell.get();
	}
}
