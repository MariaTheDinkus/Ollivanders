package to.tinypota.ollivanders.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

import java.util.concurrent.CompletableFuture;

public class OllivandersLootTableGenerator extends FabricBlockLootTableProvider {
	public OllivandersLootTableGenerator(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generate() {
		for (var storage : WoodBlockRegistry.WOOD_BLOCK_STORAGES) {
			addDrop(storage.getLog());
			addDrop(storage.getWood());
			addDrop(storage.getStrippedLog());
			addDrop(storage.getStrippedWood());
			addDrop(storage.getPlanks());
			addDrop(storage.getStairs());
			addDrop(storage.getSlab(), block -> slabDrops(storage.getSlab()));
			addDrop(storage.getFence());
			addDrop(storage.getFenceGate());
			addDrop(storage.getDoor());
			addDrop(storage.getTrapdoor());
			addDrop(storage.getPressurePlate());
			addDrop(storage.getButton());
			addDrop(storage.getSign());
			addDrop(storage.getWallSign(), block -> drops(storage.getSign()));
			addDrop(storage.getHangingSign());
			addDrop(storage.getWallHangingSign(), block -> drops(storage.getHangingSign()));
			addDrop(storage.getSapling());
			addDrop(storage.getLeaves(), block -> leavesDrops(block, storage.getSapling(), SAPLING_DROP_CHANCE));
		}
		
		addDrop(OllivandersBlocks.LATHE);
	}
}