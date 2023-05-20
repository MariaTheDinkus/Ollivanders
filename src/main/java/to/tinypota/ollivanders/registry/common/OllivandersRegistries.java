package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleDefaultedRegistry;
import net.minecraft.registry.SimpleRegistry;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.core.Core;
import to.tinypota.ollivanders.common.spell.Spell;

public class OllivandersRegistries {
	public static final RegistryKey<Registry<Spell>> SPELL_KEY = RegistryKey.ofRegistry(Ollivanders.id("spell"));
	public static final SimpleDefaultedRegistry<Spell> SPELL = FabricRegistryBuilder.createDefaulted(SPELL_KEY, Ollivanders.id("empty")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final RegistryKey<Registry<Core>> CORE_KEY = RegistryKey.ofRegistry(Ollivanders.id("core"));
	public static final SimpleRegistry<Core> CORE = FabricRegistryBuilder.createSimple(CORE_KEY).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	
	public static void init() {
	
	}
}
