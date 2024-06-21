package gisellevonbingen.mmp.common.datagen.recipe.builder;

import gisellevonbingen.mmp.common.crafting.ChemicalCrystallizerTaggedOutputRecipe;
import mekanism.api.datagen.recipe.MekanismRecipeBuilder;
import mekanism.api.recipes.ChemicalCrystallizerRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;

public class CrystallizerTaggedOutputRecipeBuilder extends MekanismRecipeBuilder<CrystallizerTaggedOutputRecipeBuilder>
{
	private final ChemicalStackIngredient<?, ?, ?> input;
	private final ItemStackIngredient output;

	protected CrystallizerTaggedOutputRecipeBuilder(ChemicalStackIngredient<?, ?, ?> input, ItemStackIngredient output)
	{
		this.input = input;
		this.output = output;
	}

	public static CrystallizerTaggedOutputRecipeBuilder crystallizing(ChemicalStackIngredient<?, ?, ?> input, ItemStackIngredient output)
	{
		return new CrystallizerTaggedOutputRecipeBuilder(input, output);
	}

	@Override
	protected ChemicalCrystallizerRecipe asRecipe()
	{
		return new ChemicalCrystallizerTaggedOutputRecipe(this.input, this.output);
	}

}
