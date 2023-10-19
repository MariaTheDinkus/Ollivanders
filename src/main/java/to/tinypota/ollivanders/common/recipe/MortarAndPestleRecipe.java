package to.tinypota.ollivanders.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CookingRecipeCategory;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;
import to.tinypota.ollivanders.registry.common.OllivandersRecipeSerializers;
import to.tinypota.ollivanders.registry.common.OllivandersRecipeTypes;

public class MortarAndPestleRecipe extends AbstractCookingRecipe {
	public MortarAndPestleRecipe(String group, CookingRecipeCategory category, Ingredient input, ItemStack output, float experience, int cookTime) {
		super(OllivandersRecipeTypes.MORTAR_AND_PESTLE, group, category, input, output, experience, cookTime);
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(OllivandersBlocks.MORTAR_AND_PESTLE);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return OllivandersRecipeSerializers.LATHE;
	}
}
