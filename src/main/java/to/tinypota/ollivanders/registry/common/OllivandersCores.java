package to.tinypota.ollivanders.registry.common;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.core.Core;
import to.tinypota.ollivanders.common.spell.ClearEffectSpell;
import to.tinypota.ollivanders.common.spell.EffectSpell;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.spell.SpellType;
import to.tinypota.ollivanders.common.util.WeightedRandomBag;

public class OllivandersCores {
	public static final WeightedRandomBag<Core> CORES = new WeightedRandomBag<>();
	public static final Core EMPTY = OllivandersCores.register("empty", new Core(Items.AIR, 0));
	public static final Core BONE = OllivandersCores.register("bone", new Core(Items.BONE, 80));
	public static final Core SPIDER_EYE = OllivandersCores.register("spider_eye", new Core(Items.SPIDER_EYE, 80));
	public static final Core ENDER_PEARL = OllivandersCores.register("ender_pearl", new Core(Items.ENDER_PEARL, 60));
	public static final Core BLAZE_POWDER = OllivandersCores.register("blaze_powder", new Core(Items.BLAZE_POWDER, 60));
	public static final Core EYE_OF_ENDER = OllivandersCores.register("eye_of_ender", new Core(Items.ENDER_EYE, 40));
	public static final Core DRAGON_HEARTSTRING = OllivandersCores.register("dragon_heartstring", new Core(OllivandersItems.DRAGON_HEARTSTRING, 20));
	public static final Core PHOENIX_FEATHER = OllivandersCores.register("phoenix_feather", new Core(OllivandersItems.PHOENIX_FEATHER, 20));
	public static final Core THESTRAL_TAIL_HAIR = OllivandersCores.register("thestral_tail_hair", new Core(OllivandersItems.THESTRAL_TAIL_HAIR, 20));
	public static final Core UNICORN_TAIL_HAIR = OllivandersCores.register("unicorn_tail_hair", new Core(OllivandersItems.UNICORN_TAIL_HAIR, 20));
//	public static final Spell LUMOS = register("lumos", new EffectSpell("lumos", new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20 * 30), new Spell.Settings().type(SpellType.SELF)));
//	public static final Spell NOX = register("nox", new ClearEffectSpell("nox", StatusEffects.NIGHT_VISION, new Spell.Settings().type(SpellType.SELF)));
	
	public static void init() {
	
	}
	
	public static <C extends Core> C register(String name, C core) {
		C result = Registry.register(OllivandersRegistries.CORE, Ollivanders.id(name), core);
		if (core != EMPTY) {
			CORES.addEntry(core, core.getRarity());
		}
		return result;
	}
}
