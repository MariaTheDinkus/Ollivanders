package to.tinypota.ollivanders.registry.common;

import net.minecraft.recipe.RecipeType;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.recipe.LatheRecipe;
import to.tinypota.ollivanders.common.recipe.MortarAndPestleRecipe;

public class OllivandersRecipeTypes {
	public static final RecipeType<LatheRecipe> LATHE = RecipeType.register(Ollivanders.id("lathe").toString());
	public static final RecipeType<MortarAndPestleRecipe> MORTAR_AND_PESTLE = RecipeType.register(Ollivanders.id("mortar_and_pestle").toString());
	
	public static void init() {
	
	}
}
