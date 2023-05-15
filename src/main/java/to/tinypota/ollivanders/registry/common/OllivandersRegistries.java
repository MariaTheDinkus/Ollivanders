package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.*;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.core.Core;
import to.tinypota.ollivanders.common.spell.Spell;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class OllivandersRegistries {
	public static final RegistryKey<Registry<Spell>> SPELL_KEY = RegistryKey.ofRegistry(Ollivanders.id("spell"));
	public static final SimpleDefaultedRegistry<Spell> SPELL = FabricRegistryBuilder.createDefaulted(SPELL_KEY, Ollivanders.id("empty")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final RegistryKey<Registry<Core>> CORE_KEY = RegistryKey.ofRegistry(Ollivanders.id("core"));
	public static final SimpleRegistry<Core> CORE = FabricRegistryBuilder.createSimple(CORE_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();
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
