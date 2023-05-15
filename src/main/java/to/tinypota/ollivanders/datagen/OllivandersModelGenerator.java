package to.tinypota.ollivanders.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TexturedModel;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;
import to.tinypota.ollivanders.registry.common.OllivandersItems;

public class OllivandersModelGenerator extends FabricModelProvider {
	public OllivandersModelGenerator(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		for (var storage : WoodBlockRegistry.WOOD_BLOCK_STORAGES) {
			blockStateModelGenerator.registerLog(storage.getLog()).log(storage.getLog()).wood(storage.getWood());
			blockStateModelGenerator.registerLog(storage.getStrippedLog()).log(storage.getStrippedLog()).wood(storage.getStrippedWood());
			blockStateModelGenerator.registerTintableCross(storage.getSapling(), BlockStateModelGenerator.TintType.NOT_TINTED);
			blockStateModelGenerator.registerCubeAllModelTexturePool(storage.getPlanks()).stairs(storage.getStairs()).slab(storage.getSlab()).fence(storage.getFence()).fenceGate(storage.getFenceGate()).button(storage.getButton()).pressurePlate(storage.getPressurePlate());
			blockStateModelGenerator.registerDoor(storage.getDoor());
			blockStateModelGenerator.registerTrapdoor(storage.getTrapdoor());
			blockStateModelGenerator.registerSingleton(storage.getLeaves(), TexturedModel.LEAVES);
		}
	}
	
	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		itemModelGenerator.register(OllivandersItems.DRAGON_HEARTSTRING, Models.GENERATED);
		itemModelGenerator.register(OllivandersItems.PHOENIX_FEATHER, Models.GENERATED);
		itemModelGenerator.register(OllivandersItems.THESTRAL_TAIL_HAIR, Models.GENERATED);
		itemModelGenerator.register(OllivandersItems.UNICORN_TAIL_HAIR, Models.GENERATED);
		itemModelGenerator.register(OllivandersItems.FLOO_POWDER, Models.GENERATED);
		
		itemModelGenerator.register(OllivandersItems.OAK_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.SPRUCE_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.BIRCH_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.JUNGLE_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.ACACIA_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.DARK_OAK_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.MANGROVE_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.CHERRY_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.LAUREL_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.REDWOOD_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.VINE_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.CRIMSON_WAND, Models.HANDHELD_ROD);
		itemModelGenerator.register(OllivandersItems.WARPED_WAND, Models.HANDHELD_ROD);
	}
}