package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;
import to.tinypota.ollivanders.registry.builder.WoodBlockStorage;

public class OllivandersBlocks {
	public static final WoodBlockStorage LAUREL_WOOD = WoodBlockRegistry.registerWood("Laurel", "laurel", MapColor.BROWN, MapColor.BROWN);
	public static final WoodBlockStorage REDWOOD = WoodBlockRegistry.registerWood("Redwood", "redwood", MapColor.DARK_RED, MapColor.DARK_RED);
	public static final FlooFireBlock FLOO_FIRE = register("floo_fire", new FlooFireBlock(FabricBlockSettings.create().mapColor(MapColor.EMERALD_GREEN).noCollision().strength(0.2f).luminance(state -> state.get(Properties.LIT) ? 15 : 0).sounds(BlockSoundGroup.WOOL).pistonBehavior(PistonBehavior.BLOCK)), new Item.Settings());
	
	public static void init() {
	
	}
	
	public static <B extends Block> B register(String name, B block) {
		return Registry.register(Registries.BLOCK, Ollivanders.id(name), block);
	}
	
	public static <B extends Block> B register(String name, B block, Item.Settings itemSettings) {
		var blockItem = new BlockItem(block, itemSettings);
		Registry.register(Registries.BLOCK, Ollivanders.id(name), block);
		Registry.register(Registries.ITEM, Ollivanders.id(name), blockItem);
		OllivandersItemGroups.addToDefault(blockItem);
		return block;
	}
}
