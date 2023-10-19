package to.tinypota.ollivanders.registry.common;

import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.recipe.LatheRecipe;
import to.tinypota.ollivanders.common.recipe.MortarAndPestleRecipe;

public class OllivandersRecipeSerializers {
	public static final RecipeSerializer<LatheRecipe> LATHE = RecipeSerializer.register(Ollivanders.id("lathe").toString(), new CookingRecipeSerializer<>(LatheRecipe::new, 100));
	public static final RecipeSerializer<MortarAndPestleRecipe> MORTAR_AND_PESTLE = RecipeSerializer.register(Ollivanders.id("mortar_and_pestle").toString(), new CookingRecipeSerializer<>(MortarAndPestleRecipe::new, 100));
	
	public static void init() {
	
	}
}
