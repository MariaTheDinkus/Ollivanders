package to.tinypota.ollivanders.registry.common;

import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.core.Core;
import to.tinypota.ollivanders.common.util.WeightedRandomBag;

public class OllivandersCores {
	public static final WeightedRandomBag<Core> CORES = new WeightedRandomBag<>();
	public static final Core EMPTY = OllivandersCores.register("empty", new Core(Items.AIR, 0));
	public static final Core ENDER_PEARL = OllivandersCores.register("ender_pearl", new Core(Items.ENDER_PEARL, 35));
	public static final Core BLAZE_POWDER = OllivandersCores.register("blaze_powder", new Core(Items.BLAZE_POWDER, 35));
	public static final Core EYE_OF_ENDER = OllivandersCores.register("eye_of_ender", new Core(Items.ENDER_EYE, 35));
	public static final Core ECHO_SHARD = OllivandersCores.register("echo_shard", new Core(Items.ECHO_SHARD, 25));
	public static final Core HEART_OF_THE_SEA = OllivandersCores.register("heart_of_the_sea", new Core(Items.HEART_OF_THE_SEA, 25));
	public static final Core DRAGON_HEARTSTRING = OllivandersCores.register("dragon_heartstring", new Core(OllivandersItems.DRAGON_HEARTSTRING, 40));
	public static final Core UNICORN_TAIL_HAIR = OllivandersCores.register("unicorn_tail_hair", new Core(OllivandersItems.UNICORN_TAIL_HAIR, 40));
	public static final Core PHOENIX_FEATHER = OllivandersCores.register("phoenix_feather", new Core(OllivandersItems.PHOENIX_FEATHER, 20));
	public static final Core THESTRAL_TAIL_HAIR = OllivandersCores.register("thestral_tail_hair", new Core(OllivandersItems.THESTRAL_TAIL_HAIR, 4));
	
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
