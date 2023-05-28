package to.tinypota.ollivanders.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.VanillaRecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class OllivandersRecipeGenerator extends FabricRecipeProvider {
	public OllivandersRecipeGenerator(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generate(Consumer<RecipeJsonProvider> exporter) {
		for (var storage : WoodBlockRegistry.WOOD_BLOCK_STORAGES) {
			var logTag = TagKey.of(RegistryKeys.ITEM, new Identifier("c", storage.getName() + "_logs"));
			var hasLog = "has_" + storage.getName() + "_log";
			var hasLogCondition = VanillaRecipeProvider.conditionsFromTag(logTag);
			var hasPlanks = "has_" + storage.getName() + "_planks";
			var hasPlanksCondition = VanillaRecipeProvider.conditionsFromItem(storage.getPlanks().asItem());
			ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getPlanks().asItem(), 4).input(logTag).criterion(hasLog, hasLogCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getWood().asItem(), 3).input('l', storage.getLog().asItem()).pattern("ll").pattern("ll").criterion(hasLog, hasLogCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getStrippedWood().asItem(), 3).input('l', storage.getStrippedLog().asItem()).pattern("ll").pattern("ll").criterion(hasLog, hasLogCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getPressurePlate().asItem()).input('p', storage.getPlanks().asItem()).pattern("pp").criterion(hasPlanks, hasPlanksCondition).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getButton().asItem()).input(storage.getPlanks().asItem()).criterion(hasPlanks, hasPlanksCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getSlab().asItem(), 6).input('p', storage.getPlanks().asItem()).pattern("ppp").criterion(hasPlanks, hasPlanksCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getStairs().asItem(), 4).input('p', storage.getPlanks().asItem()).pattern("p  ").pattern("pp ").pattern("ppp").criterion(hasPlanks, hasPlanksCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getDoor().asItem(), 3).input('p', storage.getPlanks().asItem()).pattern("pp").pattern("pp").pattern("pp").criterion(hasPlanks, hasPlanksCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getTrapdoor().asItem(), 2).input('p', storage.getPlanks().asItem()).pattern("ppp").pattern("ppp").criterion(hasPlanks, hasPlanksCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getFence().asItem(), 3).input('p', storage.getPlanks().asItem()).input('s', Items.STICK).pattern("psp").pattern("psp").criterion(hasPlanks, hasPlanksCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getFenceGate().asItem()).input('p', storage.getPlanks().asItem()).input('s', Items.STICK).pattern("sps").pattern("sps").criterion(hasPlanks, hasPlanksCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getSign().asItem(), 3).input('p', storage.getPlanks().asItem()).input('s', Items.STICK).pattern("ppp").pattern("ppp").pattern(" s ").criterion(hasPlanks, hasPlanksCondition).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, () -> storage.getHangingSign().asItem(), 6).input('s', storage.getStrippedLog().asItem()).input('c', Items.CHAIN).pattern("c c").pattern("sss").pattern("sss").criterion(hasLog, hasLogCondition).offerTo(exporter);
		}
	}
}