package to.tinypota.ollivanders.registry.common;

import net.minecraft.recipe.RecipeType;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.recipe.LatheRecipe;

public class OllivandersRecipeTypes {
	public static final RecipeType<LatheRecipe> LATHE = RecipeType.register(Ollivanders.id("lathe").toString());
	
	public static void init() {
	
	}
}
