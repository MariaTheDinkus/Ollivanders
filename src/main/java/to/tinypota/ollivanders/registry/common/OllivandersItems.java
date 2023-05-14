package to.tinypota.ollivanders.registry.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Rarity;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.item.WandItem;

public class OllivandersItems {
	public static final WandItem OAK_WAND = register("wand_oak", new WandItem(new Item.Settings().maxCount(1).fireproof()));
	public static final Item DRAGON_HEARTSTRING = register("dragon_heartstring", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item PHOENIX_FEATHER = register("phoenix_feather", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item THESTRAL_TAIL_HAIR = register("thestral_tail_hair", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item UNICORN_TAIL_HAIR = register("unicorn_tail_hair", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item FLOO_POWDER = register("floo_powder", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
	
	public static void init() {
	
	}
	
	public static <I extends Item> I register(String name, I item) {
		return register(name, item, OllivandersItemGroups.OLLIVANDERS_KEY);
	}
	
	public static <I extends Item> I register(String name, I item, RegistryKey<ItemGroup> group) {
		I result = Registry.register(Registries.ITEM, Ollivanders.id(name), item);
		OllivandersItemGroups.addToItemGroup(group, item);
		return result;
	}
}
