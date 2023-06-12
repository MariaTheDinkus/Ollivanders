package to.tinypota.ollivanders.registry.common;

import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.recipe.LatheRecipe;

public class OllivandersRecipeSerializers {
	public static final RecipeSerializer<LatheRecipe> LATHE = RecipeSerializer.register(Ollivanders.id("lathe").toString(), new CookingRecipeSerializer<>(LatheRecipe::new, 100));
	
	public static void init() {
	
	}
}
