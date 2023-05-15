package to.tinypota.ollivanders.registry.builder;

import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.sapling.OakSaplingGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.TallBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;
import to.tinypota.ollivanders.registry.common.OllivandersItems;

import java.util.ArrayList;

public class WoodBlockRegistry {
	public static final ArrayList<WoodBlockStorage> WOOD_BLOCK_STORAGES = new ArrayList<>();
	
	//TODO: Fix saplings. Additionally data gen creating blockstates, models, crafting recipes, and lang file entries for all of these.
	public static WoodBlockStorage registerWood(String name, MapColor color, MapColor topColor) {
		var woodType = WoodBlockRegistry.createWoodType(name);
		var log = OllivandersBlocks.register(name + "_log", new PillarBlock(setLogSettings(Block.Settings.copy(Blocks.OAK_LOG), topColor, color)), new Item.Settings());
		var wood = OllivandersBlocks.register(name + "_wood", new PillarBlock(Block.Settings.copy(Blocks.OAK_WOOD)), new Item.Settings());
		var stripped_log = OllivandersBlocks.register("stripped_" + name + "_log", new PillarBlock(Block.Settings.copy(Blocks.STRIPPED_OAK_LOG)), new Item.Settings());
		var stripped_wood = OllivandersBlocks.register("stripped_" + name + "_wood", new PillarBlock(Block.Settings.copy(Blocks.STRIPPED_OAK_WOOD)), new Item.Settings());
		var planks = OllivandersBlocks.register(name + "_planks", new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)), new Item.Settings());
		var stairs = OllivandersBlocks.register(name + "_stairs", new StairsBlock(planks.getDefaultState(), Block.Settings.copy(planks)), new Item.Settings());
		var slab = OllivandersBlocks.register(name + "_slab", new SlabBlock(Block.Settings.copy(Blocks.OAK_SLAB)), new Item.Settings());
		
		//TODO: Data gen tags for fence and fence gate connections.
		var fence = OllivandersBlocks.register(name + "_fence", new FenceBlock(Block.Settings.copy(Blocks.OAK_FENCE)), new Item.Settings());
		var fence_gate = OllivandersBlocks.register(name + "_fence_gate", new FenceGateBlock(Block.Settings.copy(Blocks.OAK_FENCE_GATE), woodType), new Item.Settings());
		//TODO: Fix door blockstate/model.
		var door = OllivandersBlocks.register(name + "_door", new DoorBlock(Block.Settings.copy(Blocks.OAK_DOOR), woodType.setType()));
		var door_item = OllivandersItems.register(name + "_door", new TallBlockItem(door, new Item.Settings()));
		
		var trapdoor = OllivandersBlocks.register(name + "_trapdoor", new TrapdoorBlock(Block.Settings.copy(Blocks.OAK_TRAPDOOR), woodType.setType()), new Item.Settings());
		var pressure_plate = OllivandersBlocks.register(name + "_pressure_plate", new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, Block.Settings.copy(Blocks.OAK_PRESSURE_PLATE), woodType.setType()), new Item.Settings());
		var button = OllivandersBlocks.register(name + "_button", new ButtonBlock(Block.Settings.copy(Blocks.OAK_BUTTON), woodType.setType(), 30, true), new Item.Settings());
		
		var sapling = OllivandersBlocks.register(name + "_sapling", new SaplingBlock(new OakSaplingGenerator(), Block.Settings.copy(Blocks.OAK_SAPLING)), new Item.Settings());
		var leaves = OllivandersBlocks.register(name + "_leaves", new LeavesBlock(Block.Settings.copy(Blocks.OAK_LEAVES)), new Item.Settings());
		
		var blockStorage = new WoodBlockStorage(sapling, leaves, log, stripped_log, wood, stripped_wood, planks, stairs, slab, fence, fence_gate, door, door_item, button, trapdoor, pressure_plate);
		WOOD_BLOCK_STORAGES.add(blockStorage);
		return blockStorage;
	}
	
	public static WoodType createWoodType(String name) {
		return WoodType.register(new WoodType(name, BlockSetType.register(new BlockSetType(name))));
	}
	
	private static AbstractBlock.Settings setLogSettings(Block.Settings settings, MapColor topMapColor, MapColor sideMapColor) {
		return settings.mapColor(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor).instrument(Instrument.BASS).strength(2.0f).sounds(BlockSoundGroup.WOOD).burnable();
	}
}