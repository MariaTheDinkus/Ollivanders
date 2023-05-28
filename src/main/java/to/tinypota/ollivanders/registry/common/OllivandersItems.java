package to.tinypota.ollivanders.registry.common;

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
import to.tinypota.ollivanders.common.util.WandHelper;
import to.tinypota.ollivanders.common.util.WeightedRandomBag;

public class OllivandersItems {
	public static final WeightedRandomBag<WandItem> WANDS = new WeightedRandomBag<>();
	
	public static final WandItem OAK_WAND = register("oak_wand", new WandItem(Blocks.OAK_PLANKS, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem SPRUCE_WAND = register("spruce_wand", new WandItem(Blocks.SPRUCE_PLANKS, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem BIRCH_WAND = register("birch_wand", new WandItem(Blocks.BIRCH_PLANKS, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem JUNGLE_WAND = register("jungle_wand", new WandItem(Blocks.JUNGLE_PLANKS, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem ACACIA_WAND = register("acacia_wand", new WandItem(Blocks.ACACIA_PLANKS, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem DARK_OAK_WAND = register("dark_oak_wand", new WandItem(Blocks.DARK_OAK_PLANKS, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem MANGROVE_WAND = register("mangrove_wand", new WandItem(Blocks.MANGROVE_PLANKS, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem CHERRY_WAND = register("cherry_wand", new WandItem(Blocks.CHERRY_PLANKS, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem LAUREL_WAND = register("laurel_wand", new WandItem(OllivandersBlocks.LAUREL_WOOD.getPlanks(), new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem REDWOOD_WAND = register("redwood_wand", new WandItem(OllivandersBlocks.REDWOOD.getPlanks(), new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem CRIMSON_WAND = register("crimson_wand", new WandItem(Blocks.CRIMSON_PLANKS, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem WARPED_WAND = register("warped_wand", new WandItem(Blocks.WARPED_PLANKS, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	public static final WandItem VINE_WAND = register("vine_wand", new WandItem(Blocks.VINE, new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERS_WANDS_KEY);
	
	public static final Item DRAGON_HEARTSTRING = register("dragon_heartstring", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item PHOENIX_FEATHER = register("phoenix_feather", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item THESTRAL_TAIL_HAIR = register("thestral_tail_hair", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item UNICORN_TAIL_HAIR = register("unicorn_tail_hair", new Item(new Item.Settings().rarity(Rarity.RARE)));
	public static final Item FLOO_POWDER = register("floo_powder", new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
	
	public static void init() {
		//TODO: Make all of these actual JSON recipes.
		WandCraftHelper.putCraft(Items.OAK_PLANKS, OAK_WAND);
		WandCraftHelper.putCraft(Items.SPRUCE_PLANKS, SPRUCE_WAND);
		WandCraftHelper.putCraft(Items.BIRCH_PLANKS, BIRCH_WAND);
		WandCraftHelper.putCraft(Items.JUNGLE_PLANKS, JUNGLE_WAND);
		WandCraftHelper.putCraft(Items.ACACIA_PLANKS, ACACIA_WAND);
		WandCraftHelper.putCraft(Items.DARK_OAK_PLANKS, DARK_OAK_WAND);
		WandCraftHelper.putCraft(Items.MANGROVE_PLANKS, MANGROVE_WAND);
		WandCraftHelper.putCraft(Items.CHERRY_PLANKS, CHERRY_WAND);
		WandCraftHelper.putCraft(OllivandersBlocks.LAUREL_WOOD.getPlanks().asItem(), LAUREL_WAND);
		WandCraftHelper.putCraft(OllivandersBlocks.REDWOOD.getPlanks().asItem(), REDWOOD_WAND);
		WandCraftHelper.putCraft(Items.CRIMSON_PLANKS, CRIMSON_WAND);
		WandCraftHelper.putCraft(Items.WARPED_PLANKS, WARPED_WAND);
		WandCraftHelper.putCraft(Items.VINE, VINE_WAND);
		
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
