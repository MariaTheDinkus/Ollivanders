package to.tinypota.ollivanders.registry.common;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Rarity;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.item.WandItem;
import to.tinypota.ollivanders.common.util.WandCraftHelper;
import to.tinypota.ollivanders.common.util.WeightedRandomBag;

public class OllivandersItems {
	public static final WeightedRandomBag<WandItem> WANDS = new WeightedRandomBag<>();
	
	public static final WandItem OAK_WAND = register("oak_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem SPRUCE_WAND = register("spruce_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem BIRCH_WAND = register("birch_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem JUNGLE_WAND = register("jungle_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem ACACIA_WAND = register("acacia_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem DARK_OAK_WAND = register("dark_oak_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem MANGROVE_WAND = register("mangrove_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem CHERRY_WAND = register("cherry_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem LAUREL_WAND = register("laurel_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), OllivandersBlocks.LAUREL_WOOD.getLog(), OllivandersBlocks.LAUREL_WOOD.getStrippedLog()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem REDWOOD_WAND = register("redwood_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), OllivandersBlocks.REDWOOD.getLog(), OllivandersBlocks.REDWOOD.getStrippedLog()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem CRIMSON_WAND = register("crimson_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem WARPED_WAND = register("warped_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem VINE_WAND = register("vine_wand", new WandItem(new Item.Settings().maxCount(1).fireproof(), Blocks.VINE), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	
	public static final Item DRAGON_HEARTSTRING = register("dragon_heartstring", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item PHOENIX_FEATHER = register("phoenix_feather", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item THESTRAL_TAIL_HAIR = register("thestral_tail_hair", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item UNICORN_TAIL_HAIR = register("unicorn_tail_hair", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item FLOO_POWDER = register("floo_powder", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
	
	public static void init() {
		WANDS.addEntry(OAK_WAND, 80);
		WANDS.addEntry(SPRUCE_WAND, 80);
		WANDS.addEntry(BIRCH_WAND, 80);
		WANDS.addEntry(JUNGLE_WAND, 70);
		WANDS.addEntry(ACACIA_WAND, 70);
		WANDS.addEntry(DARK_OAK_WAND, 70);
		WANDS.addEntry(MANGROVE_WAND, 50);
		WANDS.addEntry(CHERRY_WAND, 50);
		WANDS.addEntry(LAUREL_WAND, 40);
		WANDS.addEntry(REDWOOD_WAND, 40);
		WANDS.addEntry(CRIMSON_WAND, 20);
		WANDS.addEntry(WARPED_WAND, 20);
		WANDS.addEntry(VINE_WAND, 40);
	}
	
	public static <I extends Item> I register(String name, I item) {
		return register(name, item, OllivandersItemGroups.OLLIVANDERS_KEY);
	}
	
	public static <I extends Item> I register(String name, I item, RegistryKey<ItemGroup> group) {
		I result = Registry.register(Registries.ITEM, Ollivanders.id(name), item);
		OllivandersItemGroups.addToItemGroup(group, item);
		if (item instanceof WandItem) {
			OllivandersItemGroups.addWandsToItemGroup(item);
		}
		return result;
	}
}
