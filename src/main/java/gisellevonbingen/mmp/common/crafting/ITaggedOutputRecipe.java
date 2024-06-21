package gisellevonbingen.mmp.common.crafting;

import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;

public interface ITaggedOutputRecipe
{
	ItemStack getItemStackResult();

	ItemStackIngredient getTaggedResult();
}
