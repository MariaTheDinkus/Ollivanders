package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.block.*;
import to.tinypota.ollivanders.common.block.sapling.LaurelSaplingGenerator;
import to.tinypota.ollivanders.common.block.sapling.RedwoodSaplingGenerator;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;
import to.tinypota.ollivanders.registry.builder.WoodBlockStorage;

public class OllivandersBlocks {
	public static final LatheBlock LATHE = register("lathe", new LatheBlock(FabricBlockSettings.copy(Blocks.OAK_PLANKS).nonOpaque()), new Item.Settings());
	public static final MortarAndPestleBlock MORTAR_AND_PESTLE = register("mortar_and_pestle", new MortarAndPestleBlock(FabricBlockSettings.copy(Blocks.OAK_PLANKS).notSolid().nonOpaque()), new Item.Settings());
	public static final VanishingCabinetBlock VANISHING_CABINET = registerTallBlock("vanishing_cabinet", new VanishingCabinetBlock(FabricBlockSettings.copy(Blocks.OAK_PLANKS).nonOpaque()), new Item.Settings());
	
	public static final WoodBlockStorage LAUREL_WOOD = WoodBlockRegistry.registerWood("Laurel", "laurel", new LaurelSaplingGenerator(), MapColor.BROWN, MapColor.BROWN);
	public static final WoodBlockStorage REDWOOD = WoodBlockRegistry.registerWood("Redwood", "redwood", new RedwoodSaplingGenerator(), MapColor.DARK_RED, MapColor.DARK_RED);
	public static final FlowerBlock FLOO_FLOWER = register("floo_flower", new FlowerBlock(StatusEffects.NAUSEA, 10, FabricBlockSettings.copy(Blocks.POPPY).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).luminance(value -> 7)), new Item.Settings());
	public static final FlooFireBlock FLOO_FIRE = register("floo_fire", new FlooFireBlock(FabricBlockSettings.create().mapColor(MapColor.EMERALD_GREEN).noCollision().strength(0.2f).luminance(state -> state.get(Properties.LIT) ? 15 : 0).sounds(BlockSoundGroup.WOOL).pistonBehavior(PistonBehavior.BLOCK)));
	public static final ChildFlooFireBlock CHILD_FLOO_FIRE = register("child_floo_fire", new ChildFlooFireBlock(FabricBlockSettings.create().mapColor(MapColor.EMERALD_GREEN).noCollision().strength(0.2f).luminance(state -> state.get(Properties.LIT) ? 15 : 0).sounds(BlockSoundGroup.WOOL).pistonBehavior(PistonBehavior.BLOCK)));
	
	public static final CoreBlock PHOENIX_FEATHER_BLOCK = register("phoenix_feather", new CoreBlock(Ollivanders.id("phoenix_feather"), FabricBlockSettings.create().luminance(7).noBlockBreakParticles().noCollision().breakInstantly()));
	public static final CoreBlock THESTRAL_TAIL_HAIR_BLOCK = register("thestral_tail_hair", new CoreBlock(Ollivanders.id("thestral_tail_hair"), FabricBlockSettings.create().luminance(7).noBlockBreakParticles().noCollision().breakInstantly()));
	public static final CoreBlock UNICORN_TAIL_HAIR_BLOCK = register("unicorn_tail_hair", new CoreBlock(Ollivanders.id("unicorn_tail_hair"), FabricBlockSettings.create().luminance(7).noBlockBreakParticles().noCollision().breakInstantly()));
	
	public static void init() {
	
	}
	
	public static <B extends Block> B register(String name, B block) {
		return Registry.register(Registries.BLOCK, Ollivanders.id(name), block);
	}
	
	public static <B extends Block> B registerTallBlock(String name, B block, Item.Settings itemSettings) {
		var blockItem = new TallBlockItem(block, itemSettings);
		Registry.register(Registries.BLOCK, Ollivanders.id(name), block);
		Registry.register(Registries.ITEM, Ollivanders.id(name), blockItem);
		OllivandersItemGroups.addToDefault(blockItem);
		return block;
	}
	
	public static <B extends Block> B register(String name, B block, Item.Settings itemSettings) {
		var blockItem = new BlockItem(block, itemSettings);
		Registry.register(Registries.BLOCK, Ollivanders.id(name), block);
		Registry.register(Registries.ITEM, Ollivanders.id(name), blockItem);
		OllivandersItemGroups.addToDefault(blockItem);
		return block;
	}
}
