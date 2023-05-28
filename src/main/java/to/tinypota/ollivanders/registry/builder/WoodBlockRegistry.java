package to.tinypota.ollivanders.registry.builder;

import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.sapling.OakSaplingGenerator;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.HangingSignItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.item.TallBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import to.tinypota.ollivanders.common.block.OllivandersHangingSignBlock;
import to.tinypota.ollivanders.common.block.OllivandersSignBlock;
import to.tinypota.ollivanders.common.block.OllivandersWallHangingSignBlock;
import to.tinypota.ollivanders.common.block.OllivandersWallSignBlock;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;
import to.tinypota.ollivanders.registry.common.OllivandersItems;

import java.util.ArrayList;

public class WoodBlockRegistry {
	public static final ArrayList<WoodBlockStorage> WOOD_BLOCK_STORAGES = new ArrayList<>();
	
	//TODO: Fix saplings. Additionally data gen crafting recipes.
	public static WoodBlockStorage registerWood(String translationName, String name, SaplingGenerator generator, MapColor color, MapColor topColor) {
		var woodType = WoodBlockRegistry.createWoodType(name);
		var log = OllivandersBlocks.register(name + "_log", new PillarBlock(setLogSettings(Block.Settings.copy(Blocks.OAK_LOG), topColor, color)), new Item.Settings());
		var wood = OllivandersBlocks.register(name + "_wood", new PillarBlock(Block.Settings.copy(Blocks.OAK_WOOD)), new Item.Settings());
		var strippedLog = OllivandersBlocks.register("stripped_" + name + "_log", new PillarBlock(Block.Settings.copy(Blocks.STRIPPED_OAK_LOG)), new Item.Settings());
		var strippedWood = OllivandersBlocks.register("stripped_" + name + "_wood", new PillarBlock(Block.Settings.copy(Blocks.STRIPPED_OAK_WOOD)), new Item.Settings());
		var planks = OllivandersBlocks.register(name + "_planks", new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)), new Item.Settings());
		var stairs = OllivandersBlocks.register(name + "_stairs", new StairsBlock(planks.getDefaultState(), Block.Settings.copy(planks)), new Item.Settings());
		var slab = OllivandersBlocks.register(name + "_slab", new SlabBlock(Block.Settings.copy(Blocks.OAK_SLAB)), new Item.Settings());
		
		var fence = OllivandersBlocks.register(name + "_fence", new FenceBlock(Block.Settings.copy(Blocks.OAK_FENCE)), new Item.Settings());
		var fenceGate = OllivandersBlocks.register(name + "_fence_gate", new FenceGateBlock(Block.Settings.copy(Blocks.OAK_FENCE_GATE), woodType), new Item.Settings());
		var door = OllivandersBlocks.register(name + "_door", new DoorBlock(Block.Settings.copy(Blocks.OAK_DOOR), woodType.setType()));
		var doorItem = OllivandersItems.register(name + "_door", new TallBlockItem(door, new Item.Settings()));
		
		var trapdoor = OllivandersBlocks.register(name + "_trapdoor", new TrapdoorBlock(Block.Settings.copy(Blocks.OAK_TRAPDOOR), woodType.setType()), new Item.Settings());
		var pressurePlate = OllivandersBlocks.register(name + "_pressure_plate", new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, Block.Settings.copy(Blocks.OAK_PRESSURE_PLATE), woodType.setType()), new Item.Settings());
		var button = OllivandersBlocks.register(name + "_button", new ButtonBlock(Block.Settings.copy(Blocks.OAK_BUTTON), woodType.setType(), 30, true), new Item.Settings());
		
		var sign = OllivandersBlocks.register(name + "_sign", new OllivandersSignBlock(AbstractBlock.Settings.copy(Blocks.OAK_SIGN), woodType));
		var wallSign = OllivandersBlocks.register(name + "_wall_sign", new OllivandersWallSignBlock(AbstractBlock.Settings.copy(Blocks.OAK_WALL_SIGN), woodType));
		var signItem = OllivandersItems.register(name + "_sign", new SignItem(new Item.Settings().maxCount(16), sign, wallSign));
		var hangingSign = OllivandersBlocks.register(name + "_hanging_sign", new OllivandersHangingSignBlock(AbstractBlock.Settings.copy(Blocks.OAK_HANGING_SIGN), woodType));
		var wallHangingSign = OllivandersBlocks.register(name + "_wall_hanging_sign", new OllivandersWallHangingSignBlock(AbstractBlock.Settings.copy(Blocks.OAK_WALL_HANGING_SIGN), woodType));
		var hangingSignItem = OllivandersItems.register(name + "_hanging_sign", new HangingSignItem(hangingSign, wallHangingSign, new Item.Settings().maxCount(16)));
		
		var sapling = OllivandersBlocks.register(name + "_sapling", new SaplingBlock(generator, Block.Settings.copy(Blocks.OAK_SAPLING)), new Item.Settings());
		var leaves = OllivandersBlocks.register(name + "_leaves", new LeavesBlock(Block.Settings.copy(Blocks.OAK_LEAVES)), new Item.Settings());
		
		var family = BlockFamilies.register(planks).button(button).fence(fence).fenceGate(fenceGate).pressurePlate(pressurePlate).sign(sign, wallSign).slab(slab).stairs(stairs).door(door).trapdoor(trapdoor).group("wooden").unlockCriterionName("has_planks").build();
		var blockStorage = new WoodBlockStorage(translationName, name, sapling, leaves, log, strippedLog, wood, strippedWood, planks, stairs, slab, fence, fenceGate, door, doorItem, button, trapdoor, pressurePlate, sign, wallSign, hangingSign, wallHangingSign, family);
		
		StrippableBlockRegistry.register(log, strippedLog);
		StrippableBlockRegistry.register(wood, strippedWood);
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