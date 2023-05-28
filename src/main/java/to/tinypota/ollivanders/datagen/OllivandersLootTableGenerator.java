package to.tinypota.ollivanders.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;
import to.tinypota.ollivanders.registry.common.OllivandersItemGroups;
import to.tinypota.ollivanders.registry.common.OllivandersItems;

import java.util.concurrent.CompletableFuture;

public class OllivandersBlockTagGenerator extends FabricTagProvider.BlockTagProvider {
	public OllivandersBlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup arg) {
		for (var storage : WoodBlockRegistry.WOOD_BLOCK_STORAGES) {
			var logTag = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", storage.getName() + "_logs"));
			getOrCreateTagBuilder(logTag).add(storage.getLog(), storage.getWood(), storage.getStrippedLog(), storage.getStrippedWood());
			getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS).add(storage.getButton());
			getOrCreateTagBuilder(BlockTags.WOODEN_DOORS).add(storage.getDoor());
			getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).add(storage.getFence());
			getOrCreateTagBuilder(BlockTags.FENCE_GATES).add(storage.getFenceGate());
			getOrCreateTagBuilder(BlockTags.LEAVES).add(storage.getLeaves());
			getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN).addTag(logTag);
			getOrCreateTagBuilder(BlockTags.OVERWORLD_NATURAL_LOGS).add(storage.getLog());
			getOrCreateTagBuilder(BlockTags.PLANKS).add(storage.getPlanks());
			getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES).add(storage.getPressurePlate());
			getOrCreateTagBuilder(BlockTags.SAPLINGS).add(storage.getSapling());
			getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).add(storage.getSlab());
			getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).add(storage.getStairs());
			getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS).add(storage.getTrapdoor());
			getOrCreateTagBuilder(BlockTags.STANDING_SIGNS).add(storage.getSign());
			getOrCreateTagBuilder(BlockTags.WALL_SIGNS).add(storage.getWallSign());
			getOrCreateTagBuilder(BlockTags.CEILING_HANGING_SIGNS).add(storage.getHangingSign());
			getOrCreateTagBuilder(BlockTags.WALL_HANGING_SIGNS).add(storage.getWallHangingSign());
		}
	}
}