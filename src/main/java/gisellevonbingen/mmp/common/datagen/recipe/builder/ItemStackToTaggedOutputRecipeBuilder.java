package gisellevonbingen.mmp.common.datagen.recipe.builder;

import gisellevonbingen.mmp.common.crafting.CrushingTaggedOutputRecipe;
import gisellevonbingen.mmp.common.crafting.EnrichingTaggedOutputRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.datagen.recipe.MekanismRecipeBuilder;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;

@NothingNullByDefault
public class ItemStackToTaggedOutputRecipeBuilder extends MekanismRecipeBuilder<ItemStackToTaggedOutputRecipeBuilder>
{
	private final ItemStackToTaggedOutputRecipeBuilder.Factory factory;
	private final ItemStackIngredient input;
	private final ItemStackIngredient output;

	protected ItemStackToTaggedOutputRecipeBuilder(ItemStackIngredient input, ItemStackIngredient output, ItemStackToTaggedOutputRecipeBuilder.Factory factory)
	{
		this.input = input;
		this.output = output;
		this.factory = factory;
	}

	public static ItemStackToTaggedOutputRecipeBuilder enriching(ItemStackIngredient input, ItemStackIngredient output)
	{
		return new ItemStackToTaggedOutputRecipeBuilder(input, output, EnrichingTaggedOutputRecipe::new);
	}

	public static ItemStackToTaggedOutputRecipeBuilder crushing(ItemStackIngredient input, ItemStackIngredient output)
	{
		return new ItemStackToTaggedOutputRecipeBuilder(input, output, CrushingTaggedOutputRecipe::new);
	}

	@Override
	protected ItemStackToItemStackRecipe asRecipe()
	{
		return this.factory.create(this.input, this.output);
	}

	@FunctionalInterface
	public interface Factory
	{
		ItemStackToItemStackRecipe create(ItemStackIngredient input, ItemStackIngredient output);
	}

}
