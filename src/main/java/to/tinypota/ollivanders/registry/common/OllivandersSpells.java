package to.tinypota.ollivanders.registry.common;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registry;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerStorage;
import to.tinypota.ollivanders.api.spell.SpellType;
import to.tinypota.ollivanders.common.spell.*;
import to.tinypota.ollivanders.common.util.SpellHelper;

public class OllivandersSpells {
	public static final Spell LUMOS = register("lumos", new EffectSpell("lumos", StatusEffects.NIGHT_VISION, new SpellPowerStorage<>(20 * 30, 20 * 60, 20 * 120, 20 * 240, 20 * 480), new Spell.Settings().type(SpellType.SELF)));
	public static final Spell NOX = register("nox", new ClearEffectSpell("nox", StatusEffects.NIGHT_VISION, new Spell.Settings().type(SpellType.SELF)));
	public static final Spell INCENDIO = register("incendio", new IncendioSpell("incendio", new Spell.Settings().type(SpellType.PROJECTILE)));
	public static final Spell ALIQUAM_FLOO = register("aliquam_floo", new AliquamFlooSpell("aliquam floo", new Spell.Settings().type(SpellType.PROJECTILE)));
	public static final Spell APPARATE = register("apparate", new ApparateSpell("apparate", new Spell.Settings().type(SpellType.RAYCAST)));
	
	public static void init() {
	
	}
	
	public static <S extends Spell> S register(String name, S spell) {
		S result = Registry.register(OllivandersRegistries.SPELL, Ollivanders.id(name), spell);
		SpellHelper.addSpellName(spell.getCastName());
		return result;
	}
}
