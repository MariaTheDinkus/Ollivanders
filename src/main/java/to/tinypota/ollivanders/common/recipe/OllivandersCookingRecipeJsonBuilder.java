package to.tinypota.ollivanders.common.recipe;

import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import to.tinypota.ollivanders.registry.common.OllivandersRecipeSerializers;

public class OllivandersCookingRecipeJsonBuilder extends CookingRecipeJsonBuilder {
	private OllivandersCookingRecipeJsonBuilder(RecipeCategory category, CookingRecipeCategory cookingCategory, ItemConvertible output, Ingredient input, float experience, int cookingTime, RecipeSerializer<? extends AbstractCookingRecipe> serializer) {
		super(category, cookingCategory, output, input, experience, cookingTime, serializer);
	}
	
	public static CookingRecipeJsonBuilder createLathe(Ingredient input, RecipeCategory category, ItemConvertible output, float experience, int cookingTime) {
		return new CookingRecipeJsonBuilder(category, CookingRecipeCategory.MISC, output, input, experience, cookingTime, OllivandersRecipeSerializers.LATHE);
	}
	
	public static CookingRecipeJsonBuilder createMortarAndPestle(Ingredient input, RecipeCategory category, ItemConvertible output, float experience, int cookingTime) {
		return new CookingRecipeJsonBuilder(category, CookingRecipeCategory.MISC, output, input, experience, cookingTime, OllivandersRecipeSerializers.MORTAR_AND_PESTLE);
	}
}
