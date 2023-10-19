package to.tinypota.ollivanders.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;
import to.tinypota.ollivanders.registry.common.OllivandersItemGroups;
import to.tinypota.ollivanders.registry.common.OllivandersItems;

public class OllivandersLangGenerator extends FabricLanguageProvider {
	public OllivandersLangGenerator(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generateTranslations(TranslationBuilder translationBuilder) {
		translationBuilder.add(OllivandersItemGroups.OLLIVANDERS_KEY, "Ollivanders");
		translationBuilder.add(OllivandersItemGroups.OLLIVANDERS_WANDS_KEY, "Ollivanders Wands");
		
		translationBuilder.add("category.rei.lathe", "Lathe");
		
		for (var storage : WoodBlockRegistry.WOOD_BLOCK_STORAGES) {
			translationBuilder.add(storage.getLog(), storage.getTranslationName() + " Log");
			translationBuilder.add(storage.getWood(), storage.getTranslationName() + " Wood");
			translationBuilder.add(storage.getStrippedLog(), storage.getTranslationName() + " Stripped Log");
			translationBuilder.add(storage.getStrippedWood(), storage.getTranslationName() + " Stripped Wood");
			translationBuilder.add(storage.getPlanks(), storage.getTranslationName() + " Planks");
			translationBuilder.add(storage.getStairs(), storage.getTranslationName() + " Stairs");
			translationBuilder.add(storage.getSlab(), storage.getTranslationName() + " Slab");
			translationBuilder.add(storage.getFence(), storage.getTranslationName() + " Fence");
			translationBuilder.add(storage.getFenceGate(), storage.getTranslationName() + " Fence Gate");
			translationBuilder.add(storage.getDoor(), storage.getTranslationName() + " Door");
			translationBuilder.add(storage.getTrapdoor(), storage.getTranslationName() + " Trapdoor");
			translationBuilder.add(storage.getPressurePlate(), storage.getTranslationName() + " Pressure Plate");
			translationBuilder.add(storage.getButton(), storage.getTranslationName() + " Button");
			translationBuilder.add(storage.getSapling(), storage.getTranslationName() + " Sapling");
			translationBuilder.add(storage.getLeaves(), storage.getTranslationName() + " Leaves");
			translationBuilder.add(storage.getSign(), storage.getTranslationName() + " Sign");
			translationBuilder.add(storage.getHangingSign(), storage.getTranslationName() + " Hanging Sign");
		}
		
		translationBuilder.add(OllivandersBlocks.LATHE, "Lathe");
		translationBuilder.add(OllivandersBlocks.MORTAR_AND_PESTLE, "Mortar and Pestle");
		translationBuilder.add(OllivandersBlocks.VANISHING_CABINET, "Vanishing Cabinet");
		
		translationBuilder.add(OllivandersItems.OAK_WAND, "Oak Wand");
		translationBuilder.add(OllivandersItems.SPRUCE_WAND, "Spruce Wand");
		translationBuilder.add(OllivandersItems.BIRCH_WAND, "Birch Wand");
		translationBuilder.add(OllivandersItems.JUNGLE_WAND, "Jungle Wand");
		translationBuilder.add(OllivandersItems.ACACIA_WAND, "Acacia Wand");
		translationBuilder.add(OllivandersItems.DARK_OAK_WAND, "Dark Oak Wand");
		translationBuilder.add(OllivandersItems.MANGROVE_WAND, "Mangrove Wand");
		translationBuilder.add(OllivandersItems.CHERRY_WAND, "Cherry Wand");
		translationBuilder.add(OllivandersItems.LAUREL_WAND, "Laurel Wand");
		translationBuilder.add(OllivandersItems.REDWOOD_WAND, "Redwood Wand");
		translationBuilder.add(OllivandersItems.VINE_WAND, "Vine Wand");
		translationBuilder.add(OllivandersItems.CRIMSON_WAND, "Crimson Wand");
		translationBuilder.add(OllivandersItems.WARPED_WAND, "Warped Wand");
		
		translationBuilder.add(OllivandersItems.CABINET_CORE, "Cabinet Core");
		translationBuilder.add(OllivandersItems.SPLIT_CABINET_CORE, "Split Cabinet Core");
		
		translationBuilder.add(OllivandersItems.DRAGON_HEARTSTRING, "Dragon Heartstring");
		translationBuilder.add(OllivandersItems.PHOENIX_FEATHER, "Phoenix Feather");
		translationBuilder.add(OllivandersItems.THESTRAL_TAIL_HAIR, "Thestral Tail Hair");
		translationBuilder.add(OllivandersItems.UNICORN_TAIL_HAIR, "Unicorn Tail Hair");
		translationBuilder.add(OllivandersItems.FLOO_POWDER, "Floo Powder");
	}
}