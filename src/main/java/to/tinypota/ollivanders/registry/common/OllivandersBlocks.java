package to.tinypota.ollivanders.registry.common;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;
import to.tinypota.ollivanders.registry.builder.WoodBlockStorage;

public class OllivandersBlocks {
	public static final WoodBlockStorage LAUREL_WOOD = WoodBlockRegistry.registerWood("laurel", MapColor.BROWN, MapColor.BROWN);
	public static final WoodBlockStorage REDWOOD = WoodBlockRegistry.registerWood("redwood", MapColor.DARK_RED, MapColor.DARK_RED);
	
	public static void init() {
	
	}
	
	public static <B extends Block> B register(String name, B block) {
		return Registry.register(Registries.BLOCK, Ollivanders.id(name), block);
	}
	
	public static <B extends Block> B register(String name, B block, Item.Settings itemSettings) {
		BlockItem blockItem = new BlockItem(block, itemSettings);
		Registry.register(Registries.BLOCK, Ollivanders.id(name), block);
		Registry.register(Registries.ITEM, Ollivanders.id(name), blockItem);
		OllivandersItemGroups.addToDefault(blockItem);
		return block;
	}
}
