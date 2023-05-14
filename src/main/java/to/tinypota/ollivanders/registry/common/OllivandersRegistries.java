package to.tinypota.ollivanders.registry.common;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.spell.Spell;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class OllivandersRegistries {
	public static final RegistryKey<Registry<Spell>> SPELL_KEY = RegistryKeys.of("spell");
	public static final Registry<Spell> SPELL = Registries.create(SPELL_KEY, Ollivanders.id("empty").toString(), registry -> Spell.EMPTY);
	private static final ArrayList<String> SPELL_NAMES = new ArrayList<>();
	
	public static void init() {
	
	}
	
	public static void addSpellName(String name) {
		SPELL_NAMES.add(name);
	}
	
	public static ArrayList<String> getSpellNames() {
		return SPELL_NAMES;
	}
	
	public static boolean containsSpellName(String message) {
		for (String name : SPELL_NAMES) {
			if (!name.equals("empty") && message.toLowerCase().startsWith(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static Spell getSpellByName(String name) {
		AtomicReference<Spell> matchedSpell = new AtomicReference<>(Spell.EMPTY);
		SPELL.forEach(spell -> {
			if (spell.getCastName().equals(name)) {
				matchedSpell.set(spell);
			}
		});
		return matchedSpell.get();
	}
	
	public static Spell getSpellFromMessage(String message) {
		AtomicReference<Spell> matchedSpell = new AtomicReference<>(Spell.EMPTY);
		SPELL.forEach(spell -> {
			if (message.startsWith(spell.getCastName())) {
				matchedSpell.set(spell);
			}
		});
		return matchedSpell.get();
	}
}
