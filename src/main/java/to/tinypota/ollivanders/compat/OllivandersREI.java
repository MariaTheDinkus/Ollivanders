package to.tinypota.ollivanders.compat;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.client.categories.cooking.DefaultCookingCategory;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultCookingDisplay;
import me.shedaniel.rei.plugin.common.displays.cooking.DefaultSmeltingDisplay;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.recipe.LatheRecipe;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;
import to.tinypota.ollivanders.registry.common.OllivandersRecipeTypes;

public class OllivandersREI implements REIClientPlugin {
	CategoryIdentifier<DefaultSmeltingDisplay> LATHE = CategoryIdentifier.of(Ollivanders.ID, "plugins/lathe");
	
	@Override
	public void registerCategories(CategoryRegistry registry) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment())
			Ollivanders.LOGGER.info("REGISTERING REI STUFF");
		registry.add(new DefaultCookingCategory(LATHE, EntryStacks.of(OllivandersBlocks.LATHE), "category.rei.lathe"));
		registry.addWorkstations(LATHE, EntryStacks.of(OllivandersBlocks.LATHE));
	}
	
	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registry.registerRecipeFiller(LatheRecipe.class, OllivandersRecipeTypes.LATHE, latheRecipe -> new DefaultCookingDisplay(latheRecipe) {
			@Override
			public CategoryIdentifier<?> getCategoryIdentifier() {
				return LATHE;
			}
		});
	}
}
