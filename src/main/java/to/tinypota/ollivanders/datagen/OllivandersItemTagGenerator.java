package to.tinypota.ollivanders.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;
import to.tinypota.ollivanders.registry.common.OllivandersCores;
import to.tinypota.ollivanders.registry.common.OllivandersItems;

import java.util.concurrent.CompletableFuture;

public class OllivandersItemTagGenerator extends FabricTagProvider.ItemTagProvider {
	public static final TagKey<Item> WANDS = TagKey.of(RegistryKeys.ITEM, Ollivanders.id("wands"));
	public static final TagKey<Item> CORES = TagKey.of(RegistryKeys.ITEM, Ollivanders.id("cores"));
	
	public OllivandersItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup arg) {
		for (var storage : WoodBlockRegistry.WOOD_BLOCK_STORAGES) {
			var logTag = TagKey.of(RegistryKeys.ITEM, new Identifier("c", storage.getName() + "_logs"));
			getOrCreateTagBuilder(logTag).add(storage.getLog().asItem(), storage.getWood().asItem(), storage.getStrippedLog().asItem(), storage.getStrippedWood().asItem());
			getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS).add(storage.getButton().asItem());
			getOrCreateTagBuilder(ItemTags.WOODEN_DOORS).add(storage.getDoor().asItem());
			getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(storage.getFence().asItem());
			getOrCreateTagBuilder(ItemTags.FENCE_GATES).add(storage.getFenceGate().asItem());
			getOrCreateTagBuilder(ItemTags.LEAVES).add(storage.getLeaves().asItem());
			getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN).addTag(logTag);
			getOrCreateTagBuilder(ItemTags.PLANKS).add(storage.getPlanks().asItem());
			getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES).add(storage.getPressurePlate().asItem());
			getOrCreateTagBuilder(ItemTags.SAPLINGS).add(storage.getSapling().asItem());
			getOrCreateTagBuilder(ItemTags.WOODEN_SLABS).add(storage.getSlab().asItem());
			getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS).add(storage.getStairs().asItem());
			getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS).add(storage.getTrapdoor().asItem());
		}
		
		for (var wand : OllivandersItems.WANDS.getEntries()) {
			getOrCreateTagBuilder(WANDS).add(wand.getObject());
		}
		
		for (var core : OllivandersCores.CORES.getEntries()) {
			getOrCreateTagBuilder(CORES).add(core.getObject().getItem());
		}
	}
}